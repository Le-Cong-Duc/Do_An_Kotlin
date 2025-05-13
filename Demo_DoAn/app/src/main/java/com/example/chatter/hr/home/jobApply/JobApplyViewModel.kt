package com.example.chatter.hr.home.jobApply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatter.AI.CVMatchingEngine
import com.example.chatter.AI.MatchResult
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

    private val _matchResults = MutableStateFlow<List<MatchResult>>(emptyList())
    val matchResults = _matchResults.asStateFlow()

    private val dbRef = FirebaseDatabase.getInstance().getReference("usercv")
    private val jobRef = FirebaseDatabase.getInstance().getReference("job")

    init {
        loadCVsAndJobs()
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

    private fun loadCVsAndJobs() {
        // First load all CVs
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cvList = mutableListOf<UserCV>()
                for (child in snapshot.children) {
                    val cv = child.getValue(UserCV::class.java)
                    if (cv != null) {
                        cv.id = child.key.toString()
                        cvList.add(cv)
                    }
                }
                _userCvs.value = cvList

                // After loading CVs, load all jobs
                loadJobsAndMatchCVs(cvList)
            }

            override fun onCancelled(error: DatabaseError) {
                println("CV loading cancelled: ${error.message}")
            }
        })
    }

    private fun loadJobsAndMatchCVs(cvList: List<UserCV>) {
        val jobIds = cvList.mapNotNull { it.jobId }.distinct()
        if (jobIds.isEmpty()) return

        // Create a map to store jobs by ID
        val jobMap = mutableMapOf<String, Job>()
        var loadedJobCount = 0

        // Load each job by ID
        jobIds.forEach { jobId ->
            jobRef.child(jobId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val job = snapshot.getValue(Job::class.java)
                    if (job != null) {
                        // Add job ID to the job object if needed
                        job.id = snapshot.key ?: jobId
                        jobMap[jobId] = job
                    }

                    loadedJobCount++

                    // When all jobs are loaded, update the state and match CVs
                    if (loadedJobCount >= jobIds.size) {
                        _job.value = jobMap
                        performMatching(cvList, jobMap)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Job loading cancelled: ${error.message}")
                    loadedJobCount++
                }
            })
        }
    }

    private fun performMatching(cvList: List<UserCV>, jobMap: Map<String, Job>) {
        viewModelScope.launch {
            val allResults = mutableListOf<MatchResult>()

            // Match each CV with its corresponding job
            cvList.forEach { cv ->
                val jobId = cv.jobId
                val job = jobMap[jobId]

                if (job != null) {
                    // Use the matching engine to calculate score
                    val matchResult = CVMatchingEngine.match(cv, job)
                    allResults.add(matchResult)
                } else {
                    // If job not found, add CV with zero score
                    allResults.add(MatchResult(cv, 0.0))
                }
            }

            // Sort results by score (highest first)
            _matchResults.value = allResults.sortedByDescending { it.score }
        }
    }

}