package io.usoamic.webwallet.model

import io.usoamic.web3kt.bignumber.BigNumber

data class ExchangeTxData(
    val nonce: BigNumber,
    val amount: String,
    val password: String
)