package io.usoamic.webwallet

import io.usoamic.usoamicktjs.enumcls.NetworkType
import io.usoamic.usoamicktjs.enumcls.NodeProvider
import io.usoamic.usoamicktjs.other.Config

object AppConfig {
    const val ABI = Config.ABI
    const val NUMBER_OF_DASHBOARD_TRANSFERS = 10L
    const val NUMBER_OF_HISTORY_TRANSFERS = 30L

    val NETWORK = NetworkType.MAINNET
    val NODE_PROVIDER = NodeProvider.INFURA
}