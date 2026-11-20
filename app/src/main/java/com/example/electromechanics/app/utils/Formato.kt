package com.electromechanics.app.utils

// "2026-11-19T10:15:30.123456" -> "2026-11-19 10:15:30"
fun String?.fechaCorta(): String = this?.replace("T", " ")?.take(19) ?: "-"
