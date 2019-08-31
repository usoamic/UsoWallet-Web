package io.usoamic.webwallet.view

import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery

class AddWalletView(application: Application) : View(application) {
    override val view = jQuery("#add_wallet_view")
    val privateKeyInput = jQuery("#add_pk_input")
    val passwordInput = jQuery("#add_password_input")
    val confirmInput = jQuery("#add_confirm_password_input")
    val saveBtn = jQuery("#save_button")

    init {

    }

    fun setListeners() {
        saveBtn.onClick {

        }

    }

    companion object {
        fun newInstance(application: Application) {
            application.open(AddWalletView(application))
        }
    }
}