package com.api.utils

import org.mindrot.jbcrypt.BCrypt


// Email validation function
fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return emailRegex.matches(email)
}

fun verifyPassword(inputPassword: String, hashedPassword: String): Boolean {
    return BCrypt.checkpw(inputPassword, hashedPassword)
}
