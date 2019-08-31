package io.usoamic.webwallet.view

import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery

class FirstView(application: Application) : View(application) {
    override val view = jQuery("#first_view")
    val addBtn = jQuery("#add_button")
    val createBtn = jQuery("#create_button")

    init {
        setListeners()
        stopLoading()
    }

    fun setListeners() {
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