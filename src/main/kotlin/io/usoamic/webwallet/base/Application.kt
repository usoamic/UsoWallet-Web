package io.usoamic.webwallet.base

import io.usoamic.webwallet.enumcls.Page

interface Application {
    var currentView: View
    fun onStart()
    fun runLoader()
    fun startLoading()
    fun stopLoading()
    fun open(view: View)
    fun openPage(page: Page)
    fun onException(t: Throwable)
    fun onError(s: String?)
    fun showNavigationBar()
    fun hideNavigationBar()
    fun loadDependency()
    fun hasWallet(): Boolean
    fun getWallet(): String?
    fun setWallet(json: String)
}