package com.roman.application.network

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.nio.charset.Charset
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class DecryptionInterceptor(private val keyString: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val encryptedBytes = originalResponse.body?.bytes() ?: throw Exception("Response body is null")

        val keyBytes = hashKey(keyString)
        val secretKey = SecretKeySpec(keyBytes, "AES")

        // Extract the IV (first 16 bytes) from the encrypted data
        val iv = encryptedBytes.copyOfRange(0, 16)
        val cipherBytes = encryptedBytes.copyOfRange(16, encryptedBytes.size)

        // Initialize the cipher for decryption with the key and IV
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

        // Decrypt the data
        val decryptedBytes = cipher.doFinal(cipherBytes)
        val decryptedContent = String(decryptedBytes, Charset.forName("UTF-8"))

        // Create a new response with the decrypted content
        val decryptedResponseBody = decryptedContent.toResponseBody(originalResponse.body?.contentType())
        return originalResponse.newBuilder().body(decryptedResponseBody).build()
    }

    private fun hashKey(key: String): ByteArray {
        val sha256 = MessageDigest.getInstance("SHA-256")
        return sha256.digest(key.toByteArray(Charset.forName("UTF-8")))
    }
}