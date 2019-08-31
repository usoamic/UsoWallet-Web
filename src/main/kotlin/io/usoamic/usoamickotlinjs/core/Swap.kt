package io.usoamic.usoamickotlinjs.core

import io.usoamic.web3kt.core.contract.response.CallResponse
import io.usoamic.web3kt.core.contract.response.ExecuteResponse

interface Swap {
    @JsName("withdrawEth")
    fun withdrawEth(value: String): ExecuteResponse

    @JsName("burnSwap")
    fun burnSwap(value: String): ExecuteResponse

    @JsName("setSwapRate")
    fun setSwapRate(swapRate: String): ExecuteResponse

    @JsName("setSwappable")
    fun setSwappable(swappable: Boolean): ExecuteResponse

    @JsName("getSwapBalance")
    fun getSwapBalance(): String

    @JsName("getSwapRate")
    fun getSwapRate(): String

    @JsName("getSwappable")
    fun getSwappable(): Boolean
}