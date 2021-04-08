package kr.bgsoft.ucargo.jeju.utils

import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.spec.AlgorithmParameterSpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AES256 {
    val ivBytes = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
    var setKey  = ""
    var setKey2 = ""

    fun encode(value: String): String? {
        try {
            val textbyte = value.toByteArray(Charsets.UTF_8)
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
            val newKey = SecretKeySpec(setKey.toByteArray(Charsets.UTF_8), "AES")
            val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)

            return Base64.encodeToString(cipher.doFinal(textbyte), 0)
        } catch (e: Exception) {
            return ""
        }
    }

    fun decode(value: String): String? {
        try {
            val textbyte = Base64.decode(value, 0)
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
            val newKey = SecretKeySpec(setKey.toByteArray(Charsets.UTF_8), "AES")
            val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
            return String(cipher.doFinal(textbyte), Charsets.UTF_8)
        } catch (e: Exception) {
            return ""
        }
    }

    fun set(key: String) {
        setKey = key.take(16)
    }
    fun set2(key: String) {
        setKey2 = key.take(16)
    }

    fun getHash(): String {
        try {
            val date = Etc.convertDateToString(Date(), "yyyyMMddHHmmss")
            val base = date + setKey2
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(base.toByteArray(Charsets.UTF_8))
            val hexString = StringBuffer()

            for (i in hash.indices) {
                val hex = Integer.toHexString(0xff and hash[i].toInt())
                if (hex.length == 1) hexString.append('0')
                hexString.append(hex)
            }

            return date + hexString.toString()
        } catch (ex: Exception) {
            return ""
        }

    }
}