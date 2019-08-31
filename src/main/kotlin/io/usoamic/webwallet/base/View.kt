package io.usoamic.webwallet.base

import io.usoamic.webwallet.App
import js.externals.jquery.JQuery
import org.w3c.dom.HTMLElement

abstract class View(protected val application: Application) {
    abstract val view: JQuery<HTMLElement>

    fun startLoading() {
        application.startLoading()
    }

    fun stopLoading() {
        application.stopLoading()
    }

    fun onStart() {
        view.show()
    }

    fun onStop() {
        view.hide()
    }
}