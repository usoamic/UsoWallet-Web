package js.externals.datatables.net

import js.externals.jquery.JQuery
import org.w3c.dom.HTMLElement

external interface JQueryDataTable : js.externals.jquery.JQuery<HTMLElement> {
    fun dataTable(json: String)
    fun dataTable()
}