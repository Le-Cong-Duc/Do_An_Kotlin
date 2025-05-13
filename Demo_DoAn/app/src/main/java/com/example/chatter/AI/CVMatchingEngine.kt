package com.example.chatter.AI

import com.example.chatter.model.Job
import com.example.chatter.model.UserCV

object CVMatchingEngine {

    // Make this function public so it can be called directly
    fun match(cv: UserCV, job: Job): MatchResult {
        var score = 0.0

        // Match skills
        val commonSkills = cv.skills.intersect(job.skills ?: emptyList())
        val skillScore = if ((job.skills?.size ?: 0) > 0)
            (commonSkills.size.toDouble() / (job.skills?.size ?: 1)) * 40 else 0.0
        score += skillScore

        // Match experience
        if (cv.experience?.isNotBlank() == true && job.experience?.isNotBlank() == true) {
            if (cv.experience.equals(job.experience, ignoreCase = true)) {
                score += 20
            } else {
                // Check years of experience
                val cvYears = extractYearsOfExperience(cv.experience)
                val jobYears = extractYearsOfExperience(job.experience ?: "")
                if (cvYears >= jobYears && jobYears > 0) {
                    score += 15
                } else if (cvYears > 0 && jobYears > 0) {
                    score += 10 * (cvYears.toDouble() / jobYears)
                }
            }
        }

        // Match education
        if (cv.education?.isNotBlank() == true && job.education?.isNotBlank() == true) {
            if (cv.education.equals(job.education, ignoreCase = true)) {
                score += 15
            } else if (cv.education.contains(job.education ?: "", ignoreCase = true) ||
                (job.education ?: "").contains(cv.education ?: "", ignoreCase = true)
            ) {
                score += 10
            }
        }

        // Match job title
        if (!job.title.isNullOrBlank() && !cv.jobTitle.isNullOrBlank()) {
            if (cv.jobTitle?.contains(job.title, ignoreCase = true) == true ||
                job.title.contains(cv.jobTitle ?: "", ignoreCase = true)
            ) {
                score += 15
            }
        }

        // Round the score to 2 decimal places
        val roundedScore = (score * 100).toInt() / 100.0

        return MatchResult(
            userCV = cv,
            score = roundedScore
        )
    }

    fun matchMultipleCVs(cvs: List<UserCV>, job: Job): List<MatchResult> {
        return cvs.map { match(it, job) }.sortedByDescending { it.score }
    }

    fun suggestJobsForCV(cv: UserCV, jobs: List<Job>): List<MatchResult> {
        return jobs.map { match(cv, it) }.sortedByDescending { it.score }
    }

    private fun extractYearsOfExperience(expText: String): Int {
        // Regex to extract years of experience
        val regex = "(\\d+)\\s*(?:năm|years|year)".toRegex(RegexOption.IGNORE_CASE)
        val matchResult = regex.find(expText)
        return matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }
}