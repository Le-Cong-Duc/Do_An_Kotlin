package com.example.chatter.hr.AI

import com.example.chatter.model.Job
import com.example.chatter.model.UserCV

object CVMatchingEngine {

    fun match(cv: UserCV, job: Job): MatchResult {
        var score = 0.0

        val commonSkills = cv.skills.intersect((job.skills ?: emptyList()).toSet())
        val skillScore = if ((job.skills?.size ?: 0) > 0)
            (commonSkills.size.toDouble() / (job.skills?.size ?: 1)) * 40 else 0.0
        score += skillScore

        if (cv.experience.isNotBlank() && job.experience?.isNotBlank() == true) {
            if (cv.experience.equals(job.experience, ignoreCase = true)) {
                score += 20
            } else {
                val cvYears = extractYearsOfExperience(cv.experience)
                val jobYears = extractYearsOfExperience(job.experience)
                if (jobYears in 1..cvYears) {
                    score += 15
                } else if (cvYears > 0 && jobYears > 0) {
                    score += 10 * (cvYears.toDouble() / jobYears)
                }
            }
        }

        if (cv.education.isNotBlank() && job.education?.isNotBlank() == true) {
            if (cv.education.equals(job.education, ignoreCase = true)) {
                score += 15
            } else if (cv.education.contains(job.education, ignoreCase = true) ||
                (job.education ).contains(cv.education, ignoreCase = true)
            ) {
                score += 10
            }
        }

        if (!job.title.isNullOrBlank() && cv.jobTitle.isNotBlank()) {
            if (cv.jobTitle.contains(job.title, ignoreCase = true) || job.title.contains(cv.jobTitle , ignoreCase = true)
            ) {
                score += 15
            }
        }

        val roundedScore = (score * 100).toInt() / 100.0

        return MatchResult(
            userCV = cv,
            score = roundedScore
        )
    }

    private fun extractYearsOfExperience(expText: String): Int {
        // Regex to extract years of experience
        val regex = "(\\d+)\\s*(?:năm|years|year)".toRegex(RegexOption.IGNORE_CASE)
        val matchResult = regex.find(expText)
        return matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }
}