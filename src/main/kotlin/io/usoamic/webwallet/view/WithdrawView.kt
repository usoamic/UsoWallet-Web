package io.usoamic.webwallet.view

import io.usoamic.web3kt.bignumber.BigNumber
import io.usoamic.web3kt.bignumber.extension.toBigNumber
import io.usoamic.web3kt.core.contract.model.EstimateGasOption
import io.usoamic.web3kt.core.contract.util.Coin
import io.usoamic.web3kt.tx.model.RawTransaction
import io.usoamic.web3kt.util.EthUnit
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.TransactionView
import io.usoamic.webwallet.enumcls.Asset
import io.usoamic.webwallet.enumcls.TransactionExecutionStatus
import io.usoamic.webwallet.exception.ValidateUtilException
import io.usoamic.webwallet.model.WithdrawTxData
import io.usoamic.webwallet.util.Alert
import io.usoamic.webwallet.util.ValidateUtil
import js.externals.jquery.JQuery
import js.externals.jquery.extension.*
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

class WithdrawView(application: Application) : TransactionView(application) {
    override val view = jQuery("#withdraw_view")
    override val navBarItem: JQuery<HTMLElement>? = jQuery("#withdraw_item")

    private val addressInput = jQuery("#withdraw_address_input")
    private val amountInput = jQuery("#withdraw_amount_input")
    private val gasPriceSelect = jQuery("#withdraw_gas_price_input")
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

            getTransactionCount { nonce ->
                changeLoadingText(asset, TransactionExecutionStatus.GAS_PRICE)
                getGasPrice(sGasPrice) { gasPrice ->
                    sendWith(
                        data = WithdrawTxData(
                            nonce = nonce,
                            asset = asset,
                            address = sAddress,
                            amount = sAmount,
                            password = sPassword
                        ),
                        gasPrice = gasPrice
                    )
                }
            }
        } catch (t: Throwable) {
            onException(t)
        }
    }

    private fun sendWith(
        data: WithdrawTxData,
        gasPrice: BigNumber
    ) {
        changeLoadingText(data.asset, TransactionExecutionStatus.SIGNING)

        when (data.asset) {
            Asset.COIN -> sendEth(
                data = data,
                gasPrice = gasPrice
            )
            Asset.TOKEN -> sendUso(
                data = data,
                gasPrice = gasPrice
            )
        }
    }

    private fun sendEth(
        data: WithdrawTxData,
        gasPrice: BigNumber
    ) {
        val value = web3.utils.toWei(data.amount, EthUnit.ETHER).toBigNumber()

        val to = data.address

        web3.eth.estimateGas(
            EstimateGasOption.Ether(
                from = address,
                to = to,
                value = value
            )
        )
            .then { gasLimit ->
                val transaction = RawTransaction.createEtherTransaction(
                    from = address,
                    nonce = data.nonce,
                    gasPrice = gasPrice,
                    gasLimit = gasLimit,
                    to = to,
                    value = value
                )
                sendTransaction(Asset.COIN, transaction, data.password)
            }
            .catch(::onException)
    }

    private fun sendUso(
        data: WithdrawTxData,
        gasPrice: BigNumber
    ) {
        val response = methods.transfer(data.address, Coin.fromCoin(data.amount).toStringSat())

        response.estimateGas(
            EstimateGasOption.Contract(
                from = address,
                to = CONTRACT_ADDRESS,
                data = response.encodeABI()
            )
        )
            .then { gasLimit ->
                val transaction = RawTransaction.createContractTransaction(
                    from = address,
                    nonce = data.nonce,
                    gasPrice = gasPrice,
                    gasLimit = gasLimit,
                    to = CONTRACT_ADDRESS,
                    data = response.encodeABI()
                )
                sendTransaction(Asset.TOKEN, transaction, data.password)
            }
            .catch(::onException)
    }

    private fun sendTransaction(asset: Asset, transaction: RawTransaction, password: String) {
        sendTransaction(
            transaction = transaction,
            password = password,
            onStatusChanged = { status ->
                changeLoadingText(asset, status)
            },
            onStop = {
                Alert.show(it.transactionHash)
                stopLoading(asset)
            }
        )
    }

    private fun changeLoadingText(asset: Asset, status: TransactionExecutionStatus) {
        val currentBtn = when (asset) {
            Asset.COIN -> {
                withdrawEthBtn
            }
            Asset.TOKEN -> {
                withdrawUsoBtn
            }
        }
        val message = when (status) {
            TransactionExecutionStatus.GAS_PRICE -> "Loading Gas Price"
            TransactionExecutionStatus.SIGNING -> "Signing"
            TransactionExecutionStatus.SENDING -> "Withdrawing"
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