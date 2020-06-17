package io.usoamic.webwallet.model

import io.usoamic.web3kt.bignumber.BigNumber
import io.usoamic.webwallet.enumcls.Asset

data class TxData(
    val nonce: BigNumber,
    val gasPrice: BigNumber,
    val asset: Asset,
    val address: String,
    val amount: String,
    val password: String
)