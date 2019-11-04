package io.usoamic.webwallet.view

import io.usoamic.webwallet.AppConfig
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.WalletView
import js.externals.datatables.net.JQueryDataTable
import js.externals.datatables.net.extension.dataTable
import js.externals.datatables.net.model.DataTableOption
import js.externals.jquery.JQuery
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

class HistoryView(application: Application) : WalletView(application) {
    override val view = jQuery("#history_view")
    override val navBarItem: JQuery<HTMLElement>? = jQuery("#history_item")
    private val historyTable = jQuery("#history").unsafeCast<JQueryDataTable>()
    private var lastTxId: Long = 0

    init {
        prepareHistory()
    }

    private fun prepareHistory() {
        historyTable.dataTable(DataTableOption.initLoading())
    }

    override fun onRefresh() {
        getTransactions(AppConfig.NUMBER_OF_HISTORY_TRANSFERS, lastTxId) { list: List<List<Any>>, ltId: Long ->
            lastTxId = ltId
            historyTable.dataTable(DataTableOption(list))
        }
    }

    companion object {
        private var instance: HistoryView? = null

        fun open(application: Application) {
            if(instance == null) {
                instance = HistoryView(application)
            }
            return application.open(instance!!)
        }
    }
}