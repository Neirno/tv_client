package com.neirno.tv_client.core.util

fun isValidIP(ip: String): Boolean {
    return ip.matches(Regex("\\b((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\.)){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\b"))
}
