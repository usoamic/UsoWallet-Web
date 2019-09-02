package io.usoamic.webwallet.view

import io.usoamic.webwallet.base.Application
import js.externals.jquery.JQuery
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

class HistoryView(application: Application) : WalletView(application) {
    override val view = jQuery("#history_view")
    override val navBarItem: JQuery<HTMLElement>? = jQuery("#history_item")

    init {

    }

    companion object {
        fun newInstance(application: Application) {
            return application.open(HistoryView(application))
        }
    }
}