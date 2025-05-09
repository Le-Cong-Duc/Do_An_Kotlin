package com.example.chatter.user.home.listjob

import com.example.chatter.model.Job
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class JobRepository {
    private val database = FirebaseDatabase.getInstance()
    private val jobsRef = database.getReference("job")

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
}