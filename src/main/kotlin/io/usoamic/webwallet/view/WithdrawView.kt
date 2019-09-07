package io.usoamic.webwallet.view

import io.usoamic.usoamickotlinjs.other.Config
import io.usoamic.web3kt.bignumber.BigNumber
import io.usoamic.web3kt.bignumber.BigNumberValue
import io.usoamic.web3kt.buffer.Buffer
import io.usoamic.web3kt.buffer.extension.fromHex
import io.usoamic.web3kt.buffer.extension.toHex
import io.usoamic.web3kt.core.contract.model.EstimateGasOption
import io.usoamic.web3kt.core.contract.util.Coin
import io.usoamic.web3kt.tx.Transaction
import io.usoamic.web3kt.tx.block.DefaultBlockParameterName
import io.usoamic.web3kt.tx.model.RawTransaction
import io.usoamic.web3kt.util.extension.addHexPrefix
import io.usoamic.web3kt.util.extension.removeHexPrefixIfExist
import io.usoamic.web3kt.wallet.Wallet
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.WalletView
import io.usoamic.webwallet.enums.Asset
import io.usoamic.webwallet.exception.ValidateUtilException
import io.usoamic.webwallet.util.Alert
import io.usoamic.webwallet.util.ValidateUtil
import js.externals.jquery.JQuery
import js.externals.jquery.extension.clearVal
import js.externals.jquery.extension.onClick
import js.externals.jquery.extension.startLoading
import js.externals.jquery.extension.stopLoading
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

class WithdrawView(application: Application) : WalletView(application) {
    override val view = jQuery("#withdraw_view")
    override val navBarItem: JQuery<HTMLElement>? = jQuery("#withdraw_item")

    private val addressInput = jQuery("#withdraw_address_input")
    private val amountInput = jQuery("#withdraw_amount_input")
    private val passwordInput = jQuery("#withdraw_password_input")
    private val withdrawEthBtn = jQuery("#withdraw_eth_button")
    private val withdrawUsoBtn = jQuery("#withdraw_uso_button")

    init {
        setListeners()
    }

    private fun setListeners() {
        withdrawEthBtn.onClick {
            withdraw(Asset.COIN)
        }

        withdrawUsoBtn.onClick {
            withdraw(Asset.TOKEN)
        }
    }

    override fun startLoading() {
        withdrawUsoBtn.startLoading()
        withdrawEthBtn.startLoading()
    }

    override fun stopLoading() {
        withdrawUsoBtn.stopLoading()
        withdrawEthBtn.stopLoading()
    }

    override fun onException(t: Throwable) {
        super.onException(t)
        passwordInput.clearVal()
        if (t !is ValidateUtilException) {
            stopLoading()
        }
    }

    private fun withdraw(asset: Asset) {
        val sAddress = addressInput.content()
        val sAmount = amountInput.content()
        val sPassword = passwordInput.content()

        try {
            ValidateUtil.validateAddress(sAddress)
                .validateTransferValue(sAmount)
                .validatePassword(sPassword)

            startLoading()

            when (asset) {
                Asset.COIN -> sendEth(sAddress, sAmount, sPassword)
                Asset.TOKEN -> sendUso(sAddress, sAmount, sPassword)
            }
        } catch (t: Throwable) {
            onException(t)
        }
    }

    private fun sendEth(sAddress: String, sAmount: String, sPassword: String) {
        web3.eth.getTransactionCount(address, DefaultBlockParameterName.LATEST)
            .then { nonce ->
                web3.eth.estimateGas(
                    EstimateGasOption(
                        address,
                        sAddress,
                        BigNumber(sAmount)
                    )
                ).then { gasLimit ->
                    val transaction = RawTransaction.createEtherTransaction(
                        address,
                        nonce,
                        BigNumber(gasLimit),
                        sAddress,
                        Coin.fromCoin(sAmount).toBigNumber()
                    )
                    sendTransaction(transaction, sPassword)
                }
            }
    }

    private fun sendUso(sAddress: String, sAmount: String, sPassword: String) {
        web3.eth.getTransactionCount(address, DefaultBlockParameterName.LATEST)
            .then { nonce ->
                val response = methods.transfer(sAddress, Coin.fromCoin(sAmount).toStringSat())
                response.estimateGas(
                    EstimateGasOption(
                        address,
                        sAddress,
                        BigNumberValue.ZERO,
                        response.encodeABI()
                    )
                )
                    .then { gasLimit ->
                        val transaction = RawTransaction.createContractTransaction(
                            address,
                            nonce,
                            BigNumber(gasLimit),
                            Config.CONTRACT_ADDRESS,
                            response.encodeABI()
                        )
                        sendTransaction(transaction, sPassword)
                    }
            }

    }

    private fun sendTransaction(transaction: RawTransaction, password: String) {
        val privateKey = Wallet.fromV3(account, password).getPrivateKeyAsString().removeHexPrefixIfExist()

        val tx = Transaction(transaction)
        tx.sign(Buffer.fromHex(privateKey))

        val signedTransaction = tx.serialize()
        web3.eth.sendSignedTransaction(signedTransaction.toHex().addHexPrefix()).then {
            Alert.show(it.transactionHash)
            stopLoading()
        }
    }

    companion object {
        private var instance: WithdrawView? = null

        fun open(application: Application) {
            if(instance == null) {
                instance = WithdrawView(application)
            }
            return application.open(instance!!)
        }
    }
}