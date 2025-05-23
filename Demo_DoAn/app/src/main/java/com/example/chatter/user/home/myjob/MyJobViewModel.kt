package com.example.chatter.user.home.myjob

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatter.model.Job
import com.example.chatter.model.UserCV
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MyJobViewModel : ViewModel() {
    private val _userCv = MutableStateFlow<List<UserCV>>(emptyList())
    val userCv = _userCv.asStateFlow()

    private val _job = MutableStateFlow<Map<String, Job>>(emptyMap())
    val job = _job.asStateFlow()

    init {
        getUserCv()
    }

    private fun getUserCv() {
        val dbRef = FirebaseDatabase.getInstance().getReference("usercv")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<UserCV>()
                for (child in snapshot.children) {
                    val cv = child.getValue(UserCV::class.java)

                    if (cv != null) {
                        cv.id = child.key.toString()
                        list.add(cv)
                    }
                }
                _userCv.value = list

                getJob(list)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun getJob(listCv: List<UserCV>) {
        viewModelScope.launch {
            try {
                val jobId = listCv.map { it.jobId }.distinct()
                val jobs = mutableMapOf<String, Job>()

                jobId.forEach { jobid ->
                    if (jobId.isNotEmpty()) {
                        val snapshot =
                            FirebaseDatabase.getInstance().getReference("job").child(jobid).get()
                                .await()
                        val job = snapshot.getValue(Job::class.java)
                        if (job != null) {
                            jobs[jobid] = job
                        }
                    }
                }

                _job.value = jobs
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun deleteCv(CvId: String) {
        viewModelScope.launch {
            try {
                FirebaseDatabase.getInstance().getReference("usercv").child(CvId).removeValue()
                    .await()
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}