package io.usoamic.webwallet.util

import io.usoamic.webwallet.enumcls.Transfer

class TxUtils {
    companion object {
        fun getTxType(owner: String, from: String): Transfer {
            return if(owner == from) Transfer.DEPOSIT else Transfer.WITHDRAWAL
        }
    }
}