package io.usoamic.webwallet.model

import io.usoamic.web3kt.bignumber.BigNumber
import io.usoamic.webwallet.enumcls.Asset

data class WithdrawTxData(
    val nonce: BigNumber,
    val asset: Asset,
    val address: String,
    val amount: String,
    val password: String
)