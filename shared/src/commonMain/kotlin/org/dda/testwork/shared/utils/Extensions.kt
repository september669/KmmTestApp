package org.dda.testwork.shared.utils

inline fun <T> T.checkWhen(): T = this

/***************************************/

inline infix fun <A, B, C> Pair<A, B>.to(that: C): Triple<A, B, C> = Triple(first, second, that)


/***************************************/

inline fun CharSequence.isEmptyOrBlank(): Boolean = isBlank()

inline fun CharSequence.isNotEmptyOrBlank(): Boolean = isNotBlank()

inline fun CharSequence?.isNullOrEmptyOrBlank(): Boolean = this == null || isBlank()

inline fun CharSequence?.isNotNullOrEmptyOrBlank(): Boolean = this != null && isNotBlank()


fun randomString(length: Int): String {
    val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    return (1..kotlin.random.Random.nextInt(0, length))
        .map { i -> kotlin.random.Random.nextInt(0, alphabet.length) }
        .map(alphabet::get)
        .joinToString("")
}
