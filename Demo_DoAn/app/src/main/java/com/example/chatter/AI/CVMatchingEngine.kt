package com.example.chatter.AI

import com.example.chatter.model.Job
import com.example.chatter.model.UserCV


object CVMatchingEngine {

    fun match(cv: UserCV, job: Job): MatchResult {
        var score = 0.0

        // Kỹ năng trùng nhau
        val commonSkills = cv.skills.intersect(job.skills ?: emptyList())
        val skillScore = if ((job.skills?.size ?: 1) > 0)
            (commonSkills.size.toDouble() / (job.skills?.size ?: 1)) * 40 else 0.0
        score += skillScore

        // Kinh nghiệm
        if (!cv.experience.isNullOrBlank() && cv.experience.equals(job.experience, ignoreCase = true)) {
            score += 20
        }

        // Học vấn
        if (!cv.education.isNullOrBlank() && cv.education.equals(job.education, ignoreCase = true)) {
            score += 15
        }

        // Giới tính
        if (!job.gender.isNullOrBlank() && cv.gender.equals(job.gender, ignoreCase = true)) {
            score += 10
        }

        // Job title gần giống
        if (!job.title.isNullOrBlank() && cv.jobTitle.contains(job.title, ignoreCase = true)) {
            score += 15
        }

        return MatchResult(
            cvId = cv.id,
            jobId = job.id ?: "",
            score = "%.2f".format(score).toDouble()
        )
    }

    fun matchMultipleCVs(cvs: List<UserCV>, job: Job): List<MatchResult> {
        return cvs.map { match(it, job) }.sortedByDescending { it.score }
    }

    fun suggestJobsForCV(cv: UserCV, jobs: List<Job>): List<MatchResult> {
        return jobs.map { match(cv, it) }.sortedByDescending { it.score }
    }
}
