package io.usoamic.webwallet.view

import io.usoamic.webwallet.base.Application
import js.externals.jquery.JQuery
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

class WithdrawView(application: Application) : WalletView(application) {
    override val view = jQuery("#withdraw_view")
    override val navBarItem: JQuery<HTMLElement>? = jQuery("#withdraw_item")

    init {

    }

    companion object {
        fun newInstance(application: Application) {
            return application.open(WithdrawView(application))
        }
    }
}