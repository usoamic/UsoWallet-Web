package io.usoamic.usoamickotlinjs.core

import io.usoamic.usoamickotlinjs.model.Purchase
import io.usoamic.web3kt.core.contract.response.CallResponse
import io.usoamic.web3kt.core.contract.response.ExecuteResponse

interface Purchases {
    @JsName("makePurchase")
    fun makePurchase(appId: String, purchaseId: String, cost: String): ExecuteResponse

    @JsName("getPurchaseByAddress")
    fun getPurchaseByAddress(address: String, id: String): CallResponse<Purchase>

    @JsName("getLastPurchaseId")
    fun getLastPurchaseId(address: String): CallResponse<String>

    @JsName("getNumberOfPurchasesByAddress")
    fun getNumberOfPurchasesByAddress(address: String): CallResponse<String>
}