package org.example.yourownex.service;

import lombok.*;
import org.bitcoinj.core.*;
import org.bitcoinj.params.*;
import org.bitcoinj.script.*;
import org.bitcoinj.store.*;
import org.bitcoinj.wallet.*;
import org.springframework.stereotype.*;

import java.io.*;

@Service
public class BitcoinService {

    @SneakyThrows
    public static void main(String[] args) {
        NetworkParameters params = TestNet3Params.get();

        Wallet wallet = Wallet.createDeterministic(params, Script.ScriptType.P2PKH);
        File walletFile = new File("wallet-file");
        wallet.saveToFile(walletFile);
        Context.propagate(new Context(params));

        BlockStore blockStore = new SPVBlockStore(params, new File("spvblockchain.spvchain"));
        BlockChain chain = new BlockChain(params, wallet, blockStore);
        PeerGroup peerGroup = new PeerGroup(params, chain);
        peerGroup.addWallet(wallet);

        // Starts the connection to the Bitcoin network
        peerGroup.start();
        peerGroup.downloadBlockChain();

        Address toAddress = Address.fromString(params, "YOUR_RECEIVING_ADDRESS");
        Coin amount = Coin.parseCoin("0.0000001");

        SendRequest req = SendRequest.to(toAddress, amount);
        req.feePerKb = Coin.parseCoin("0.00000001"); // Transaction fee per KB
        try {
            wallet.completeTx(req);
            wallet.commitTx(req.tx);
            peerGroup.broadcastTransaction(req.tx).broadcast().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
