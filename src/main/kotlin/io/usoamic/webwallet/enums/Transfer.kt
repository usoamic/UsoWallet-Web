package io.usoamic.webwallet.enums

enum class Transfer {
    DEPOSIT,
    WITHDRAWAL;

    fun toPlainString(): String {
        val str = this.toString()
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase()
    }
}