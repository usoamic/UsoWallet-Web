package io.usoamic.webwallet.view

import io.usoamic.web3kt.core.contract.util.Coin
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

    private val ethBalance = jQuery("#eth_balance")
    private val usoBalance = jQuery("#uso_balance")
    private val ethHeight = jQuery("#eth_height")
    private val usoSupply = jQuery("#uso_supply")
    private var numberOfLastTransfers: Long = 0


    init {
        prepareLastTransfers()
    }

    override fun onRefresh() {
        refreshEthBalance()
        refreshUsoBalance()
        refreshEthHeight()
        refreshUsoSupply()
        refreshLastTransfers()
    }

    private fun refreshEthBalance() {
        getEthBalance {
            ethBalance.text(it)
        }
    }

    private fun refreshUsoBalance() {
        getUsoBalance {
            usoBalance.text(it.toPlainString())
        }
    }

    private fun refreshEthHeight() {
        web3.eth.getBlockNumber()
            .then {
                ethHeight.text(it.toString())
            }
            .catch {
                onException(it)
            }
    }

    private fun refreshUsoSupply() {
        methods.getSupply().call(callOption)
            .then {
                usoSupply.text(Coin.fromSat(it).toMillion())
            }
            .catch {
                onException(it)
            }
    }

    private fun prepareLastTransfers() {
        lastTransfersTable.dataTable(DataTableOption.initLoading())
    }

    private fun refreshLastTransfers() {
        getTransactions(10, numberOfLastTransfers) {
            numberOfLastTransfers = it.size.toLong()
            lastTransfersTable.dataTable(DataTableOption(data = it))
        }
    }

    companion object {
        private var instance: DashboardView? = null

        fun open(application: Application) {
            if(instance == null) {
                instance = DashboardView(application)
            }
            return application.open(instance!!)
        }
    }
}
