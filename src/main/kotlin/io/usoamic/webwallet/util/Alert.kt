package io.usoamic.webwallet.util

class Alert {
    companion object {
        fun show(str: String) {
            js("alert(str);")
        }
    }
}