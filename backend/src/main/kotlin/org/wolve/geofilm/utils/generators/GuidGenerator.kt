package org.wolve.geofilm.utils.generators

import kotlin.random.Random

class GuidGenerator {

    fun generarId(): String {
        val caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val id = StringBuilder()

        repeat(11) {
            val index = Random.nextInt(0, caracteres.length)
            id.append(caracteres[index])
        }

        return id.toString()
    }
}
