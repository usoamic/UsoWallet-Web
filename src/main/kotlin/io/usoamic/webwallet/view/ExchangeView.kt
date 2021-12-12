package io.usoamic.webwallet.view

import io.usoamic.web3kt.bignumber.BigNumber
import io.usoamic.web3kt.core.contract.model.EstimateGasOption
import io.usoamic.web3kt.core.contract.util.Coin
import io.usoamic.web3kt.tx.model.RawTransaction
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.TransactionView
import io.usoamic.webwallet.enumcls.TransactionExecutionStatus
import io.usoamic.webwallet.exception.ValidateUtilException
import io.usoamic.webwallet.model.ExchangeTxData
import io.usoamic.webwallet.util.Alert
import io.usoamic.webwallet.util.ConvertUtil.Companion.convertWeiToEth
import io.usoamic.webwallet.util.ValidateUtil
import js.externals.jquery.JQuery
import js.externals.jquery.extension.*
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

class ExchangeView(application: Application) : TransactionView(application) {
    override val view = jQuery("#exchange_view")
    override val navBarItem: JQuery<HTMLElement>? = jQuery("#exchange_item")

    private val amountInput = jQuery("#exchange_amount_input")
    private val priceInput = jQuery("#exchange_price_input")
    private val gasPriceSelect = jQuery("#exchange_gas_price_input")
    private val passwordInput = jQuery("#exchange_password_input")
    private val exchangeDisabledAlert = jQuery("#exchange_disabled_alert")
    private val exchangeForm = jQuery("#exchange_form")

    private val exchangeBtn = jQuery("#exchange_button")

    init {
        refreshPrice()
        refreshSwappable()
        setListeners()
    }

    private fun refreshPrice() {
        methods.getSwapRate().call(callOption)
            .then(::setPrice)
            .catch(::onException)
    }

    private fun refreshSwappable() {
        methods.getSwappable().call(callOption)
            .then(::setSwappable)
            .catch(::onException)
    }

    private fun setSwappable(isSwappable: Boolean) {
        if (isSwappable) {
            exchangeDisabledAlert.hide()
            exchangeForm.show()
        } else {
            exchangeDisabledAlert.show()
            exchangeBtn.hide()
        }
    }

    private fun setPrice(price: String) {
        priceInput.`val`(
            convertWeiToEth(price).multipliedBy(Coin.SAT_PER_COIN).toString()
        )
    }

    private fun setListeners() {
        exchangeBtn.onClick {
            exchange()
        }
    }

    override fun startLoading() {
        passwordInput.clearVal()
        exchangeBtn.startLoading()
    }

    override fun stopLoading() {
        exchangeBtn.stopLoading()
    }

    override fun onException(t: Throwable) {
        super.onException(t)
        passwordInput.clearVal()
        if (t !is ValidateUtilException) {
            stopLoading()
        }
    }

    private fun exchange() {
        val sAmount = amountInput.content()
        val sPassword = passwordInput.content()
        val sGasPrice = gasPriceSelect.content()

        try {
            ValidateUtil
                .validateTransferValue(sAmount)
                .validatePassword(sPassword)

            startLoading()

            getTransactionCount { nonce ->
                changeLoadingText(TransactionExecutionStatus.GAS_PRICE)
                getGasPrice(sGasPrice) { gasPrice ->
                    exchangeWith(
                        data = ExchangeTxData(
                            nonce = nonce,
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

    private fun exchangeWith(
        data: ExchangeTxData,
        gasPrice: BigNumber
    ) {
        changeLoadingText(TransactionExecutionStatus.SIGNING)

        val response = methods.burnSwap(Coin.fromCoin(data.amount).toStringSat())

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
                sendTransaction(transaction, data.password)
            }
            .catch(::onException)
    }

    private fun sendTransaction(transaction: RawTransaction, password: String) {
        sendTransaction(
            transaction = transaction,
            password = password,
            onStatusChanged = { status ->
                changeLoadingText(status)
            },
            onStop = {
                Alert.show(it.transactionHash)
                stopLoading()
            }
        )
    }

    private fun changeLoadingText(status: TransactionExecutionStatus) {
        val message = when (status) {
            TransactionExecutionStatus.GAS_PRICE -> "Loading Gas Price"
            TransactionExecutionStatus.SIGNING -> "Signing"
            TransactionExecutionStatus.SENDING -> "Exchanging"
        }
        exchangeBtn.changeLoadingText(message)
    }

    override fun onStop() {
        super.onStop()
        listOf(amountInput, passwordInput).forEach {
            it.clearContent()
        }
    }

    companion object {
        private var instance: ExchangeView? = null

        fun open(application: Application) {
            if (instance == null) {
                instance = ExchangeView(application)
            }
            return application.open(instance!!)
        }
    }
}