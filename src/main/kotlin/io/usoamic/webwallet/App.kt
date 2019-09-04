package io.usoamic.webwallet

import io.usoamic.usoamickotlinjs.other.Config
import io.usoamic.web3kt.kt2js.require
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import io.usoamic.webwallet.enums.Page
import io.usoamic.webwallet.view.*
import js.externals.jquery.JQuery
import js.externals.jquery.extension.onClick
import js.externals.jquery.extension.setActive
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement
import kotlin.browser.localStorage
import kotlin.browser.window

class App : Application {
    private val baseElement: JQuery<HTMLElement> = jQuery("#html")
    override lateinit var currentView: View
    private val toFirstBtn = jQuery(".to_first")
    private val loader = jQuery(".loader")

    init {
        loadDependency()
        setListeners()
    }

    override fun onStart() {
        startLoading()
        openPage(Page.FIRST)
        stopLoading()
    }

    override fun startLoading() { }

    override fun stopLoading() {
        loader.delay(0).fadeOut()
    }

    override fun open(view: View) {
        if(::currentView.isInitialized) {
            currentView.onStop()
            currentView.navBarItem?.setActive(false)
        }
        view.navBarItem?.setActive(true)
        currentView = view
        currentView.onStart()
    }

    override fun openPage(page: Page) {
        window.location.hash = "#${page.name.toLowerCase()}"
    }

    override fun onException(t: Throwable) {
        println(t.message)
    }

    private fun setListeners() {
        toFirstBtn.onClick {
            FirstView.newInstance(this)
        }

        window.addEventListener(
            "hashchange",
            {
                println("hash: ${window.location.hash}")
                try {
                    val page = Page.valueOf(window.location.hash.replace("#", "").toUpperCase())
                    println("page: $page")
                    when (page) {
                        Page.FIRST -> {
                            FirstView.newInstance(this)
                        }
                        Page.ADD -> {
                            AddWalletView.newInstance(this)
                        }
                        Page.CREATE -> {
                            CreateWalletView.newInstance(this)
                        }
                        Page.DASHBOARD -> {
                            DashboardView.newInstance(this)
                        }
                        Page.DEPOSIT -> {
                            DepositView.newInstance(this)
                        }
                        Page.WITHDRAW -> {
                            WithdrawView.newInstance(this)
                        }
                        Page.HISTORY -> {
                            HistoryView.newInstance(this)
                        }
                    }
                }
                catch (e: IllegalStateException) {
                    println("e1: ${e.message}")
                    if (!localStorage.getItem(Config.ACCOUNT_FILENAME).isNullOrEmpty()) {
                        DashboardView.newInstance(this)
                    } else {
                        FirstView.newInstance(this)
                    }
                    return@addEventListener
                }
            },
            false
        )
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

    override fun loadDependency() {
        require("datatables.net-bs4")
    }
}

