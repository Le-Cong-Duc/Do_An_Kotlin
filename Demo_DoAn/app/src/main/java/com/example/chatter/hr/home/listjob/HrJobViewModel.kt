package com.example.chatter.hr.home.listjob

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatter.model.Job
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class HrJobViewModel : ViewModel() {
    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val job = _jobs.asStateFlow()

    private val dbRef = FirebaseDatabase.getInstance().getReference("job")

    fun getJob() {
        viewModelScope.launch {
            callbackFlow {
                val listener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val jobs = mutableListOf<Job>()
                        for (child in snapshot.children) {
                            child.getValue(Job::class.java)?.let {
                                jobs.add(it)
                            }
                        }
                        trySend(jobs)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        close(error.toException())
                    }
                }

                dbRef.addValueEventListener(listener)

                awaitClose {
                    dbRef.removeEventListener(listener)
                }
            }.collect { jobsList ->
                _jobs.value = jobsList
            }
        }
    }

    fun addJob(job: Job) {
        viewModelScope.launch {
            try {
                val jobId = if (job.id.isNullOrBlank()) UUID.randomUUID().toString() else job.id

                val updatedJob = job.copy(id = jobId)

                if (jobId != null) {
                    dbRef.child(jobId).setValue(updatedJob).await()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteJob(jobId: String) {
        viewModelScope.launch {
            try {
                dbRef.child(jobId).removeValue().await()
            } catch (e: Exception) {
                // Xử lý lỗi nếu cần
                e.printStackTrace()
            }
        }
    }
}