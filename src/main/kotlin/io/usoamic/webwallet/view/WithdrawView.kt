package io.usoamic.webwallet.view

import io.usoamic.usoamicktjs.other.Contract
import io.usoamic.web3kt.bignumber.BigNumberValue
import io.usoamic.web3kt.bignumber.extension.toBigNumber
import io.usoamic.web3kt.buffer.Buffer
import io.usoamic.web3kt.buffer.extension.fromHex
import io.usoamic.web3kt.buffer.extension.toHex
import io.usoamic.web3kt.core.contract.model.EstimateGasOption
import io.usoamic.web3kt.core.contract.util.Coin
import io.usoamic.web3kt.tx.Tx
import io.usoamic.web3kt.tx.block.DefaultBlockParameterName
import io.usoamic.web3kt.tx.gas.DefaultGasProvider
import io.usoamic.web3kt.tx.model.RawTransaction
import io.usoamic.web3kt.util.EthUnit
import io.usoamic.web3kt.util.extension.addHexPrefixIfNotExist
import io.usoamic.web3kt.util.extension.removeHexPrefixIfExist
import io.usoamic.web3kt.wallet.Wallet
import io.usoamic.webwallet.AppConfig
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.WalletView
import io.usoamic.webwallet.enumcls.Asset
import io.usoamic.webwallet.enumcls.WithdrawLoadingStatus
import io.usoamic.webwallet.exception.ValidateUtilException
import io.usoamic.webwallet.model.TxData
import io.usoamic.webwallet.util.Alert
import io.usoamic.webwallet.util.ValidateUtil
import js.externals.jquery.JQuery
import js.externals.jquery.extension.*
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

class WithdrawView(application: Application) : WalletView(application) {
    override val view = jQuery("#withdraw_view")
    override val navBarItem: JQuery<HTMLElement>? = jQuery("#withdraw_item")

    private val addressInput = jQuery("#withdraw_address_input")
    private val amountInput = jQuery("#withdraw_amount_input")
    private val gasPriceSelect = jQuery("#gas_price_input")
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

    override fun stopLoading() {
        withdrawEthBtn.stopLoading()
        withdrawUsoBtn.stopLoading()
    }

    private fun startLoading(asset: Asset) {
        passwordInput.clearVal()
        when (asset) {
            Asset.COIN -> {
                withdrawUsoBtn.disable()
                withdrawEthBtn.startLoading()
            }
            Asset.TOKEN -> {
                withdrawUsoBtn.startLoading()
                withdrawEthBtn.disable()
            }
        }
    }

    private fun stopLoading(asset: Asset) {
        when (asset) {
            Asset.COIN -> {
                withdrawUsoBtn.enable()
                withdrawEthBtn.stopLoading()
            }
            Asset.TOKEN -> {
                withdrawUsoBtn.stopLoading()
                withdrawEthBtn.enable()
            }
        }
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
        val sGasPrice = gasPriceSelect.content()

        try {
            ValidateUtil.validateAddress(sAddress)
                .validateTransferValue(sAmount)
                .validatePassword(sPassword)

            startLoading(asset)
            web3.eth.getTransactionCount(address, DefaultBlockParameterName.LATEST)
                .then { nonce ->
                    changeLoadingText(asset, WithdrawLoadingStatus.GAS_PRICE)
                    if (sGasPrice == "auto") {
                        web3.eth.getGasPrice()
                            .then { gasPrice ->
                                sendWith(
                                    TxData(
                                        nonce = nonce,
                                        gasPrice = gasPrice,
                                        asset = asset,
                                        address = sAddress,
                                        amount = sAmount,
                                        password = sPassword
                                    )
                                )
                            }
                            .catch(::onException)
                    } else {
                        val gasPrice = when (sGasPrice) {
                            "125" -> DefaultGasProvider.GAS_PRICE_125
                            "100" -> DefaultGasProvider.GAS_PRICE_100
                            "75" -> DefaultGasProvider.GAS_PRICE_75
                            "50" -> DefaultGasProvider.GAS_PRICE_50
                            "35" -> DefaultGasProvider.GAS_PRICE_35
                            "20" -> DefaultGasProvider.GAS_PRICE_20
                            else -> throw Exception("Invalid Gas Price")
                        }
                        sendWith(
                            TxData(
                                nonce = nonce,
                                gasPrice = gasPrice,
                                asset = asset,
                                address = sAddress,
                                amount = sAmount,
                                password = sPassword
                            )
                        )
                    }
                }
                .catch(::onException)

        } catch (t: Throwable) {
            onException(t)
        }
    }

    private fun sendWith(
        data: TxData
    ) {
        changeLoadingText(data.asset, WithdrawLoadingStatus.SIGNING)
        when (data.asset) {
            Asset.COIN -> sendEth(data)
            Asset.TOKEN -> sendUso(data)
        }
    }

    private fun sendEth(
        data: TxData
    ) {
        val value = web3.utils.toWei(data.amount, EthUnit.ETHER).toBigNumber()
        web3.eth.estimateGas(
            EstimateGasOption(
                address,
                data.address,
                value
            )
        )
            .then { gasLimit ->
                val transaction = RawTransaction.createEtherTransaction(
                    from = address,
                    nonce = data.nonce,
                    gasPrice = data.gasPrice,
                    gasLimit = gasLimit,
                    to = data.address,
                    value = value
                )
                sendTransaction(Asset.COIN, transaction, data.password)
            }
            .catch(::onException)
    }

    private fun sendUso(
        data: TxData
    ) {
        val response = methods.transfer(data.address, Coin.fromCoin(data.amount).toStringSat())
        response.estimateGas(
            EstimateGasOption(
                address,
                data.address,
                BigNumberValue.ZERO,
                response.encodeABI()
            )
        )
            .then { gasLimit ->
                val transaction = RawTransaction.createContractTransaction(
                    from = address,
                    nonce = data.nonce,
                    gasPrice = data.gasPrice,
                    gasLimit = gasLimit,
                    to = Contract.forNetwork(AppConfig.NETWORK),
                    data = response.encodeABI()
                )
                sendTransaction(Asset.TOKEN, transaction, data.password)
            }
            .catch(::onException)
    }

    private fun sendTransaction(asset: Asset, transaction: RawTransaction, password: String) {
        val privateKey = Wallet.fromV3(account, password).getPrivateKeyAsString().removeHexPrefixIfExist()
        val tx = Tx(transaction)
        tx.sign(Buffer.fromHex(privateKey))

        val signedTransaction = tx.serialize()
        changeLoadingText(asset, WithdrawLoadingStatus.WITHDRAWING)
        web3.eth.sendSignedTransaction(signedTransaction.toHex().addHexPrefixIfNotExist())
            .then {
                Alert.show(it.transactionHash)
                stopLoading(asset)
            }
            .catch(::onException)
    }

    private fun changeLoadingText(asset: Asset, status: WithdrawLoadingStatus) {
        val currentBtn = when(asset) {
            Asset.COIN -> {
                withdrawEthBtn
            }
            Asset.TOKEN -> {
                withdrawUsoBtn
            }
        }
        val message = when(status) {
            WithdrawLoadingStatus.GAS_PRICE -> "Loading Gas Price"
            WithdrawLoadingStatus.SIGNING -> "Signing"
            WithdrawLoadingStatus.WITHDRAWING -> "Withdrawing"
        }
        currentBtn.changeLoadingText(message)
    }

    override fun onStop() {
        super.onStop()
        listOf(addressInput, amountInput, passwordInput).forEach {
            it.clearContent()
        }
    }

    companion object {
        private var instance: WithdrawView? = null

        fun open(application: Application) {
            if (instance == null) {
                instance = WithdrawView(application)
            }
            return application.open(instance!!)
        }
    }
}