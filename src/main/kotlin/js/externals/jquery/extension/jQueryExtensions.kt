package js.externals.jquery.extension

import js.externals.jquery.JQuery
import org.w3c.dom.HTMLElement

fun JQuery<HTMLElement>.onClick(callback: () -> Unit) {
    this.on("click", callback)
}