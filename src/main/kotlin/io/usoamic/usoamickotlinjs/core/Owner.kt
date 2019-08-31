package io.usoamic.usoamickotlinjs.core

import io.usoamic.web3kt.core.contract.response.ExecuteResponse

interface Owner {
    @JsName("setFrozen")
    fun setFrozen(frozen: Boolean): ExecuteResponse

    @JsName("setOwner")
    fun setOwner(newOwner: String): ExecuteResponse
}