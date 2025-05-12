package com.example.chatter

import android.content.Context
import android.net.Uri
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import java.util.UUID

class SupabbaseStorageUtils(val context: Context) {
    // kết nối với supabase
    val supabase = createSupabaseClient(
        "https://rdevyvydkskplaszoaqj.supabase.co",
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJkZXZ5dnlka3NrcGxhc3pvYXFqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDYzMTU3OTAsImV4cCI6MjA2MTg5MTc5MH0.lIp9Qen-Qo_tYP2dOa92WovmNrysbLWO6ATuTI4QWLQ"
    ) {
        // cấu hình supabase với tính năng lưu trữ tệp
        install(Storage)
    }


    suspend fun upLoadImage(uri: Uri): String? {
        try {
            val path = uri.path?.substringAfterLast(".") ?: "jpg"
            val fileName = "${UUID.randomUUID()}.$path"

            val inputStream = context.contentResolver.openInputStream(uri) ?: return null

            supabase.storage.from(BUCKET_NAME).upload(fileName, inputStream.readBytes())

            val publicUrl = supabase.storage.from(BUCKET_NAME).publicUrl(fileName)

            return publicUrl
        } catch (e: Exception) {
            return null
        }
    }

    companion object {
        const val BUCKET_NAME = "doan-images"
    }
}