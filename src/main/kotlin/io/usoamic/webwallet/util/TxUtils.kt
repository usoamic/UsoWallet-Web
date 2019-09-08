package io.usoamic.webwallet.util

import io.usoamic.webwallet.enumcls.Transfer

class TxUtils {
    companion object {
        fun getTxType(owner: String, from: String, to: String): Transfer {
            val lcOwner = owner.toLowerCase()
            val lcFrom = from.toLowerCase()
            val lcTo = to.toLowerCase()
            return when {
                ((lcOwner != lcFrom) && (lcOwner == lcTo)) -> Transfer.DEPOSIT
                ((lcOwner == lcFrom) && (lcOwner != lcTo)) -> Transfer.WITHDRAWAL
                ((lcOwner == lcFrom) && (lcOwner == lcTo)) -> Transfer.INTERNAL
                else -> Transfer.UNKNOWN
            }
        }
    }
}