package io.usoamic.webwallet

import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import io.usoamic.webwallet.view.DashboardView
import io.usoamic.webwallet.view.FirstView
import js.externals.jquery.JQuery
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

class App : Application {
    private val baseElement: JQuery<HTMLElement> = jQuery("#html")
    override lateinit var currentView: View
    private val toFirstBtn = jQuery(".to_first")
    private val loader = jQuery(".loader")

    init {
        setListeners()
    }

    override fun onStart() {
        startLoading()
        FirstView.newInstance(this)
        stopLoading()
    }

    override fun startLoading() { }

    override fun stopLoading() {
        loader.delay(0).fadeOut()
    }

    override fun open(view: View) {
        if(::currentView.isInitialized) {
            currentView.onStop()
        }

        currentView = view
        currentView.onStart()
    }

    override fun onException(t: Throwable) {
        println(t.message)
    }

    private fun setListeners() {
        toFirstBtn.onClick {
            FirstView.newInstance(this)
        }
    }

    override fun showNavigationBar() {
        if(baseElement.hasClass("login_page")) {
            baseElement.removeClass("login_page")
        }
    }

    override fun hideNavigationBar() {
        if(!baseElement.hasClass("login_page")) {
            baseElement.addClass("login_page")
        }
    }
}

