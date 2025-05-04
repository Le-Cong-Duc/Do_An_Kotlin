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
            //Lấy phần path của uri nếu không lấy được thif mặc định là jpg
            val path = uri.path?.substringAfterLast(".") ?: "jpg"
            // Tạo tên tệp ngẫu nhiên
            val fileName = "${UUID.randomUUID()}.$path"
            //mở một luồng dữ liệu từ uri nếu không -> trả về null
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null

            // dùng supabase api để tải ảnh lên bucket trên supabase ở đây là doan-images với tên file và inputStream
            supabase.storage.from(BUCKET_NAME).upload(fileName, inputStream.readBytes())

            // Sau khi tải lên thành công thì lấy tên ảnh từ supabase storage
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