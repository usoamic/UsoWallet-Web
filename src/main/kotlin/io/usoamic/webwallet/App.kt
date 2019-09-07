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
import js.externals.toastr.extensions.error
import js.externals.toastr.toastr
import org.w3c.dom.HTMLElement
import kotlin.browser.localStorage
import kotlin.browser.window
import kotlin.js.Json

class App : Application {
    private val baseElement: JQuery<HTMLElement> = jQuery("#html")
    override lateinit var currentView: View
    private val toFirstBtn = jQuery(".to_first")
    private val loader = jQuery(".loader")

    init {
        startLoading()
        loadDependency()
        setListeners()
    }

    override fun onStart() {
        onHashChange()
        stopLoading()
    }

    override fun startLoading() {}

    override fun stopLoading() {
        loader.delay(0).fadeOut()
    }

    override fun open(view: View) {
        if (::currentView.isInitialized) {
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

    override fun onError(s: String?) {
        s?.let {
            toastr.error(it)
        }
    }

    override fun onException(t: Throwable) {
        onError(t.message)
    }

    private fun setListeners() {
        toFirstBtn.onClick {
            FirstView.newInstance(this)
        }

        window.addEventListener(
            "hashchange",
            {
                onHashChange()
            },
            false
        )
    }

    private fun onHashChange() {
        val page = try {
            val p = Page.valueOf(window.location.hash.replace("#", "").toUpperCase())
            if (p.isAuthPage() && hasWallet()) {
                Page.DASHBOARD
            } else if (p.isLoginPage() && !hasWallet()) {
                Page.FIRST
            } else p
        } catch (e: IllegalStateException) {
            if (hasWallet()) {
                Page.DASHBOARD
            } else {
                Page.FIRST
            }
        }

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

    override fun hasWallet(): Boolean {
        return (!getWallet().isNullOrEmpty())
    }

    override fun getWallet(): String? {
        return localStorage.getItem(Config.ACCOUNT_FILENAME)
    }

    override fun setWallet(json: String) {
        localStorage.setItem(Config.ACCOUNT_FILENAME, json)
    }

    override fun showNavigationBar() {
        if (baseElement.hasClass("login_page")) {
            baseElement.removeClass("login_page")
        }
    }

    override fun hideNavigationBar() {
        if (!baseElement.hasClass("login_page")) {
            baseElement.addClass("login_page")
        }
    }

    override fun loadDependency() {
        require("datatables.net-bs4")
    }
}

