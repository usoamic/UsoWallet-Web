package js.externals.qrcode

import org.w3c.dom.Element
import org.w3c.dom.HTMLCanvasElement

@JsModule("qrcode")
external class QRCode {
    companion object {
        fun toCanvas(canvas: Element, str: String, options: QRCodeOptions, callback: (String?) -> Unit)
        fun toCanvas(canvas: Element, str: String, callback: (String?) -> Unit)
    }
}