package io.usoamic.webwallet

import js.externals.jquery.JQuery
import js.externals.jquery.JQueryCallback
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
 * Common Authorization
 */
val toFirstBtn = jQuery(".to_first")

/*
 * FirstView
 */
val addBtn = jQuery("#add_button")
val createBtn = jQuery("#create_button")

/*
 * AddWalletView
 */
val saveBtn = jQuery("#save_button")
val addPrivateKeyInput = jQuery("#add_pk_input")
val addPasswordInput = jQuery("#add_password_input")
val addConfirmInput = jQuery("#add_confirm_password_input")
val newPrivateKeyElement = jQuery("#new_pk_element")


/*
 * CreateWalletView
 */

/*
 * DashboardView
 */
fun main() {
    loader.delay(1500).fadeOut()
    setListeners()
}

private fun setListeners() {
    addBtn.onClick {
        addWalletView.showIt()
    }

    createBtn.onClick {
        createWalletView.showIt()
    }

    toFirstBtn.onClick {
        firstView.showIt()
    }

    saveBtn.onClick {

    }
}

fun JQuery<HTMLElement>.onClick(callback: () -> Unit) {
    this.on("click", callback)
}

fun JQuery<HTMLElement>.showIt() {
    currentView.hide()
    this.show()
    currentView = this
}