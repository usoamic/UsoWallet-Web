package io.usoamic.webwallet.view

import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import io.usoamic.webwallet.enumcls.Page
import js.externals.jquery.JQuery
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement

class FirstView(application: Application) : View(application) {
    override val view = jQuery("#first_view")
    override val navBarItem: JQuery<HTMLElement>? = null
    private val addBtn = jQuery("#add_button")
    private val createBtn = jQuery("#create_button")

    init {
        application.hideNavigationBar()
        setListeners()
    }

    private fun setListeners() {
        addBtn.onClick {
           application.openPage(Page.ADD)
        }

        createBtn.onClick {
            application.openPage(Page.CREATE)
        }
    }

    companion object {
        private var instance: FirstView? = null

        fun open(application: Application) {
            if(instance == null) {
                instance = FirstView(application)
            }
            return application.open(instance!!)
        }
    }
}