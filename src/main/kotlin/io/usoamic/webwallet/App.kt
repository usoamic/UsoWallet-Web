package io.usoamic.webwallet

import io.usoamic.usoamicktjs.model.Account
import io.usoamic.web3kt.kt2js.require
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import io.usoamic.webwallet.enumcls.Page
import io.usoamic.webwallet.view.*
import js.externals.jquery.JQuery
import js.externals.jquery.extension.onClick
import js.externals.jquery.extension.setActive
import js.externals.jquery.jQuery
import js.externals.toastr.toastr
import org.w3c.dom.HTMLElement
import kotlinx.browser.localStorage
import kotlinx.browser.window

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

    override fun runLoader() {
        loader.fadeIn()
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

        setTitle(view)

        view.navBarItem?.setActive(true)
        currentView = view
        currentView.onStart()
        currentView.onRefresh()
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
            FirstView.open(this)
        }

        window.addEventListener(
            "hashchange",
            {
                onHashChange()
            },
            false
        )
    }

    private fun setTitle(view: View) {
        val pageTitle = when(view) {
            is FirstView -> "Login"
            is AddWalletView -> "Add Wallet"
            is CreateWalletView -> "Create Wallet"
            is DashboardView -> "Dashboard"
            is DepositView -> "Deposit"
            is WithdrawView -> "Withdraw"
            is ExchangeView -> "Exchange"
            is HistoryView -> "History"
            else -> ""
        }
        window.document.title = "Usoamic Wallet" + (if(pageTitle.isNotEmpty()) " - $pageTitle" else "")
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
                FirstView.open(this)
            }
            Page.ADD -> {
                AddWalletView.open(this)
            }
            Page.CREATE -> {
                CreateWalletView.open(this)
            }
            Page.DASHBOARD -> {
                DashboardView.open(this)
            }
            Page.DEPOSIT -> {
                DepositView.open(this)
            }
            Page.WITHDRAW -> {
                WithdrawView.open(this)
            }
            Page.EXCHANGE -> {
                ExchangeView.open(this)
            }
            Page.HISTORY -> {
                HistoryView.open(this)
            }
        }
    }

    override fun hasWallet(): Boolean {
        return (!getWallet().isNullOrEmpty())
    }

    override fun getWallet(): String? {
        return localStorage.getItem(Account.ACCOUNT)
    }

    override fun setWallet(json: String) {
        localStorage.setItem(Account.ACCOUNT, json)
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
        require("bootstrap")
        require("jquery")
        require("popper.js")
        require("babel-polyfill")
    }
}

