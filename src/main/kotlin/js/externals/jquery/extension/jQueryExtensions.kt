package js.externals.jquery.extension

import js.externals.jquery.JQuery
import org.w3c.dom.HTMLElement

fun JQuery<HTMLElement>.onClick(callback: () -> Unit) {
    this.on("click", callback)
}

fun JQuery<HTMLElement>.startLoading() {
    this.data("text", this.html())
    this.html("<i class=\"fa fa-spinner fa-spin\"></i> Please wait...")
    this.prop("disabled", true)
}

fun JQuery<HTMLElement>.stopLoading(disable: Boolean = false) {
    val text = this.data("text") as String
    if(text.isNotEmpty()) {
        this.html(text)
        this.prop("disabled", disable)
    }
}

fun JQuery<HTMLElement>.showLoading() {
    this.text("Loading...")
}

fun JQuery<HTMLElement>.addHidden() {
    this.addClass("hidden")
}

fun JQuery<HTMLElement>.removeHidden() {
    this.removeClass("hidden")
}