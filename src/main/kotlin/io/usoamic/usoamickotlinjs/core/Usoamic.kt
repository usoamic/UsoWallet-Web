package io.usoamic.usoamickotlinjs.core

import io.usoamic.web3kt.core.contract.response.CallResponse
import io.usoamic.web3kt.core.contract.response.ExecuteResponse

interface Usoamic {
    @JsName("balanceOf")
    fun balanceOf(address: String): CallResponse<String>

    @JsName("burn")
    fun burn(value: String): ExecuteResponse

    @JsName("transfer")
    fun transfer(to: String, value: String): ExecuteResponse

    @JsName("getSupply")
    fun getSupply(): CallResponse<String>

    @JsName("getVersion")
    fun getVersion(): CallResponse<String>
}