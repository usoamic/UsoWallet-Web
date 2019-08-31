package io.usoamic.usoamickotlinjs.core

import io.usoamic.web3kt.core.contract.response.CallResponse
import io.usoamic.web3kt.core.contract.response.ExecuteResponse

interface Swap : TransactionExplorer {
    @JsName("withdrawEth")
    fun withdrawEth(value: String): ExecuteResponse

    @JsName("burnSwap")
    fun burnSwap(value: String): ExecuteResponse

    @JsName("setSwapRate")
    fun setSwapRate(swapRate: String): ExecuteResponse

    @JsName("setSwappable")
    fun setSwappable(swappable: Boolean): ExecuteResponse

    @JsName("getSwapBalance")
    fun getSwapBalance(): CallResponse<String>

    @JsName("getSwapRate")
    fun getSwapRate(): CallResponse<String>

    @JsName("getSwappable")
    fun getSwappable(): CallResponse<Boolean>
}