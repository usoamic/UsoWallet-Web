package io.usoamic.webwallet.base

import io.usoamic.webwallet.App
import js.externals.jquery.JQuery
import org.w3c.dom.HTMLElement

abstract class View(protected val application: Application) {
    abstract val view: JQuery<HTMLElement>

    open fun startLoading() {
        application.startLoading()
    }

    open fun stopLoading() {
        application.stopLoading()
    }

    open fun onStart() {
        view.show()
    }

    open fun onStop() {
        view.hide()
    }
}