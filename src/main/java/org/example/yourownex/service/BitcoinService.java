package org.example.yourownex.service;

import lombok.*;
import org.bitcoinj.core.*;
import org.bitcoinj.params.*;
import org.bitcoinj.script.*;
import org.bitcoinj.store.*;
import org.bitcoinj.wallet.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.concurrent.*;

@Service
public class BitcoinService {

    private final Wallet wallet;
    private final PeerGroup peerGroup;

    @SneakyThrows
    public BitcoinService() {
        NetworkParameters params = TestNet3Params.get();
        File walletFile = new File("wallet-file");
        if (walletFile.exists()) {
            wallet = Wallet.loadFromFile(walletFile);
        } else {
            wallet = Wallet.createDeterministic(params, Script.ScriptType.P2WPKH);
            wallet.saveToFile(walletFile);
        }
        wallet.addCoinsReceivedEventListener(Executors.newVirtualThreadPerTaskExecutor(),
                (wallet, tx, prevBalance, newBalance) -> {
                    for (TransactionOutput input : tx.getOutputs()) {

                        Script scriptSig = input.getScriptPubKey();
                    }
                });
        Context.propagate(new Context(params));

        BlockStore blockStore = new SPVBlockStore(params, new File("spvblockchain.spvchain"));
        BlockChain chain = new BlockChain(params, wallet, blockStore);
        peerGroup = new PeerGroup(params, chain);

        // Starts the connection to the Bitcoin network
        peerGroup.start();
        peerGroup.downloadBlockChain();

        Thread.sleep(Long.MAX_VALUE);
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
}
