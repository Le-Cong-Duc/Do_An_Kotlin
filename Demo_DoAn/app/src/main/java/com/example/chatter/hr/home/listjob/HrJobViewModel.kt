package com.example.chatter.hr.home.listjob

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatter.model.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HrJobViewModel : ViewModel() {
    private val repository = HrJobRepository()

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadJobs()
    }

    private fun loadJobs() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllJobs()
                .catch { e ->
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
                .collect { jobsList ->
                    _jobs.value = jobsList
                    _isLoading.value = false
                }
        }
    }

    fun addJob(job: Job) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.saveJob(job)
                .onSuccess {
                    _errorMessage.value = null
                }
                .onFailure { e ->
                    _errorMessage.value = "Không thể đăng tin: ${e.message}"
                }
            _isLoading.value = false
        }
    }

    fun updateJob(job: Job) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.saveJob(job)
                .onSuccess {
                    _errorMessage.value = null
                }
                .onFailure { e ->
                    _errorMessage.value = "Không thể cập nhật tin: ${e.message}"
                }
            _isLoading.value = false
        }
    }

    fun deleteJob(jobId: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.deleteJob(jobId)
                .onSuccess {
                    _errorMessage.value = null
                }
                .onFailure { e ->
                    _errorMessage.value = "Không thể xóa tin: ${e.message}"
                }
            _isLoading.value = false
        }
    }
}