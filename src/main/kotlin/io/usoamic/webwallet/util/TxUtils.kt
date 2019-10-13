package io.usoamic.webwallet.util

import io.usoamic.webwallet.enumcls.TransferType

class TxUtils {
    companion object {
        fun getTxType(owner: String, from: String, to: String): TransferType {
            val lcOwner = owner.toLowerCase()
            val lcFrom = from.toLowerCase()
            val lcTo = to.toLowerCase()
            return when {
                ((lcOwner != lcFrom) && (lcOwner == lcTo)) -> TransferType.DEPOSIT
                ((lcOwner == lcFrom) && (lcOwner != lcTo)) -> TransferType.WITHDRAWAL
                ((lcOwner == lcFrom) && (lcOwner == lcTo)) -> TransferType.INTERNAL
                else -> TransferType.UNKNOWN
            }
        }
    }
}