package com.example.chatter.hr.home.jobApply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatter.hr.AI.CVMatchingEngine
import com.example.chatter.hr.AI.MatchResult
import com.example.chatter.model.Job
import com.example.chatter.model.UserCV
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JobApplyViewModel : ViewModel() {
    private val _userCvs = MutableStateFlow<List<UserCV>>(emptyList())
    val userCV = _userCvs.asStateFlow()

    private val _job = MutableStateFlow<Map<String, Job>>(emptyMap())
    val job = _job.asStateFlow()

    private val _matchResults = MutableStateFlow<List<MatchResult>>(emptyList())
    val matchResults = _matchResults.asStateFlow()

    private val dbRef = FirebaseDatabase.getInstance().getReference("usercv")
    private val jobRef = FirebaseDatabase.getInstance().getReference("job")

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

                loadJobsAndMatchCVs(list)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun loadJobsAndMatchCVs(cvList: List<UserCV>) {
        val jobIds = cvList.map { it.jobId }.distinct()
        if (jobIds.isEmpty()) return

        val jobMap = mutableMapOf<String, Job>()
        var loadedJobCount = 0

        jobIds.forEach { jobId ->
            jobRef.child(jobId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val job = snapshot.getValue(Job::class.java)
                    if (job != null) {
                        job.id = snapshot.key ?: jobId
                        jobMap[jobId] = job
                    }

                    loadedJobCount++

                    if (loadedJobCount >= jobIds.size) {
                        _job.value = jobMap
                        performMatching(cvList, jobMap)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Job loading cancelled: ${error.message}")
                }
            })
        }
    }

    private fun performMatching(cvList: List<UserCV>, jobMap: Map<String, Job>) {
        viewModelScope.launch {
            val allResults = mutableListOf<MatchResult>()

            cvList.forEach { cv ->
                val jobId = cv.jobId
                val job = jobMap[jobId]

                if (job != null) {
                    val matchResult = CVMatchingEngine.match(cv, job)
                    allResults.add(matchResult)
                } else {
                    allResults.add(MatchResult(cv, 0.0))
                }
            }

            _matchResults.value = allResults.sortedByDescending { it.score }
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