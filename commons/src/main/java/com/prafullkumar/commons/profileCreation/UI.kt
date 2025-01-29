package com.prafullkumar.commons.profileCreation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CircularProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevSocialMediaOnboarding(
    viewModel: ProfileCreationViewModel,
    navigateToHomeScreen: () -> Unit = {}
) {
    val savingLoading by viewModel.savingLoading.collectAsState()
    val navigateToNextScreen by viewModel.navigateToNextScreen.collectAsState()
    LaunchedEffect(navigateToNextScreen) {
        if (navigateToNextScreen) {
            navigateToHomeScreen()
        }
    }

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Enter Details") })
        },
    ) { innerPadding ->
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item("profileDetails") {
                    ProfileDetailsSection(viewModel)
                }
                item("skills") {
                    SkillsSection(viewModel)
                }
                item {
                    SaveButton(viewModel)
                }
            }
            if (savingLoading) {
                CircularProgressIndicator()
            }
        }

    }
}

@Composable
fun SaveButton(viewModel: ProfileCreationViewModel) {
    val saveButtonEnabled by viewModel.saveButtonEnabled.collectAsState()
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        FilledTonalButton(
            enabled = saveButtonEnabled,
            modifier = Modifier
                .padding(16.dp)
                .size(100.dp),
            shape = RoundedCornerShape(10),
            onClick = {
                viewModel.save()
            }
        ) {
            Text("Save")
        }
    }
}

@Composable
fun ProfileDetailsSection(viewModel: ProfileCreationViewModel) {
    Heading("Profile Details")
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            label = { Text("Full Name") },
            value = viewModel.fullName,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.personalDetailsColors(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            shape = RoundedCornerShape(25)
        )
        OutlinedTextField(
            label = {
                Text("Email")
            },
            value = viewModel.email,
            onValueChange = {},
            colors = OutlinedTextFieldDefaults.personalDetailsColors(),
            enabled = viewModel.email.isEmpty(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            shape = RoundedCornerShape(25)
        )
        OutlinedTextField(
            label = {
                Text("Phone Number")
            },
            value = viewModel.phoneNumber,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.personalDetailsColors(),
            shape = RoundedCornerShape(25)
        )

    }
}

@Composable
fun OutlinedTextFieldDefaults.personalDetailsColors(): TextFieldColors {
    return colors(
        disabledContainerColor = MaterialTheme.colorScheme.surfaceBright,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
        disabledBorderColor = MaterialTheme.colorScheme.surfaceBright,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
        disabledTextColor = MaterialTheme.colorScheme
            .onSurface,
        disabledLabelColor = MaterialTheme.colorScheme
            .onSurface,
        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
        focusedBorderColor = MaterialTheme.colorScheme.surfaceBright,
        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceBright,
    )
}

@Composable
fun Heading(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .padding(12.dp),
        style = MaterialTheme.typography.headlineLarge
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillsSection(viewModel: ProfileCreationViewModel) {
    Heading("Skills")
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SkillSectionItem(
            "Languages",
            viewModel.selectedLanguages,
            suggestedLanguages,
            viewModel::toggleLanguage,
        )
        SkillSectionItem(
            "Frameworks",
            viewModel.selectedFrameworks,
            suggestedFrameworks,
            viewModel::toggleFramework
        )
        SkillSectionItem(
            "Tools",
            viewModel.selectedTools,
            suggestedTools,
            viewModel::toggleTool
        )
        SkillSectionItem(
            "Soft Skills",
            viewModel.selectedTools,
            suggestedSoftSkills,
            viewModel::toggleSoftSkill
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillSectionItem(
    type: String,
    skills: List<String>,
    totalSkills: List<String>,
    toggle: (String) -> Unit = {}
) {
    Text(type, style = MaterialTheme.typography.titleLarge)
    LazyRow(Modifier.fillMaxWidth()) {
        item {
            FlowRow(
                Modifier.fillMaxWidth(),
                maxItemsInEachRow = 6,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                totalSkills.forEach { language ->
                    Chip(
                        onClick = {
                            toggle(language)
                        },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(language)
                                if (skills.contains(language)) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove",
                                        Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = if (skills.contains(language)) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceContainer
                            },
                        ),
                    )
                }
            }
        }
    }
}