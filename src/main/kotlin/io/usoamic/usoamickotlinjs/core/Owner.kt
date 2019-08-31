package io.usoamic.usoamickotlinjs.core

import io.usoamic.web3kt.core.contract.response.ExecuteResponse

interface Owner {
    @JsName("setFrozen")
    fun setFrozen(password: String, frozen: Boolean): ExecuteResponse

    @JsName("setOwner")
    fun setOwner(password: String, newOwner: String): ExecuteResponse
}