package io.usoamic.webwallet.view

import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery
import io.usoamic.web3kt.wallet.Wallet
import io.usoamic.webwallet.Page
import io.usoamic.webwallet.util.Async
import js.externals.jquery.JQuery
import js.externals.jquery.extension.removeHidden
import js.externals.jquery.extension.showLoading
import org.w3c.dom.HTMLElement

class CreateWalletView(application: Application) : View(application) {
    override val view = jQuery("#create_wallet_view")
    override val navBarItem: JQuery<HTMLElement>? = null
    private val nextBtn = jQuery("#next_button")
    private val privateKeyElement = jQuery("#new_pk_element")
    private val addressElement = jQuery("#new_address_element")

    init {
        application.hideNavigationBar()
        preparePrivateKey()
        setListeners()
    }

    override fun startLoading() {
        privateKeyElement.showLoading()
        addressElement.showLoading()
    }

    private fun preparePrivateKey() {
        startLoading()
        Async.launch {
            val wallet = Wallet.generate()
            privateKeyElement.text(wallet.getPrivateKeyAsString())
            addressElement.text(wallet.getAddressAsString())
            nextBtn.removeHidden()
        }
    }

    private fun setListeners() {
        nextBtn.onClick {
            application.openPage(Page.ADD)
        }
    }

    companion object {
        fun newInstance(application: Application) {
            return application.open(CreateWalletView(application))
        }
    }
}