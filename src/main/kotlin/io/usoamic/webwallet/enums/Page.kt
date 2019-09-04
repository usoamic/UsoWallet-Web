package io.usoamic.webwallet.enums

enum class Page {
    FIRST,
    ADD,
    CREATE,
    DASHBOARD,
    DEPOSIT,
    WITHDRAW,
    HISTORY;

    fun isAuthPage(): Boolean {
        return (listOf<Page>(FIRST, ADD, CREATE).contains(this))
    }

    fun isLoginPage(): Boolean {
        return (listOf<Page>(DASHBOARD, DEPOSIT, WITHDRAW, HISTORY).contains(this))
    }
}