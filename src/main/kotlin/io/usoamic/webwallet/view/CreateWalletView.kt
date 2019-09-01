package io.usoamic.webwallet.view

import io.usoamic.webwallet.App
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import js.externals.jquery.jQuery

class CreateWalletView(application: Application) : View(application) {
    override val view = jQuery("#create_wallet_view")
    private val newPrivateKeyElement = jQuery("#new_pk_element")

    companion object {
        fun newInstance(application: Application) {
            return application.open(CreateWalletView(application))
        }
    }
}