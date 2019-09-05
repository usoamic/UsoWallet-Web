package js.externals.datatables.net.extension

import js.externals.datatables.net.model.DataTableOption
import js.externals.datatables.net.JQueryDataTable

fun JQueryDataTable.dataTable(options: DataTableOption) {
    dataTable(JSON.parse(JSON.stringify(options)))
}