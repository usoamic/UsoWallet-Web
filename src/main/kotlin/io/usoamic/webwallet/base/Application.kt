package io.usoamic.webwallet.base

interface Application {
    var currentView: View
    fun onStart()
    fun startLoading()
    fun stopLoading()
    fun open(view: View)
    fun onException(t: Throwable)
    fun showNavigationBar()
    fun hideNavigationBar()
}