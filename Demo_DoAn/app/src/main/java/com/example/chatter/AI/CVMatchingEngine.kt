package com.example.chatter.AI

import com.example.chatter.model.Job
import com.example.chatter.model.UserCV

object CVMatchingEngine {

    private fun match(cv: UserCV, job: Job): MatchResult {
        var score = 0.0

        val commonSkills = cv.skills.intersect(job.skills ?: emptyList())

        val skillScore = if ((job.skills?.size ?: 0) > 0)
            (commonSkills.size.toDouble() / (job.skills?.size ?: 1)) * 40 else 0.0
        score += skillScore

        println("Job skills: ${job.skills}")
        println("CV skills: ${cv.skills}")
        println("Common skills: $commonSkills → Skill Score: $skillScore")

        if (cv.experience.isNotBlank() && job.experience?.isNotBlank() == true) {
            if (cv.experience.equals(job.experience, ignoreCase = true)) {
                score += 20
            } else {
                // Kiểm tra số năm kinh nghiệm
                val cvYears = extractYearsOfExperience(cv.experience)
                val jobYears = extractYearsOfExperience(job.experience ?: "")
                if (cvYears >= jobYears && jobYears > 0) {
                    score += 15
                } else if (cvYears > 0 && jobYears > 0) {
                    score += 10 * (cvYears.toDouble() / jobYears)
                }
            }
        }

        if (cv.education.isNotBlank() && job.education?.isNotBlank() == true) {
            if (cv.education.equals(job.education, ignoreCase = true)) {
                score += 15
            } else if (cv.education.contains(job.education ?: "", ignoreCase = true) ||
                (job.education ?: "").contains(cv.education, ignoreCase = true)
            ) {
                score += 10
            }
        }

        if (!job.title.isNullOrBlank() && cv.jobTitle.isNotBlank()) {
            if (cv.jobTitle.contains(job.title, ignoreCase = true) ||
                job.title.contains(cv.jobTitle, ignoreCase = true)
            ) {
                score += 15
            }
        }

        val roundedScore = "%.2f".format(score).toDouble()

        return MatchResult(
            userCV = cv,
            score = roundedScore,
        )
    }

    fun matchMultipleCVs(cvs: List<UserCV>, job: Job): List<MatchResult> {
        return cvs.map { match(it, job) }.sortedByDescending { it.score }
    }

    fun suggestJobsForCV(cv: UserCV, jobs: List<Job>): List<MatchResult> {
        return jobs.map { match(cv, it) }.sortedByDescending { it.score }
    }

    private fun extractYearsOfExperience(expText: String): Int {
        // Regex để trích xuất số năm kinh nghiệm
        val regex = "(\\d+)\\s*(?:năm|years|year)".toRegex(RegexOption.IGNORE_CASE)
        val matchResult = regex.find(expText)
        return matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }
}
