package io.usoamic.webwallet.view

import io.usoamic.usoamickotlinjs.other.Config
import io.usoamic.webwallet.enums.Page
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import io.usoamic.webwallet.exception.WalletNotFoundException
import io.usoamic.webwallet.model.Account
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery
import kotlin.browser.localStorage

abstract class WalletView(application: Application) : View(application) {
    private val logoutBtn = jQuery("#logout")
    protected val account
        get() = localStorage.getItem(Config.ACCOUNT_FILENAME)?.let {
            JSON.parse<Account>(it)
        } ?: throw WalletNotFoundException()

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