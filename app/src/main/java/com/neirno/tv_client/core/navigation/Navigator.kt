package com.neirno.tv_client.core.navigation

interface Navigator {
    fun toMainScreen()
    fun toHistoryScreen()
    fun toQueryScreen()
    fun toConnectionScreen()
    fun back()
}
