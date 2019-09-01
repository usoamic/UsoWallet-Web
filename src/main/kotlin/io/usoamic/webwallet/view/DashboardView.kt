package io.usoamic.webwallet.view

import io.usoamic.usoamickotlinjs.other.Config
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import io.usoamic.webwallet.exception.WalletNotFoundException
import io.usoamic.webwallet.model.Account
import js.externals.jquery.jQuery
import kotlin.browser.localStorage

class DashboardView(application: Application) : View(application) {

    override val view = jQuery("#dashboard_view")
    private val account get() =  localStorage.getItem(Config.ACCOUNT_FILENAME)?.let {
            JSON.parse<Account>(it)
    } ?: throw WalletNotFoundException()

    init {
        application.showNavigationBar()
//        println("address: ${account.address}")

    }

    companion object {
        fun newInstance(application: Application) {
            return application.open(DashboardView(application))
        }
    }
}