package io.usoamic.webwallet.base

import io.usoamic.usoamickotlinjs.core.Usoamic
import io.usoamic.usoamickotlinjs.core.extension.getTransactionsByAddress
import io.usoamic.usoamickotlinjs.other.Config
import io.usoamic.usoamickotlinjs.other.Config.Companion.CONTRACT_ABI
import io.usoamic.usoamickotlinjs.other.Config.Companion.NODE
import io.usoamic.web3kt.core.Web3
import io.usoamic.web3kt.core.contract.model.CallOption
import io.usoamic.web3kt.core.contract.util.Coin
import io.usoamic.web3kt.core.extension.newContract
import io.usoamic.web3kt.tx.block.DefaultBlockParameterName
import io.usoamic.web3kt.util.EthUnit
import io.usoamic.web3kt.util.extension.addPrefixIfNotExist
import io.usoamic.webwallet.enums.Page
import io.usoamic.webwallet.enums.Transfer
import io.usoamic.webwallet.exception.WalletNotFoundException
import io.usoamic.webwallet.model.Account
import io.usoamic.webwallet.other.Timestamp
import io.usoamic.webwallet.util.TxUtils
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery
import kotlin.browser.localStorage

abstract class WalletView(application: Application) : View(application) {
    protected val web3 = Web3(NODE)
    private val contract = web3.newContract<Usoamic>(CONTRACT_ABI, "0x42210806DCA8E0C7A5Cff83192852eB0db4ce764")
    protected val methods = contract.methods

    private val logoutBtn = jQuery("#logout")

    private val _account
        get() = localStorage.getItem(Config.ACCOUNT_FILENAME)?.let {
            JSON.parse<Account>(it)
        } ?: throw WalletNotFoundException()

    protected val account get() = JSON.stringify(_account)
    protected val address = _account.address.addPrefixIfNotExist()
    protected val callOption = CallOption(address)

    init {
        application.showNavigationBar()
        setListeners()
    }

    private fun setListeners() {
        logoutBtn.onClick {
            localStorage.removeItem(Config.ACCOUNT_FILENAME)
            application.openPage(Page.FIRST)
        }
    }

    protected fun getTransactions(callback: (List<List<Any>>) -> Unit) {
        getTransactions(null, callback)
    }

    protected fun getEthBalance(callback: (String) -> Unit) {
        web3.eth.getBalance(address, DefaultBlockParameterName.LATEST)
            .then {
                callback(web3.utils.fromWei(it, EthUnit.ETHER))
            }
            .catch {
                onException(it)
            }
    }

    protected fun getUsoBalance(callback: (Coin) -> Unit) {
        methods.balanceOf(address).call(callOption)
            .then {
                callback(Coin.fromSat(it))
            }
            .catch {
                onException(it)
            }
    }

    protected fun getTransactions(lastId: Long?, callback: (List<List<Any>>) -> Unit) {
        methods.getTransactionsByAddress(address, lastId) {
            val list = mutableListOf<List<Any>>()
            it.forEach { tx ->
                val txType = TxUtils.getTxType(address, tx.from)
                list.add(
                    listOf(
                        txType.toPlainString(),
                        when (txType) {
                            Transfer.DEPOSIT -> tx.from
                            Transfer.WITHDRAWAL -> tx.to
                        },
                        Coin.fromSat(tx.value).toPlainString(),
                        Timestamp.fromBigNumber(tx.timestamp).toLocaleString()
                    )
                )
            }
            callback(list)
        }
    }
}