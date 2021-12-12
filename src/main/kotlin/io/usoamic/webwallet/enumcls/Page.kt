package io.usoamic.webwallet.enumcls

enum class Page {
    FIRST,
    ADD,
    CREATE,
    DASHBOARD,
    DEPOSIT,
    WITHDRAW,
    EXCHANGE,
    HISTORY;

    fun isAuthPage(): Boolean {
        return (listOf<Page>(FIRST, ADD, CREATE).contains(this))
    }

    fun isLoginPage(): Boolean {
        return (listOf<Page>(DASHBOARD, DEPOSIT, WITHDRAW, EXCHANGE, HISTORY).contains(this))
    }
}