package io.usoamic.webwallet.view

import io.usoamic.usoamicktjs.model.Account
import io.usoamic.web3kt.util.extension.removeHexPrefixIfExist
import io.usoamic.web3kt.wallet.Wallet
import io.usoamic.web3kt.wallet.extension.fromPrivateKey
import io.usoamic.web3kt.wallet.extension.toJsonString
import io.usoamic.webwallet.base.Application
import io.usoamic.webwallet.base.View
import io.usoamic.webwallet.enumcls.Page
import io.usoamic.webwallet.util.Async
import io.usoamic.webwallet.util.ValidateUtil
import js.externals.jquery.JQuery
import js.externals.jquery.extension.enable
import js.externals.jquery.extension.onClick
import js.externals.jquery.extension.startLoading
import js.externals.jquery.extension.stopLoading
import js.externals.jquery.jQuery
import js.externals.toastr.extensions.error
import js.externals.toastr.toastr
import org.w3c.dom.HTMLElement
import kotlinx.browser.localStorage

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
                    .validatePasswords(password, confirmPassword)

                Async.launch {
                    val json = Wallet.fromPrivateKey(privateKey).toV3(password).toJsonString()
                    localStorage.setItem(Account.ACCOUNT, json)
                    stopLoading()
                    application.openPage(Page.DASHBOARD)
                }

            } catch (e: Throwable) {
                saveBtn.stopLoading(false)
                toastr.error(e.message)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        saveBtn.enable()
        clearInputs(listOf(privateKeyInput, passwordInput, confirmPasswordInput))
    }

    companion object {
        private var instance: AddWalletView? = null

        fun open(application: Application) {
            if(instance == null) {
                instance = AddWalletView(application)
            }
            return application.open(instance!!)
        }
    }
}