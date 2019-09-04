package io.usoamic.webwallet.view

import io.usoamic.web3kt.kt2js.require
import io.usoamic.webwallet.base.Application
import js.externals.jquery.JQuery
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

class DashboardView(application: Application) : WalletView(application) {
    override val view = jQuery("#dashboard_view")
    override val navBarItem: JQuery<HTMLElement>? = jQuery("#dashboard_item")
    private val lastTransfersTable = jQuery("#last_transfers")

    init {
        prepareTable()
    }

    private fun prepareTable() {
        lastTransfersTable.dataTable()
    }

    companion object {
        fun newInstance(application: Application) {
            return application.open(DashboardView(application))
        }
    }
}