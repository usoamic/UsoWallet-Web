package io.usoamic.webwallet.base

import io.usoamic.usoamicktjs.core.Usoamic
import io.usoamic.usoamicktjs.core.extension.getTransactionsByAddress
import io.usoamic.usoamicktjs.model.Transfer
import io.usoamic.usoamicktjs.other.Contract
import io.usoamic.usoamicktjs.other.Node
import io.usoamic.web3kt.bignumber.BigNumber
import io.usoamic.web3kt.bignumber.extension.toBigNumber
import io.usoamic.web3kt.buffer.Buffer
import io.usoamic.web3kt.buffer.extension.fromHex
import io.usoamic.web3kt.buffer.extension.toHex
import io.usoamic.web3kt.core.Web3
import io.usoamic.web3kt.core.contract.model.CallOption
import io.usoamic.web3kt.core.contract.util.Coin
import io.usoamic.web3kt.core.extension.newContract
import io.usoamic.web3kt.tx.Tx
import io.usoamic.web3kt.tx.block.DefaultBlockParameterName
import io.usoamic.web3kt.tx.gas.DefaultGasProvider
import io.usoamic.web3kt.tx.model.RawTransaction
import io.usoamic.web3kt.tx.model.TransactionReceipt
import io.usoamic.web3kt.util.EthUnit
import io.usoamic.web3kt.util.extension.addHexPrefixIfNotExist
import io.usoamic.web3kt.util.extension.removeHexPrefixIfExist
import io.usoamic.web3kt.wallet.Wallet
import io.usoamic.webwallet.AppConfig
import io.usoamic.webwallet.enumcls.TransactionExecutionStatus
import io.usoamic.webwallet.enumcls.TransferType
import io.usoamic.webwallet.exception.WalletNotFoundException
import io.usoamic.webwallet.model.Account
import io.usoamic.webwallet.other.Timestamp
import io.usoamic.webwallet.util.TxUtils
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery
import kotlinx.browser.localStorage
import kotlinx.browser.window
import io.usoamic.usoamicktjs.model.Account as AccountData

abstract class WalletView(application: Application) : View(application) {
    protected val web3 = Web3(Node.by(AppConfig.NETWORK, AppConfig.NODE_PROVIDER))

    private val contract = web3.newContract<Usoamic>(AppConfig.ABI, CONTRACT_ADDRESS)
    protected val methods = contract.methods

    private val logoutBtn = jQuery("#logout")

    private val _account
        get() = localStorage.getItem(AccountData.ACCOUNT)?.let {
            JSON.parse<Account>(it)
        } ?: throw WalletNotFoundException()

    protected val account get() = JSON.stringify(_account)
    protected val address = _account.address.addHexPrefixIfNotExist()
    protected val callOption = CallOption(address)

    init {

        application.showNavigationBar()
        setListeners()
    }

    private fun setListeners() {
        logoutBtn.onClick {
            application.runLoader()
            localStorage.removeItem(AccountData.ACCOUNT)
            window.location.href = "index.html"
        }
    }

    protected fun getEthBalance(callback: (String) -> Unit) {
        web3.eth.getBalance(address, DefaultBlockParameterName.LATEST)
            .then {
                callback(web3.utils.fromWei(it, EthUnit.ETHER).toBigNumber().toPrecision(5).toString())
            }
            .catch(::onException)
    }

    protected fun getUsoBalance(callback: (Coin) -> Unit) {
        methods.balanceOf(address).call(callOption)
            .then {
                callback(Coin.fromSat(it))
            }
            .catch(::onException)
    }

    protected fun getTransactions(maxTx: Long?, loadedLastId: Long, callback: (list: List<List<Any>>, lastTxId: Long) -> Unit) {
        maxTx?.let { mId ->
            methods.getTransactionsByAddress(address, mId, loadedLastId) { list: MutableList<Transfer>, lastTxId: Long, throwable: Throwable?, hasUpdate: Boolean ->
                if(!hasUpdate) {
                    return@getTransactionsByAddress
                }
                if(throwable != null) {
                    onException(throwable)
                    return@getTransactionsByAddress
                }
                val txList = mutableListOf<List<Any>>()

                list.forEachIndexed { index, tx ->
                    val txType = TxUtils.getTxType(address, tx.from, tx.to)
                    txList.add(
                        listOf(
                            index + 1,
                            txType.toPlainString(),
                            when (txType) {
                                TransferType.DEPOSIT -> tx.from
                                TransferType.WITHDRAWAL -> tx.to
                                TransferType.INTERNAL -> address
                                TransferType.UNKNOWN -> "N/A"
                            },
                            Coin.fromSat(tx.value).toPlainString(),
                            Timestamp.fromBigNumber(tx.timestamp).toLocaleString()
                        )
                    )
                }
                callback(txList, lastTxId)
            }
        }
    }

    companion object {
        val CONTRACT_ADDRESS = Contract.forNetwork(AppConfig.NETWORK)
    }
}