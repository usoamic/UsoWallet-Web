package io.usoamic.webwallet.base

import js.externals.jquery.JQuery
import js.externals.jquery.extension.clearText
import js.externals.jquery.extension.clearVal
import org.w3c.dom.HTMLElement

abstract class View(protected val application: Application) {
    abstract val view: JQuery<HTMLElement>
    abstract val navBarItem: JQuery<HTMLElement>?

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

    protected fun clearInputs(list: List<JQuery<HTMLElement>>) {
        list.forEach {
            it.clearVal()
        }
    }

    protected fun clearTexts(list: List<JQuery<HTMLElement>>) {
        list.forEach {
            it.clearText()
        }
    }

    protected fun onException(s: String?) {
        s?.let {
            application.onException(s)
        }
    }

    protected fun onException(t: Throwable) {
        t.message?.let {
            onException(it)
        }
    }
}