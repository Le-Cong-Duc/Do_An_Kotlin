package com.example.chatter.hr.home.listjob

import com.example.chatter.model.Job
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class HrJobRepository {
    private val database = FirebaseDatabase.getInstance()
    private val jobsRef = database.getReference("job")

    suspend fun saveJob(job: Job): Result<Job> {
        return try {
            jobsRef.child(job.id.toString()).setValue(job).await()
            Result.success(job)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllJobs(): Flow<List<Job>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jobs = mutableListOf<Job>()
                for (childSnapshot in snapshot.children) {
                    childSnapshot.getValue(Job::class.java)?.let {
                        jobs.add(it)
                    }
                }
                trySend(jobs)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        jobsRef.addValueEventListener(listener)

        awaitClose {
            jobsRef.removeEventListener(listener)
        }
    }

    suspend fun deleteJob(jobId: String?): Result<Unit> {
        return try {
            jobsRef.child(jobId.toString()).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJobById(jobId: Int): Result<Job> {
        return try {
            val snapshot = jobsRef.child(jobId.toString()).get().await()
            val job = snapshot.getValue(Job::class.java)
            if (job != null) {
                Result.success(job)
            } else {
                Result.failure(NoSuchElementException("No job found with ID: $jobId"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}