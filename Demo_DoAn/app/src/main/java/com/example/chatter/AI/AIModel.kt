package com.example.chatter.AI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatter.model.Job
import com.example.chatter.model.UserCV
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

private val _matchResults = MutableLiveData<List<MatchResult>>()
val matchResults: LiveData<List<MatchResult>> get() = _matchResults

fun loadAndMatchCVs(jobId: String) {
    val db = Firebase.firestore

    // Lấy job
    db.collection("jobs").document(jobId).get().addOnSuccessListener { jobSnap ->
        val job = jobSnap.toObject(Job::class.java) ?: return@addOnSuccessListener

        // Lấy toàn bộ CV
        db.collection("userCVs").get().addOnSuccessListener { cvSnap ->
            val cvList = cvSnap.documents.mapNotNull { it.toObject(UserCV::class.java) }

            // Gọi AI
            val results = CVMatchingEngine.matchMultipleCVs(cvList, job)
            print("hi")

            // Cập nhật Data
            _matchResults.postValue(results)
        }
    }
}