package io.usoamic.webwallet.base

import io.usoamic.web3kt.bignumber.BigNumber
import io.usoamic.web3kt.buffer.Buffer
import io.usoamic.web3kt.buffer.extension.fromHex
import io.usoamic.web3kt.buffer.extension.toHex
import io.usoamic.web3kt.tx.Tx
import io.usoamic.web3kt.tx.block.DefaultBlockParameterName
import io.usoamic.web3kt.tx.gas.DefaultGasProvider
import io.usoamic.web3kt.tx.model.RawTransaction
import io.usoamic.web3kt.tx.model.TransactionReceipt
import io.usoamic.web3kt.util.extension.addHexPrefixIfNotExist
import io.usoamic.web3kt.util.extension.removeHexPrefixIfExist
import io.usoamic.web3kt.wallet.Wallet
import io.usoamic.webwallet.enumcls.TransactionExecutionStatus

abstract class TransactionView(application: Application) : WalletView(application) {
    protected fun getGasPrice(selectedGasPrice: String, callback: (gasPrice: BigNumber) -> Unit) {
        if (selectedGasPrice == "auto") {
            web3.eth.getGasPrice()
                .then(callback::invoke)
                .catch(::onException)
        }
        else {
            callback.invoke(
                when (selectedGasPrice) {
                    "125" -> DefaultGasProvider.GAS_PRICE_125
                    "100" -> DefaultGasProvider.GAS_PRICE_100
                    "75" -> DefaultGasProvider.GAS_PRICE_75
                    "50" -> DefaultGasProvider.GAS_PRICE_50
                    "35" -> DefaultGasProvider.GAS_PRICE_35
                    "20" -> DefaultGasProvider.GAS_PRICE_20
                    else -> throw Exception("Invalid Gas Price")
                }
            )
        }
    }

    protected fun getTransactionCount(callback: (nonce: BigNumber) -> Unit) {
        web3.eth.getTransactionCount(address, DefaultBlockParameterName.LATEST)
            .then(callback::invoke)
            .catch(::onException)
    }

    protected fun sendTransaction(
        transaction: RawTransaction,
        password: String,
        onStatusChanged: (TransactionExecutionStatus) -> Unit,
        onStop: (TransactionReceipt) -> Unit
    ) {
        val privateKey = Wallet.fromV3(account, password).getPrivateKeyAsString().removeHexPrefixIfExist()
        val tx = Tx(transaction)
        tx.sign(Buffer.fromHex(privateKey))
        val signedTransaction = tx.serialize()
        onStatusChanged.invoke(TransactionExecutionStatus.SENDING)

        web3.eth.sendSignedTransaction(signedTransaction.toHex().addHexPrefixIfNotExist())
            .then(onStop::invoke)
            .catch(::onException)
    }

}