package io.usoamic.webwallet.base

interface Application {
    var currentView: View
    fun onStart()
    fun startLoading()
    fun stopLoading()
    fun open(view: View)
}