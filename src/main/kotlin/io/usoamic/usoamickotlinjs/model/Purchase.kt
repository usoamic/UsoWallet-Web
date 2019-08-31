package io.usoamic.usoamickotlinjs.model

import io.usoamic.web3kt.bignumber.BigNumber

data class Purchase constructor(
    val isExist: Boolean,
    val id: BigNumber,
    val purchaseId: String,
    val appId: String,
    val cost: BigNumber,
    val timestamp: BigNumber
) {
    class Builder {
        private var isExist: Boolean = false
        private lateinit var id: BigNumber
        private lateinit var purchaseId: String
        private lateinit var appId: String
        private lateinit var cost: BigNumber
        private lateinit var timestamp: BigNumber

        fun setIsExist(isExist: Boolean) = apply {
            this.isExist = isExist
        }

        fun setId(id: BigNumber) = apply {
            this.id = id
        }

        fun setPurchaseId(purchaseId: String) = apply {
            this.purchaseId = purchaseId
        }

        fun setAppId(appId: String) = apply {
            this.appId = appId
        }

        fun setCost(cost: BigNumber) = apply {
            this.cost = cost
        }

        fun setTimestamp(timestamp: BigNumber) = apply {
            this.timestamp = timestamp
        }

        fun build() = Purchase(
            isExist,
            id,
            purchaseId,
            appId,
            cost,
            timestamp
        )
    }
}