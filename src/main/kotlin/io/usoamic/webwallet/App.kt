package io.usoamic.webwallet

import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import io.usoamic.webwallet.view.FirstView
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery

class App : Application {
    override lateinit var currentView: View

    val toFirstBtn = jQuery(".to_first")
    val loader = jQuery(".loader")

    init {
        setListeners()
    }

    override fun onStart() {
        startLoading()
        FirstView.newInstance(this)
    }

    override fun startLoading() { }

    override fun stopLoading() {
        loader.delay(1500).fadeOut()
    }

    override fun open(view: View) {
        if(::currentView.isInitialized) {
            currentView.onStop()
        }
        currentView = view
        currentView.onStart()
    }

    private fun setListeners() {
        toFirstBtn.onClick {
            FirstView.newInstance(this)
        }
    }
}

