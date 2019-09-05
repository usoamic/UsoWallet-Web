package io.usoamic.webwallet.view

import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.WalletView
import js.externals.datatables.net.JQueryDataTable
import js.externals.datatables.net.extension.dataTable
import js.externals.jquery.JQuery
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

class HistoryView(application: Application) : WalletView(application) {
    override val view = jQuery("#history_view")
    override val navBarItem: JQuery<HTMLElement>? = jQuery("#history_item")
    private val historyTable = jQuery("#history").unsafeCast<JQueryDataTable>()

    init {
        prepareTable()
    }

    private fun prepareTable() {
        (historyTable as JQueryDataTable).dataTable()
    }

    companion object {
        fun newInstance(application: Application) {
            return application.open(HistoryView(application))
        }
    }
}