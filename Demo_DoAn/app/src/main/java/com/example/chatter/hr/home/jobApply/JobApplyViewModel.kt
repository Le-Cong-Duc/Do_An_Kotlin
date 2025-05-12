package com.example.chatter.hr.home.jobApply

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

class JobApplyViewModel : ViewModel() {
    private val _userCvs = MutableStateFlow<List<UserCV>>(emptyList())
    val userCV = _userCvs.asStateFlow()

    private val _job = MutableStateFlow<Map<String, Job>>(emptyMap())
    val job = _job.asStateFlow()

    val dbRef = FirebaseDatabase.getInstance().getReference("usercv")

    init {
        getUserCv()
    }

    private fun getUserCv() {
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
                _userCvs.value = list

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

    fun updateCvStatus(cvId: String, status: Int) {
        dbRef.child(cvId).child("status").setValue(status)
    }

    fun updateCvDate(cvId: String, date: String) {
        dbRef.child(cvId).child("date").setValue(date)
    }

    fun updateCvDateInterview(cvId: String, date: String) {
        dbRef.child(cvId).child("dateInterView").setValue(date)
    }
}