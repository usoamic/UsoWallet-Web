package io.usoamic.usoamickotlinjs.core

import io.usoamic.web3kt.core.contract.other.ContractWrapper
import io.usoamic.web3kt.core.contract.response.ExecuteResponse

interface Owner : ContractWrapper {
    @JsName("setFrozen")
    fun setFrozen(frozen: Boolean): ExecuteResponse

    @JsName("setOwner")
    fun setOwner(newOwner: String): ExecuteResponse
}