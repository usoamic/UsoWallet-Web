package io.usoamic.usoamickotlinjs.core

import io.usoamic.usoamickotlinjs.model.Transaction
import io.usoamic.web3kt.core.contract.response.CallResponse

interface TransactionExplorer : Purchases {
    @JsName("getTransaction")
    fun getTransaction(txId: String): CallResponse<Transaction>

    @JsName("getNumberOfTransactions")
    fun getNumberOfTransactions(): CallResponse<String>

    @JsName("getNumberOfTransactionsByAddress")
    fun getNumberOfTransactionsByAddress(owner: String): CallResponse<String>
}