package io.usoamic.webwallet

import js.externals.jquery.JQuery
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

val loader = jQuery(".loader")
val firstView = jQuery("#first_view")
val addWalletView = jQuery("#add_wallet_view")
val createWalletView = jQuery("#create_wallet_view")
val dashboardView = jQuery("#dashboard_view")

/*
 * Common
 */
var currentView = firstView

/*
 * FirstPage
 */
val addButton = jQuery("#add_button")
val createButton = jQuery("#create_button")

/*
 * AddWalletPage
 */


/*
 * CreateWalletPage
 */

/*
 * DashboardPage
 */
fun main() {
    loader.delay(1500).fadeOut()
    setListeners()
}

private fun setListeners() {
    addButton.on("click") {
        println("onClick: addButton")
        addWalletView.showIt()
    }

    createButton.on("click") {
        createWalletView.showIt()
    }
}

fun JQuery<HTMLElement>.showIt() {
    currentView.hide()
    this.show()
    currentView = this
}