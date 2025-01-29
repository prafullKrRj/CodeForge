package com.prafullkumar.commons.profileCreation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.prafullkumar.commons.BaseClass
import com.prafullkumar.commons.model.user.FrameworkSkill
import com.prafullkumar.commons.model.user.JobRole
import com.prafullkumar.commons.model.user.Location
import com.prafullkumar.commons.model.user.PersonalInformation
import com.prafullkumar.commons.model.user.ProfessionalProfile
import com.prafullkumar.commons.model.user.ProgrammingSkill
import com.prafullkumar.commons.model.user.SkillSet
import com.prafullkumar.commons.model.user.SocialConnections
import com.prafullkumar.commons.model.user.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileCreationViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val repository: ProfileCreationRepository,
    private val context: Context
) : ViewModel() {

    var fullName by mutableStateOf(firebaseAuth.currentUser?.displayName ?: "")
    var email by mutableStateOf(firebaseAuth.currentUser?.email ?: "")
    var phoneNumber by mutableStateOf(firebaseAuth.currentUser?.phoneNumber ?: "")

    private val _navigateToNextScreen = MutableStateFlow(false)
    val navigateToNextScreen = _navigateToNextScreen.asStateFlow()

    private val _savingLoading = MutableStateFlow(false)
    val savingLoading = _savingLoading.asStateFlow()


    var selectedLanguages by mutableStateOf<List<String>>(emptyList())
    fun toggleLanguage(language: String) = selectedLanguages.toMutableList().apply {
        if (language in this) remove(language) else add(language)
    }.also { selectedLanguages = it }

    var selectedFrameworks by mutableStateOf<List<String>>(emptyList())
    fun toggleFramework(framework: String) = selectedFrameworks.toMutableList().apply {
        if (framework in this) remove(framework) else add(framework)
    }.also { selectedFrameworks = it }

    var selectedTools by mutableStateOf<List<String>>(emptyList())
    fun toggleTool(tool: String) = selectedTools.toMutableList().apply {
        if (tool in this) remove(tool) else add(tool)
    }.also { selectedTools = it }

    private val _saveButtonEnabled =
        MutableStateFlow(true)
    val saveButtonEnabled = _saveButtonEnabled.asStateFlow()

    var selectedSoftSkills by mutableStateOf<List<String>>(emptyList())
    fun toggleSoftSkill(skill: String) = selectedSoftSkills.toMutableList().apply {
        if (skill in this) remove(skill) else add(skill)
    }.also { selectedSoftSkills = it }

    fun save() {
        if (fullName.isEmpty() || (selectedLanguages.isEmpty() || selectedFrameworks.isEmpty())) {
            Toast.makeText(
                context, "Please fill all the required fields", Toast.LENGTH_SHORT
            ).show()
            return
        }
        _savingLoading.update { true }
        viewModelScope.launch {
            repository.insertData(
                UserProfile(
                    userId = firebaseAuth.currentUser?.uid ?: "",
                    personalInfo = PersonalInformation(
                        fullName = fullName,
                        email = email,
                        phoneNumber = phoneNumber,
                        location = Location(
                            city = "", country = ""
                        )
                    ),
                    skills = SkillSet(
                        programmingLanguages = selectedLanguages.map { ProgrammingSkill(it) },
                        frameworks = selectedFrameworks.map { FrameworkSkill(it) },
                        toolsAndTechnologies = selectedTools,
                        softSkills = selectedSoftSkills
                    ),
                    professionalDetails = ProfessionalProfile(
                        currentRole = JobRole()
                    ),
                    socialConnections = SocialConnections(),
                )
            ).collectLatest {
                when (it) {
                    is BaseClass.Error -> {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                null, "Error saving profile", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    BaseClass.Loading -> {
                        _savingLoading.update { true }
                        _saveButtonEnabled.update { false }
                    }

                    is BaseClass.Success -> {
                        _navigateToNextScreen.update { true }
                    }
                }
            }
        }
    }
}

val suggestedLanguages = listOf(
    "Java",
    "Kotlin",
    "Python",
    "JavaScript",
    "TypeScript",
    "C++",
    "Swift",
    "Go",
    "Rust",
    "Ruby",
    "C#"
)
val suggestedFrameworks = listOf(
    "React",
    "Spring",
    "Django",
    "Flutter",
    "Angular",
    "Vue.js",
    "Node.js",
    "Express",
    "Laravel",
    "Next.js"
)
val suggestedTools = listOf(
    "Docker",
    "Kubernetes",
    "Git",
    "Jenkins",
    "AWS",
    "Firebase",
    "Postman",
    "Jira",
    "Linux",
    "MongoDB"
)
val suggestedSoftSkills = listOf(
    "Communication", "Team Work", "Problem Solving", "Adaptability", "Leadership", "Time Management"
)