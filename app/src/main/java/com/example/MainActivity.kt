package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ScreenDestination
import com.example.model.UserRole
import com.example.ui.components.DemoScreenDrawer
import com.example.ui.components.SmaranBottomNav
import com.example.ui.components.SmaranTopBar
import com.example.ui.screens.AiChatScreen
import com.example.ui.screens.AshaVisitScreen
import com.example.ui.screens.CaregiverDashboardScreen
import com.example.ui.screens.CognitiveTrendScreen
import com.example.ui.screens.GamesMenuScreen
import com.example.ui.screens.LanguageAccessScreen
import com.example.ui.screens.LoginOtpScreen
import com.example.ui.screens.ManageRemindersScreen
import com.example.ui.screens.MemoryMatchScreen
import com.example.ui.screens.MyFamilyScreen
import com.example.ui.screens.OrientationQuizScreen
import com.example.ui.screens.PatientHomeScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SosAlertScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.SmaranTheme
import com.example.ui.viewmodel.SmaranViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SmaranViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val highContrast by viewModel.highContrast.collectAsStateWithLifecycle()
            val fontScale by viewModel.fontScale.collectAsStateWithLifecycle()

            SmaranTheme(
                highContrast = highContrast,
                fontScale = fontScale
            ) {
                SmaranMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SmaranMainApp(viewModel: SmaranViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val fontScale by viewModel.fontScale.collectAsStateWithLifecycle()
    val highContrast by viewModel.highContrast.collectAsStateWithLifecycle()
    val voiceReading by viewModel.voiceReadingEnabled.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val currentMood by viewModel.currentMood.collectAsStateWithLifecycle()

    val reminders by viewModel.reminders.collectAsStateWithLifecycle()
    val quizResults by viewModel.quizResults.collectAsStateWithLifecycle()
    val ashaVisits by viewModel.ashaVisits.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGenerating.collectAsStateWithLifecycle()
    val geminiApiKey by viewModel.geminiApiKey.collectAsStateWithLifecycle()
    val selectedModel by viewModel.selectedModel.collectAsStateWithLifecycle()

    val sosActive by viewModel.sosActive.collectAsStateWithLifecycle()
    val sosSent by viewModel.sosMessageSent.collectAsStateWithLifecycle()
    val isDrawerOpen by viewModel.isDrawerOpen.collectAsStateWithLifecycle()

    // Memory Game
    val memoryCards by viewModel.memoryCards.collectAsStateWithLifecycle()
    val matchMoves by viewModel.matchMoves.collectAsStateWithLifecycle()
    val matchedPairsCount by viewModel.matchedPairsCount.collectAsStateWithLifecycle()
    val gameDifficulty by viewModel.gameDifficulty.collectAsStateWithLifecycle()
    val gameCompleted by viewModel.gameCompleted.collectAsStateWithLifecycle()
    val gameSeconds by viewModel.gameSecondsElapsed.collectAsStateWithLifecycle()

    // Quiz
    val quizIndex by viewModel.quizCurrentIndex.collectAsStateWithLifecycle()
    val quizScore by viewModel.quizScore.collectAsStateWithLifecycle()
    val quizSelectedOption by viewModel.quizSelectedOption.collectAsStateWithLifecycle()
    val quizAnswerChecked by viewModel.quizAnswerChecked.collectAsStateWithLifecycle()
    val quizCompleted by viewModel.quizCompleted.collectAsStateWithLifecycle()

    // Handle back button
    BackHandler(enabled = currentScreen != ScreenDestination.PATIENT_HOME && currentScreen != ScreenDestination.SPLASH && currentScreen != ScreenDestination.LOGIN_OTP) {
        viewModel.navigateBack()
    }

    val showTopBar = currentScreen != ScreenDestination.SPLASH && currentScreen != ScreenDestination.LOGIN_OTP
    val showBottomNav = currentScreen != ScreenDestination.SPLASH &&
            currentScreen != ScreenDestination.LOGIN_OTP &&
            currentScreen != ScreenDestination.SOS_ALERT

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showTopBar) {
                val title = when (currentScreen) {
                    ScreenDestination.SPLASH -> "Smaran"
                    ScreenDestination.LANGUAGE_ACCESS -> "Language & Access"
                    ScreenDestination.LOGIN_OTP -> "Role Login"
                    ScreenDestination.PATIENT_HOME -> "Smaran"
                    ScreenDestination.GAMES_MENU -> "Brain Exercises"
                    ScreenDestination.MEMORY_MATCH -> "Memory Match"
                    ScreenDestination.ORIENTATION_QUIZ -> "Orientation Quiz"
                    ScreenDestination.REMINDERS -> "Medicines"
                    ScreenDestination.AI_CHAT -> "Smaran AI"
                    ScreenDestination.MY_FAMILY -> "My Family"
                    ScreenDestination.SOS_ALERT -> "Emergency SOS"
                    ScreenDestination.CAREGIVER_DASHBOARD -> "Caregiver Dashboard"
                    ScreenDestination.COGNITIVE_TREND -> "Cognitive Trends"
                    ScreenDestination.MANAGE_REMINDERS -> "Manage Medicines"
                    ScreenDestination.ASHA_FIELD_VISIT -> "ASHA Portal"
                    ScreenDestination.SETTINGS_API -> "Settings"
                }

                val showBack = currentScreen != ScreenDestination.PATIENT_HOME &&
                        currentScreen != ScreenDestination.CAREGIVER_DASHBOARD &&
                        currentScreen != ScreenDestination.ASHA_FIELD_VISIT

                SmaranTopBar(
                    title = title,
                    currentScreen = currentScreen,
                    currentRole = currentRole,
                    voiceReadingEnabled = voiceReading,
                    onBack = if (showBack) { { viewModel.navigateBack() } } else null,
                    onToggleVoice = { viewModel.setVoiceReading(!voiceReading) },
                    onOpenNavigator = { viewModel.toggleDrawer() },
                    onRoleClick = { viewModel.navigateTo(ScreenDestination.SETTINGS_API) }
                )
            }
        },
        bottomBar = {
            if (showBottomNav) {
                SmaranBottomNav(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) },
                    onOpenNavigator = { viewModel.toggleDrawer() }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                ScreenDestination.SPLASH -> {
                    SplashScreen(
                        onBeginClick = { viewModel.navigateTo(ScreenDestination.LOGIN_OTP) },
                        onLanguageClick = { viewModel.navigateTo(ScreenDestination.LANGUAGE_ACCESS) },
                        onDirectLoginClick = { viewModel.navigateTo(ScreenDestination.LOGIN_OTP) }
                    )
                }

                ScreenDestination.LANGUAGE_ACCESS -> {
                    LanguageAccessScreen(
                        currentLanguage = currentLanguage,
                        fontScale = fontScale,
                        highContrast = highContrast,
                        voiceReading = voiceReading,
                        onSelectLanguage = { viewModel.setLanguage(it) },
                        onSelectFontScale = { viewModel.setFontScale(it) },
                        onToggleHighContrast = { viewModel.setHighContrast(it) },
                        onToggleVoiceReading = { viewModel.setVoiceReading(it) },
                        onTestVoice = {
                            viewModel.ttsHelper.playGentleChime()
                            viewModel.ttsHelper.speak("Namaste! Smaran voice reading is activated.", currentLanguage.code)
                        },
                        onContinue = { viewModel.navigateTo(ScreenDestination.PATIENT_HOME) }
                    )
                }

                ScreenDestination.LOGIN_OTP -> {
                    LoginOtpScreen(
                        currentRole = currentRole,
                        onRoleSelected = { viewModel.selectRole(it) },
                        onLoginSuccess = { viewModel.loginWithRole(it) },
                        onRegisterSuccess = { name, role, phone, lang ->
                            viewModel.registerUser(name, role, phone, lang)
                        }
                    )
                }

                ScreenDestination.PATIENT_HOME -> {
                    PatientHomeScreen(
                        patientName = userName,
                        currentLanguage = currentLanguage,
                        currentMood = currentMood,
                        reminders = reminders,
                        onSelectMood = { viewModel.selectMood(it) },
                        onLanguageChange = { viewModel.setLanguage(it) },
                        onOpenLanguageSettings = { viewModel.navigateTo(ScreenDestination.LANGUAGE_ACCESS) },
                        onOpenGames = { viewModel.navigateTo(ScreenDestination.GAMES_MENU) },
                        onOpenMedicines = { viewModel.navigateTo(ScreenDestination.REMINDERS) },
                        onOpenAiChat = { viewModel.navigateTo(ScreenDestination.AI_CHAT) },
                        onOpenFamily = { viewModel.navigateTo(ScreenDestination.MY_FAMILY) },
                        onOpenSos = {
                            viewModel.triggerSos()
                            viewModel.navigateTo(ScreenDestination.SOS_ALERT)
                        },
                        onPlayMemoryGame = {
                            viewModel.resetMemoryGame("Gentle 2x2")
                            viewModel.navigateTo(ScreenDestination.MEMORY_MATCH)
                        },
                        onSpeakText = { text ->
                            viewModel.ttsHelper.playGentleChime()
                            viewModel.ttsHelper.speak(text, currentLanguage.code)
                        },
                        onToggleMedicine = { id, taken ->
                            viewModel.toggleMedicine(id, taken)
                        }
                    )
                }

                ScreenDestination.GAMES_MENU -> {
                    GamesMenuScreen(
                        onPlayMemoryMatch = {
                            viewModel.resetMemoryGame("Standard 2x3")
                            viewModel.navigateTo(ScreenDestination.MEMORY_MATCH)
                        },
                        onStartOrientationQuiz = {
                            viewModel.restartQuiz()
                            viewModel.navigateTo(ScreenDestination.ORIENTATION_QUIZ)
                        },
                        onOpenFamilyFaces = { viewModel.navigateTo(ScreenDestination.MY_FAMILY) },
                        onPlayBihuAudio = {
                            viewModel.ttsHelper.playGentleChime()
                            viewModel.ttsHelper.speak("O Mur Apunar Desh. Let the gentle Bihu flute bring peace to your mind.", currentLanguage.code)
                        }
                    )
                }

                ScreenDestination.MEMORY_MATCH -> {
                    MemoryMatchScreen(
                        cards = memoryCards,
                        moves = matchMoves,
                        matchedPairsCount = matchedPairsCount,
                        elapsedSeconds = gameSeconds,
                        difficulty = gameDifficulty,
                        isCompleted = gameCompleted,
                        onCardClicked = { viewModel.onCardClicked(it) },
                        onSelectDifficulty = { viewModel.resetMemoryGame(it) },
                        onRestartGame = { viewModel.resetMemoryGame(gameDifficulty) },
                        onBackToGames = { viewModel.navigateTo(ScreenDestination.GAMES_MENU) }
                    )
                }

                ScreenDestination.ORIENTATION_QUIZ -> {
                    OrientationQuizScreen(
                        currentIndex = quizIndex,
                        score = quizScore,
                        selectedOption = quizSelectedOption,
                        answerChecked = quizAnswerChecked,
                        isCompleted = quizCompleted,
                        onAnswerSelected = { idx, correct -> viewModel.answerQuizQuestion(idx, correct) },
                        onNextQuestion = { total -> viewModel.nextQuizQuestion(total) },
                        onRestartQuiz = { viewModel.restartQuiz() },
                        onBackToGames = { viewModel.navigateTo(ScreenDestination.GAMES_MENU) }
                    )
                }

                ScreenDestination.REMINDERS -> {
                    RemindersScreen(
                        reminders = reminders,
                        onToggleTaken = { id, taken -> viewModel.toggleMedicine(id, taken) },
                        onAddMedicine = { name, dose, time, meal, cat -> viewModel.addMedicineReminder(name, dose, time, meal, cat) },
                        onDeleteMedicine = { id -> viewModel.deleteMedicineReminder(id) }
                    )
                }

                ScreenDestination.AI_CHAT -> {
                    AiChatScreen(
                        messages = chatMessages,
                        isGenerating = isGenerating,
                        geminiApiKey = geminiApiKey,
                        selectedModel = selectedModel,
                        currentLanguage = currentLanguage,
                        onLanguageChange = { viewModel.setLanguage(it) },
                        onSendMessage = { viewModel.sendChatMessage(it) },
                        onSpeakMessage = { viewModel.speakAloud(it) },
                        onSaveApiKey = { viewModel.setApiKey(it) },
                        onSelectModel = { viewModel.setSelectedModel(it) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                ScreenDestination.MY_FAMILY -> {
                    MyFamilyScreen(
                        familyMembers = viewModel.familyMembers,
                        onPlayVoiceClip = { clipText ->
                            viewModel.ttsHelper.playGentleChime()
                            viewModel.ttsHelper.speak(clipText, currentLanguage.code)
                        }
                    )
                }

                ScreenDestination.SOS_ALERT -> {
                    SosAlertScreen(
                        sosActive = sosActive,
                        sosSent = sosSent,
                        onTriggerSos = { viewModel.triggerSos() },
                        onSendAlertSms = { viewModel.sendSosAlert() },
                        onDismissSos = {
                            viewModel.dismissSos()
                            viewModel.navigateTo(ScreenDestination.PATIENT_HOME)
                        }
                    )
                }

                ScreenDestination.CAREGIVER_DASHBOARD -> {
                    CaregiverDashboardScreen(
                        quizResults = quizResults,
                        onOpenCognitiveTrend = { viewModel.navigateTo(ScreenDestination.COGNITIVE_TREND) },
                        onOpenManageReminders = { viewModel.navigateTo(ScreenDestination.MANAGE_REMINDERS) },
                        onOpenAshaVisit = { viewModel.navigateTo(ScreenDestination.ASHA_FIELD_VISIT) }
                    )
                }

                ScreenDestination.COGNITIVE_TREND -> {
                    CognitiveTrendScreen()
                }

                ScreenDestination.MANAGE_REMINDERS -> {
                    ManageRemindersScreen(
                        reminders = reminders,
                        onAddMedicine = { name, dose, time, meal, cat -> viewModel.addMedicineReminder(name, dose, time, meal, cat) },
                        onDeleteMedicine = { id -> viewModel.deleteMedicineReminder(id) }
                    )
                }

                ScreenDestination.ASHA_FIELD_VISIT -> {
                    AshaVisitScreen(
                        visits = ashaVisits,
                        onLogVisit = { sys, dia, p, s, sug, sup, notes ->
                            viewModel.logAshaVisit(sys, dia, p, s, sug, sup, notes)
                        },
                        onSyncNhm = { id -> viewModel.syncAshaToNhm(id) }
                    )
                }

                ScreenDestination.SETTINGS_API -> {
                    SettingsScreen(
                        currentLanguage = currentLanguage,
                        currentRole = currentRole,
                        fontScale = fontScale,
                        highContrast = highContrast,
                        voiceReading = voiceReading,
                        geminiApiKey = geminiApiKey,
                        selectedModel = selectedModel,
                        onSelectLanguage = { viewModel.setLanguage(it) },
                        onSelectRole = { viewModel.setRole(it) },
                        onSelectFontScale = { viewModel.setFontScale(it) },
                        onToggleHighContrast = { viewModel.setHighContrast(it) },
                        onToggleVoiceReading = { viewModel.setVoiceReading(it) },
                        onSaveApiKey = { viewModel.setApiKey(it) },
                        onSelectModel = { viewModel.setSelectedModel(it) },
                        onOpenScreenNavigator = { viewModel.toggleDrawer() },
                        onTestVoice = {
                            viewModel.ttsHelper.playGentleChime()
                            viewModel.ttsHelper.speak("Namaste Baruah Deuta! Voice reading is enabled.", currentLanguage.code)
                        },
                        onLogout = { viewModel.logout() }
                    )
                }
            }
        }
    }

    // Modal Navigator Drawer for all screens (matches Image 1)
    DemoScreenDrawer(
        isOpen = isDrawerOpen,
        currentScreen = currentScreen,
        onSelectScreen = { destination ->
            viewModel.navigateTo(destination)
        },
        onDismiss = { viewModel.closeDrawer() }
    )
}
