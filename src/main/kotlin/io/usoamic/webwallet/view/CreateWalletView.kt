package io.usoamic.webwallet.view

import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery
import io.usoamic.web3kt.wallet.Wallet
import io.usoamic.webwallet.util.Async
import js.externals.jquery.extension.removeHidden
import js.externals.jquery.extension.showLoading

class CreateWalletView(application: Application) : View(application) {
    override val view = jQuery("#create_wallet_view")
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
            AddWalletView.newInstance(application)
        }
    }

    companion object {
        fun newInstance(application: Application) {
            return application.open(CreateWalletView(application))
        }
    }
}