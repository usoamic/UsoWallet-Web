package io.usoamic.webwallet

import io.usoamic.usoamicktjs.enumcls.NetworkType
import io.usoamic.usoamicktjs.enumcls.NodeProvider
import io.usoamic.usoamicktjs.other.Config

object AppConfig {
    const val ABI = Config.ABI

    val NETWORK = NetworkType.MAINNET
    val NODE_PROVIDER = NodeProvider.INFURA
}