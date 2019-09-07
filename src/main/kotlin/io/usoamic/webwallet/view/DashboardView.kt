package io.usoamic.webwallet.view

import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.WalletView
import js.externals.datatables.net.JQueryDataTable
import js.externals.datatables.net.extension.dataTable
import js.externals.datatables.net.model.DataTableOption
import js.externals.jquery.JQuery
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

class DashboardView(application: Application) : WalletView(application) {
    override val view = jQuery("#dashboard_view")
    override val navBarItem: JQuery<HTMLElement>? = jQuery("#dashboard_item")
    private val lastTransfersTable = jQuery("#last_transfers").unsafeCast<JQueryDataTable>()

    init {
        prepareLastTransfers()
    }

    private fun prepareLastTransfers() {
        lastTransfersTable.dataTable(DataTableOption.initEmpty())
        getTransactions(10) {
            lastTransfersTable.dataTable(DataTableOption(it))
        }
    }

    companion object {
        fun newInstance(application: Application) {
            return application.open(DashboardView(application))
        }
    }
}
