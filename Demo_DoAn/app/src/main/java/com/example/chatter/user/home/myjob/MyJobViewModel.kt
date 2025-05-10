package com.example.chatter.user.home.myjob

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatter.model.Job
import com.example.chatter.model.UserCV
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MyJobViewModel : ViewModel() {

    private val _userCVs = MutableStateFlow<List<UserCV>>(emptyList())
    val userCVs: StateFlow<List<UserCV>> = _userCVs

    private val _jobs = MutableStateFlow<Map<String, Job>>(emptyMap())
    val jobs: StateFlow<Map<String, Job>> = _jobs


    init {
        getUserCv()
    }

    private fun getUserCv() {
        val ref = FirebaseDatabase.getInstance().getReference("usercv")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<UserCV>()
                for (child in snapshot.children) {
                    val cv = child.getValue(UserCV::class.java)
                    if (cv != null) {
                        cv.id = child.key.toString()
                        list.add(cv)
                    }
                }
                _userCVs.value = list

                getJob(list)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }

    private fun getJob(cvList: List<UserCV>) {
        viewModelScope.launch {
            try {
                val jobIds = cvList.map { it.jobId }.distinct()
                val jobsMap = mutableMapOf<String, Job>()

                jobIds.forEach { jobId ->
                    if (jobId.isNotEmpty()) {
                        val snapshot = FirebaseDatabase.getInstance()
                            .getReference("job")
                            .child(jobId)
                            .get()
                            .await()

                        val job = snapshot.getValue(Job::class.java)
                        if (job != null) {
                            jobsMap[jobId] = job
                        }
                    }
                }

                _jobs.value = jobsMap

            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteCV(cvId: String) {
        viewModelScope.launch {
            try {
                FirebaseDatabase.getInstance()
                    .getReference("usercv")
                    .child(cvId)
                    .removeValue()
                    .await()

                getUserCv()
            } catch (e: Exception) {
                Log.e("MyJobViewModel", "Error deleting CV", e)
            }
        }
    }



}