package io.usoamic.webwallet.view

import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.WalletView
import js.externals.jquery.JQuery
import js.externals.jquery.jQuery
import js.externals.qrcode.QRCode
import js.externals.qrcode.QRCodeOptions
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import kotlin.browser.document

class DepositView(application: Application) : WalletView(application) {
    override val view = jQuery("#deposit_view")
    override val navBarItem: JQuery<HTMLElement>? = jQuery("#deposit_item")
    private val textAddress: JQuery<HTMLElement> = jQuery("#text_address")
    private val qrAddress: HTMLCanvasElement? = document.getElementById("qr_address") as HTMLCanvasElement

    init {
        prepareQrCode()
        prepareAddress()
    }

    private fun prepareQrCode() {
        qrAddress?.let { el ->
            QRCode.toCanvas(el, "Q", QRCodeOptions(scale = 8)) { err ->
                onException(err)
            }
        }
    }

    private fun prepareAddress() {
        textAddress.text(address)
    }

    companion object {
        fun newInstance(application: Application) {
            return application.open(DepositView(application))
        }
    }
}