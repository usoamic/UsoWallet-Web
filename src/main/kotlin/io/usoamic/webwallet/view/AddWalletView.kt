package io.usoamic.webwallet.view

import io.usoamic.web3kt.kt2js.require
import io.usoamic.web3kt.util.extension.removeHexPrefixIfExist
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import io.usoamic.webwallet.util.ValidateUtil
import js.externals.jquery.extension.onClick
import js.externals.jquery.jQuery
import io.usoamic.web3kt.wallet.extension.fromPrivateKey
import io.usoamic.web3kt.wallet.Wallet
import io.usoamic.webwallet.util.Async
import js.externals.jquery.extension.startLoading
import js.externals.jquery.extension.stopLoading
import js.externals.toastr.extensions.error
import js.externals.toastr.toastr
import io.usoamic.web3kt.wallet.extension.toJsonString
import kotlin.browser.localStorage
import io.usoamic.usoamickotlinjs.other.Config
import js.externals.jquery.JQuery
import org.w3c.dom.HTMLElement

class AddWalletView(application: Application) : View(application) {
    override val view = jQuery("#add_wallet_view")
    override val navBarItem: JQuery<HTMLElement>? = null
    private val privateKeyInput = jQuery("#add_pk_input")
    private val passwordInput = jQuery("#add_password_input")
    private val confirmPasswordInput = jQuery("#add_confirm_password_input")
    private val saveBtn = jQuery("#save_button")

    init {
        application.hideNavigationBar()
        setListeners()
    }

    override fun startLoading() {
        saveBtn.startLoading()
    }

    override fun stopLoading() {
        saveBtn.stopLoading(true)
    }

    private fun setListeners() {
        saveBtn.onClick {
            startLoading()
            try {
                val privateKey = privateKeyInput.content().removeHexPrefixIfExist()
                val password = passwordInput.content()
                val confirmPassword = confirmPasswordInput.content()

                ValidateUtil.validatePrivateKey(privateKey)
                    .validatePassword(password)
                    .validateConfirmPassword(confirmPassword)

                Async.launch {
                    val json = Wallet.fromPrivateKey(privateKey).toV3(password).toJsonString()
                    localStorage.setItem(Config.ACCOUNT_FILENAME, json)
                    stopLoading()
                    application.openPage("dashboard")
                }

            } catch (e: Throwable) {
                saveBtn.stopLoading(false)
                toastr.error(e.message)
            }
        }
    }

    companion object {
        fun newInstance(application: Application) {
            application.open(AddWalletView(application))
        }
    }
}