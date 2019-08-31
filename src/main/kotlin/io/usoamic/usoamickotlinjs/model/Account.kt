package io.usoamic.usoamickotlinjs.model

import io.usoamic.web3kt.bignumber.BigNumber

data class Account(
    val address: String,
    val path: String,
    val name: String,
    val timestamp: BigNumber
)