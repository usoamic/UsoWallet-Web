package io.usoamic.webwallet.view

import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery

class FirstView(application: Application) : View(application) {
    override val view = jQuery("#first_view")
    private val addBtn = jQuery("#add_button")
    private val createBtn = jQuery("#create_button")

    init {
        application.hideNavigationBar()
        setListeners()
    }

    private fun setListeners() {
        addBtn.onClick {
           AddWalletView.newInstance(application)
        }

        createBtn.onClick {
            CreateWalletView.newInstance(application)
        }
    }

    companion object {
        fun newInstance(application: Application) {
            application.open(FirstView(application))
        }
    }
}