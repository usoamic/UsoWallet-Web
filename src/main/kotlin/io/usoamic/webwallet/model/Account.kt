package io.usoamic.webwallet.model

interface Account {
    val version: Int
    val id: String
    val address: String
}