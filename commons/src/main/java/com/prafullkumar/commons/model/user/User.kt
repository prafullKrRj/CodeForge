package com.prafullkumar.commons.model.user

data class UserProfile(
    val userId: String,
    val personalInfo: PersonalInformation,
    val professionalDetails: ProfessionalProfile,
    val socialConnections: SocialConnections,
    val skills: SkillSet,
    val privacySettings: PrivacyPreferences = PrivacyPreferences()
)

data class PersonalInformation(
    val fullName: String,
    val email: String,
    val phoneNumber: String? = null,
    val dateOfBirth: Long? = null,
    val location: Location,
    val profilePictureUrl: String? = null
)

data class Location(
    val city: String,
    val country: String,
    val coordinates: Coordinates? = null
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

data class ProfessionalProfile(
    val currentRole: JobRole,
    val workExperiences: List<WorkExperience> = emptyList(),
    val education: List<EducationDetail> = emptyList(),
    val certifications: List<Certification> = emptyList()
)

data class JobRole(
    val title: String? = null,
    val company: String? = null,
    val employmentType: EmploymentType? = null,
    val startDate: Long? = null,
)

enum class EmploymentType {
    FULL_TIME, PART_TIME, FREELANCE, CONTRACT, INTERNSHIP, UNEMPLOYED
}

data class WorkExperience(
    val company: String,
    val role: String,
    val startDate: Long,
    val endDate: Long? = null,
    val currentlyWorking: Boolean = false,
    val technologies: List<String> = emptyList(),
    val achievements: List<String>? = null
)

data class EducationDetail(
    val institution: String,
    val degree: String,
    val fieldOfStudy: String,
    val startDate: Long,
    val endDate: Long,
    val stillStudying: Boolean = false
)

data class Certification(
    val name: String,
    val issuingOrganization: String,
    val issueDate: Long,
    val expirationDate: Long? = null,
    val credentialUrl: String? = null
)

data class SkillSet(
    val programmingLanguages: List<ProgrammingSkill> = emptyList(),
    val frameworks: List<FrameworkSkill> = emptyList(),
    val softSkills: List<String> = emptyList(),
    val toolsAndTechnologies: List<String> = emptyList()
)

data class ProgrammingSkill(
    val language: String,
    val proficiencyLevel: SkillLevel? = null
)

data class FrameworkSkill(
    val framework: String,
    val proficiencyLevel: SkillLevel? = null
)

enum class SkillLevel {
    BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
}

data class SocialConnections(
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val techCommunities: List<String> = emptyList()
)

data class PrivacyPreferences(
    val profileVisibility: VisibilityLevel = VisibilityLevel.VISIBLE,
    val contactInfoVisibility: VisibilityLevel = VisibilityLevel.VISIBLE,
    val connectRequestPreference: ConnectionRequestPreference = ConnectionRequestPreference.OPEN
)

enum class VisibilityLevel {
    VISIBLE, HIDDEN, LIMITED
}

enum class ConnectionRequestPreference {
    OPEN, RESTRICTED, INVITE_ONLY
}
