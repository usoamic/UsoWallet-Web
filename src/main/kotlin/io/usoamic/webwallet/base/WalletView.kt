package io.usoamic.webwallet.base

import io.usoamic.usoamickotlinjs.core.TransactionExplorer
import io.usoamic.usoamickotlinjs.core.Usoamic
import io.usoamic.usoamickotlinjs.model.Transaction
import io.usoamic.usoamickotlinjs.other.Config
import io.usoamic.usoamickotlinjs.other.Config.Companion.CONTRACT_ABI
import io.usoamic.usoamickotlinjs.other.Config.Companion.NODE
import io.usoamic.web3kt.core.Web3
import io.usoamic.web3kt.core.contract.model.CallOption
import io.usoamic.web3kt.core.extension.newContract
import io.usoamic.webwallet.enums.Page
import io.usoamic.webwallet.exception.WalletNotFoundException
import io.usoamic.webwallet.legacy.CONTRACT
import io.usoamic.webwallet.model.Account
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery
import kotlin.browser.localStorage

abstract class WalletView(application: Application) : View(application) {
    protected val web3 = Web3(NODE)
    protected val contract = web3.newContract<Usoamic>(CONTRACT_ABI, CONTRACT)
    protected val methods = contract.methods

    private val logoutBtn = jQuery("#logout")

    protected val account
        get() = localStorage.getItem(Config.ACCOUNT_FILENAME)?.let {
            JSON.parse<Account>(it)
        } ?: throw WalletNotFoundException()

    protected val address = account.address

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
}