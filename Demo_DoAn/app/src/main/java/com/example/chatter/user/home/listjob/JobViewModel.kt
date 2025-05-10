package com.example.chatter.user.home.listjob

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatter.model.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JobViewModel : ViewModel() {
    private val repository = JobRepository()

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs


    init {
        loadJobs()
    }

    private fun loadJobs() {
        viewModelScope.launch {
            repository.getAllJobs()
                .collect { jobsList ->
                    _jobs.value = jobsList
                }
        }
    }


}