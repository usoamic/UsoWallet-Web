package io.usoamic.web3kt

@JsModule("web3")
external class Web3(provider: String) {
    var utils: Utils = definedExternally
    var eth: Eth = definedExternally

    companion object {
        var utils: Utils = definedExternally
    }
}


external class Buffer {
    companion object {
        fun from(arg: String, encoding: String): Buffer
    }
    val length: Int
}

@JsModule("ethereumjs-tx")
external class Tx(tx: Any) {
    fun sign(privateKey: dynamic)
    fun serialize(): String
}

external class Eth {
    fun getBalance(address: String, defaultBlock: String, callback: (error: String?, balance: String?) -> Unit)
    fun getTransactionCount(address: String, defaultBlock: String, callback: (error: String?, balance: String?) -> Unit)
    fun getBlockNumber(callback: (error: String?, number: String?) -> Unit)
    fun estimateGas(`object`: Any, callback: (error: String?, gas: String?) -> Unit): String
    fun sendSignedTransaction(sendSignedTransaction: String)
}

external class Utils {
    fun checkAddressChecksum(address: String): Boolean
    fun toChecksumAddress(address: String): String
    fun isAddress(address: String): Boolean
    fun fromWei(value: String, unit: String): Double
    fun toWei(number: String, unit: String): Double
}