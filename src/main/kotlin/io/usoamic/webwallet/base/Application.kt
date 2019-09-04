package io.usoamic.webwallet.base

import io.usoamic.webwallet.Page

interface Application {
    var currentView: View
    fun onStart()
    fun startLoading()
    fun stopLoading()
    fun open(view: View)
    fun openPage(page: Page)
    fun onException(t: Throwable)
    fun showNavigationBar()
    fun hideNavigationBar()
    fun loadDependency()
}