package io.usoamic.webwallet.view

import io.usoamic.usoamickotlinjs.core.extension.getTransactionsByAddress
import io.usoamic.web3kt.core.contract.util.Coin
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.WalletView
import io.usoamic.webwallet.enums.Transfer
import io.usoamic.webwallet.other.Timestamp
import io.usoamic.webwallet.util.TxUtils
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
        prepareTable()
    }

    private fun prepareTable() {
        prepareLastTransfers()
    }

    private fun prepareLastTransfers() {
        lastTransfersTable.dataTable(DataTableOption.initEmpty())
        methods.getTransactionsByAddress(address, 10) {
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
            val options = DataTableOption(list)
            lastTransfersTable.dataTable(options)
        }
    }

    companion object {
        fun newInstance(application: Application) {
            return application.open(DashboardView(application))
        }
    }
}
