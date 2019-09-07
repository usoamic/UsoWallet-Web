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

    init {
        prepareLastTransfers()
        prepareEthBalance()
        prepareUsoBalance()
        prepareEthHeight()
        prepareUsoSupply()
    }

    private fun prepareEthBalance() {
        getEthBalance {
            ethBalance.text(it)
        }
    }

    private fun prepareUsoBalance() {
        getUsoBalance {
            usoBalance.text(it.toPlainString())
        }
    }

    private fun prepareEthHeight() {
        web3.eth.getBlockNumber()
            .then {
                ethHeight.text(it.toString())
            }
            .catch {
                onError(it)
            }
    }

    private fun prepareUsoSupply() {
        methods.getSupply().call(callOption)
            .then {
                usoSupply.text(Coin.fromSat(it).toMillion())
            }
            .catch {
                onError(it)
            }
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
