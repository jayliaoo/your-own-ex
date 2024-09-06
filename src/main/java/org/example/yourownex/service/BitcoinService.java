package org.example.yourownex.service;

import lombok.*;
import org.bitcoinj.core.*;
import org.bitcoinj.core.listeners.*;
import org.bitcoinj.net.discovery.*;
import org.bitcoinj.params.*;
import org.bitcoinj.script.*;
import org.bitcoinj.store.*;
import org.bitcoinj.wallet.*;
import org.example.yourownex.dao.*;
import org.example.yourownex.jooq.tables.records.*;
import org.springframework.beans.factory.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.concurrent.*;

@Service
public class BitcoinService implements DisposableBean {

    private final Wallet wallet;
    private final PeerGroup peerGroup;
    private final SPVBlockStore blockStore;
    private final AccountService accountService;
    private final AddressDao addressDao;
    private final TestNet3Params params;
    private final Context context;

    @SneakyThrows
    public BitcoinService(AccountService accountService, AddressDao addressDao) {
        this.accountService = accountService;
        this.addressDao = addressDao;

        params = TestNet3Params.get();
        File walletFile = new File("wallet-file");
        if (walletFile.exists()) {
            wallet = Wallet.loadFromFile(walletFile);
        } else {
            wallet = Wallet.createDeterministic(params, Script.ScriptType.P2WPKH);
            wallet.saveToFile(walletFile);
        }
        wallet.addCoinsReceivedEventListener(Executors.newVirtualThreadPerTaskExecutor(),
                (wallet, tx, prevBalance, newBalance) -> handleCoinsReceived(tx));
        context = new Context(params);
        Context.propagate(context);

        this.blockStore = new SPVBlockStore(params, new File("spvblockchain.spvchain"));
        BlockChain chain = new BlockChain(params, wallet, blockStore);
        peerGroup = new PeerGroup(params, chain);
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));

        // Starts the connection to the Bitcoin network
        peerGroup.start();
        peerGroup.startBlockChainDownload(new DownloadProgressTracker());
        System.out.printf("Balance: %s\n", wallet.getBalance().toBtc());
//        peerGroup.downloadBlockChain();
//        System.out.println("done downloading block chain.");
    }

    private void handleCoinsReceived(Transaction tx) {
        for (TransactionOutput output : tx.getOutputs()) {
            System.out.println("==========================================");
            String address = output.getScriptPubKey().getToAddress(params).toString();
            System.out.println(address);
            System.out.println("==========================================");
            AddressRecord addressRecord = addressDao.getByAddress(address);
            Long userId = addressRecord.getLockedBy();
            accountService.increase(userId, "BTC", "DEPOSIT", output.getValue().toBtc());
        }
    }

    @SneakyThrows
    public void transfer(String address, String strAmount) {
        Address toAddress = Address.fromString(TestNet3Params.get(), address);
        Coin amount = Coin.parseCoin(strAmount);

        SendRequest req = SendRequest.to(toAddress, amount);
        req.feePerKb = Coin.parseCoin("0.00000001"); // Transaction fee per KB
        wallet.completeTx(req);
        wallet.commitTx(req.tx);
        peerGroup.broadcastTransaction(req.tx).broadcast().get();
    }

    @SneakyThrows
    public String newAddress() {
        String string = wallet.freshReceiveAddress().toString();
        wallet.saveToFile(new File("wallet-file"));
        return string;
    }

    @Override
    public void destroy() throws Exception {
        Context.propagate(context);
        peerGroup.stop();
        wallet.saveToFile(new File("wallet-file"));
        blockStore.close();
    }
}
