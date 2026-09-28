package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.entity.AshaVisitRecord
import com.example.data.entity.MedicineReminder
import com.example.data.entity.MemoryGameScore
import com.example.data.entity.OrientationQuizResult
import com.example.data.repository.SmaranRepository
import com.example.model.AppLanguage
import com.example.model.ChatMessage
import com.example.model.FamilyMember
import com.example.model.MessageSender
import com.example.model.ScreenDestination
import com.example.model.UserRole
import com.example.network.GeminiService
import com.example.service.TtsHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MemoryCard(
    val id: Int,
    val identifier: String,
    val iconEmoji: String,
    val title: String,
    val subtext: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)

class SmaranViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    val repository = SmaranRepository(database)
    val ttsHelper = TtsHelper(application)
    private val geminiService = GeminiService()

    // Navigation and Identity State
    private val _currentScreen = MutableStateFlow(ScreenDestination.LOGIN_OTP)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<ScreenDestination>()

    private val _currentRole = MutableStateFlow(UserRole.PATIENT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _userName = MutableStateFlow("Bhaben Baruah")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.ASSAMESE)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _fontScale = MutableStateFlow(1.0f)
    val fontScale: StateFlow<Float> = _fontScale.asStateFlow()

    private val _highContrast = MutableStateFlow(false)
    val highContrast: StateFlow<Boolean> = _highContrast.asStateFlow()

    private val _voiceReadingEnabled = MutableStateFlow(true)
    val voiceReadingEnabled: StateFlow<Boolean> = _voiceReadingEnabled.asStateFlow()

    private val _currentMood = MutableStateFlow("Peaceful")
    val currentMood: StateFlow<String> = _currentMood.asStateFlow()

    // Database flows
    val reminders: StateFlow<List<MedicineReminder>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizResults: StateFlow<List<OrientationQuizResult>> = repository.allQuizResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val gameScores: StateFlow<List<MemoryGameScore>> = repository.allGameScores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ashaVisits: StateFlow<List<AshaVisitRecord>> = repository.allVisits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // AI Chat State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = MessageSender.SMARAN_AI,
                text = com.example.util.SmaranStrings.getChatInitialGreeting(AppLanguage.ASSAMESE)
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _geminiApiKey = MutableStateFlow("")
    val geminiApiKey: StateFlow<String> = _geminiApiKey.asStateFlow()

    private val _selectedModel = MutableStateFlow("gemini-2.5-flash")
    val selectedModel: StateFlow<String> = _selectedModel.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    // SOS State
    private val _sosActive = MutableStateFlow(false)
    val sosActive: StateFlow<Boolean> = _sosActive.asStateFlow()

    private val _sosMessageSent = MutableStateFlow(false)
    val sosMessageSent: StateFlow<Boolean> = _sosMessageSent.asStateFlow()

    // Screen Picker Drawer State
    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

    // Memory Match Game State
    private val _memoryCards = MutableStateFlow<List<MemoryCard>>(emptyList())
    val memoryCards: StateFlow<List<MemoryCard>> = _memoryCards.asStateFlow()

    private val _selectedCardIndices = MutableStateFlow<List<Int>>(emptyList())
    val selectedCardIndices: StateFlow<List<Int>> = _selectedCardIndices.asStateFlow()

    private val _matchMoves = MutableStateFlow(0)
    val matchMoves: StateFlow<Int> = _matchMoves.asStateFlow()

    private val _matchedPairsCount = MutableStateFlow(0)
    val matchedPairsCount: StateFlow<Int> = _matchedPairsCount.asStateFlow()

    private val _gameDifficulty = MutableStateFlow("Standard 2x3")
    val gameDifficulty: StateFlow<String> = _gameDifficulty.asStateFlow()

    private val _gameCompleted = MutableStateFlow(false)
    val gameCompleted: StateFlow<Boolean> = _gameCompleted.asStateFlow()

    private val _gameSecondsElapsed = MutableStateFlow(0)
    val gameSecondsElapsed: StateFlow<Int> = _gameSecondsElapsed.asStateFlow()
    private var gameTimerJob: Job? = null

    // Orientation Quiz State
    private val _quizCurrentIndex = MutableStateFlow(0)
    val quizCurrentIndex: StateFlow<Int> = _quizCurrentIndex.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    private val _quizSelectedOption = MutableStateFlow<Int?>(null)
    val quizSelectedOption: StateFlow<Int?> = _quizSelectedOption.asStateFlow()

    private val _quizAnswerChecked = MutableStateFlow(false)
    val quizAnswerChecked: StateFlow<Boolean> = _quizAnswerChecked.asStateFlow()

    private val _quizCompleted = MutableStateFlow(false)
    val quizCompleted: StateFlow<Boolean> = _quizCompleted.asStateFlow()

    // Family Members
    val familyMembers = listOf(
        FamilyMember(
            id = "rahul",
            name = "Rahul Baruah",
            relation = "Son (Living with you)",
            bio = "Rahul works in Guwahati and returns home every evening by 6:00 PM. He loves having tea with you on the verandah.",
            voiceClipText = "Deuta, this is Rahul! I'll be home right by 6 PM. Have your afternoon tea and relax. Love you!",
            memoryPrompt = "Remember when Rahul graduated from Assam Engineering College and you bought him his first fountain pen?",
            iconEmoji = "👨‍💼"
        ),
        FamilyMember(
            id = "priya",
            name = "Priya Baruah",
            relation = "Daughter-in-law",
            bio = "Prepares your warm morning ginger tea, Khar, and evening snacks. She checks that your slippers are near your bedside.",
            voiceClipText = "Deuta, good morning! Your ginger tea and Telmisartan tablet are ready on the dining table.",
            memoryPrompt = "Priya made your favorite Khar and Maasor Tenga fish curry last Sunday.",
            iconEmoji = "👩‍🍳"
        ),
        FamilyMember(
            id = "meera",
            name = "Meera Baruah",
            relation = "Granddaughter (9 yrs)",
            bio = "Studies in Grade 4. She loves sitting with you to hear folk tales of Burhi Aair Xadhu and Kaziranga animals.",
            voiceClipText = "Koka! Look at the Rhino drawing I made today at school! Can you sing the Bihu song with me?",
            memoryPrompt = "Meera recited the entire Assamese nursery rhyme you taught her last week.",
            iconEmoji = "👧"
        ),
        FamilyMember(
            id = "pratima",
            name = "Pratima Baruah",
            relation = "Late Beloved Wife (48 yrs)",
            bio = "A gentle, loving presence. Married in Tezpur in 1974. Her framed portrait with fresh marigold garland rests in the prayer room.",
            voiceClipText = "Pratima's warm memory will always surround this home with unconditional peace and blessings.",
            memoryPrompt = "You both loved walking by the Brahmaputra river ghat on autumn evenings.",
            iconEmoji = "🌸"
        )
    )

    init {
        viewModelScope.launch {
            repository.ensureDefaultDataSeeded()
            resetMemoryGame("Standard 2x3")
        }
    }

    // Navigation functions
    fun navigateTo(destination: ScreenDestination) {
        if (_currentScreen.value != destination) {
            _screenHistory.add(_currentScreen.value)
            _currentScreen.value = destination
        }
        _isDrawerOpen.value = false
    }

    fun navigateBack(): Boolean {
        if (_screenHistory.isNotEmpty()) {
            _currentScreen.value = _screenHistory.removeAt(_screenHistory.size - 1)
            return true
        } else if (_currentScreen.value != ScreenDestination.PATIENT_HOME) {
            _currentScreen.value = ScreenDestination.PATIENT_HOME
            return true
        }
        return false
    }

    fun toggleDrawer() {
        _isDrawerOpen.value = !_isDrawerOpen.value
    }

    fun closeDrawer() {
        _isDrawerOpen.value = false
    }

    fun selectRole(role: UserRole) {
        _currentRole.value = role
        _userName.value = role.defaultName
    }

    fun loginWithRole(role: UserRole) {
        _currentRole.value = role
        _userName.value = role.defaultName
        when (role) {
            UserRole.PATIENT -> navigateTo(ScreenDestination.PATIENT_HOME)
            UserRole.CAREGIVER -> navigateTo(ScreenDestination.CAREGIVER_DASHBOARD)
            UserRole.ASHA_WORKER -> navigateTo(ScreenDestination.ASHA_FIELD_VISIT)
        }
    }

    fun setRole(role: UserRole) {
        loginWithRole(role)
    }

    fun registerUser(name: String, role: UserRole, phone: String, language: AppLanguage) {
        _userName.value = if (name.isNotBlank()) name else role.defaultName
        _currentRole.value = role
        _currentLanguage.value = language
        when (role) {
            UserRole.PATIENT -> navigateTo(ScreenDestination.PATIENT_HOME)
            UserRole.CAREGIVER -> navigateTo(ScreenDestination.CAREGIVER_DASHBOARD)
            UserRole.ASHA_WORKER -> navigateTo(ScreenDestination.ASHA_FIELD_VISIT)
        }
    }

    fun logout() {
        _screenHistory.clear()
        _currentScreen.value = ScreenDestination.LOGIN_OTP
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        _chatMessages.value = listOf(
            ChatMessage(
                sender = MessageSender.SMARAN_AI,
                text = com.example.util.SmaranStrings.getChatInitialGreeting(language)
            )
        )
    }

    fun setFontScale(scale: Float) {
        _fontScale.value = scale
    }

    fun setHighContrast(enabled: Boolean) {
        _highContrast.value = enabled
    }

    fun setVoiceReading(enabled: Boolean) {
        _voiceReadingEnabled.value = enabled
    }

    fun setApiKey(key: String) {
        _geminiApiKey.value = key
    }

    fun setSelectedModel(model: String) {
        _selectedModel.value = model
    }

    fun selectMood(mood: String) {
        _currentMood.value = mood
        viewModelScope.launch {
            repository.recordMood(mood)
            if (_voiceReadingEnabled.value) {
                ttsHelper.playGentleChime()
                val response = when (mood) {
                    "Peaceful" -> "It is wonderful to feel peaceful, Baruah Deuta. Enjoy the calm day."
                    "Okay" -> "You are doing well, Deuta. Take your time and relax."
                    else -> "We are right here with you, Deuta. Rahul and Priya are close by. You are safe."
                }
                ttsHelper.speak(response, _currentLanguage.value.code)
            }
        }
    }

    fun toggleMedicine(id: Int, currentTaken: Boolean) {
        viewModelScope.launch {
            repository.toggleMedicineTaken(id, currentTaken)
            ttsHelper.playGentleChime()
            if (!currentTaken && _voiceReadingEnabled.value) {
                ttsHelper.speak("Medicine marked as taken. Well done, Deuta.", _currentLanguage.value.code)
            }
        }
    }

    fun addMedicineReminder(name: String, dosage: String, time: String, meal: String, category: String) {
        viewModelScope.launch {
            repository.addReminder(
                MedicineReminder(
                    name = name,
                    dosage = dosage,
                    timeString = time,
                    mealTiming = meal,
                    category = category,
                    isTaken = false
                )
            )
            ttsHelper.playGentleChime()
        }
    }

    fun deleteMedicineReminder(id: Int) {
        viewModelScope.launch {
            repository.deleteReminder(id)
        }
    }

    // Memory Game functions
    fun resetMemoryGame(difficulty: String) {
        _gameDifficulty.value = difficulty
        _matchMoves.value = 0
        _matchedPairsCount.value = 0
        _gameCompleted.value = false
        _selectedCardIndices.value = emptyList()
        _gameSecondsElapsed.value = 0

        gameTimerJob?.cancel()
        gameTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _gameSecondsElapsed.value += 1
            }
        }

        val allItems = listOf(
            Triple("rhino", "🦏", "Kaziranga Rhino" to "গঁড়"),
            Triple("tea", "🍃", "Assam Tea Leaf" to "চাহ পাত"),
            Triple("gamusa", "🧣", "Assam Gamusa" to "গামোচা"),
            Triple("dhol", "🪕", "Bihu Dhol & Pepa" to "ঢোল আৰু পেঁপা"),
            Triple("lotus", "🪷", "Lotus Pond" to "পদ্ম ফুল"),
            Triple("cup", "☕", "Bell-Metal Cup" to "কাঁহৰ বাটি")
        )

        val pairCount = when (difficulty) {
            "Gentle 2x2" -> 2
            "Standard 2x3" -> 3
            else -> 6 // Full 3x4
        }

        val selectedSub = allItems.take(pairCount)
        val deck = mutableListOf<MemoryCard>()
        var cardId = 0

        selectedSub.forEach { item ->
            // First of pair
            deck.add(
                MemoryCard(
                    id = cardId++,
                    identifier = item.first,
                    iconEmoji = item.second,
                    title = item.third.first,
                    subtext = item.third.second
                )
            )
            // Second of pair
            deck.add(
                MemoryCard(
                    id = cardId++,
                    identifier = item.first,
                    iconEmoji = item.second,
                    title = item.third.first,
                    subtext = item.third.second
                )
            )
        }

        deck.shuffle()
        _memoryCards.value = deck
    }

    fun onCardClicked(cardIndex: Int) {
        val currentCards = _memoryCards.value.toMutableList()
        val currentSelected = _selectedCardIndices.value

        if (cardIndex !in currentCards.indices) return
        val card = currentCards[cardIndex]

        if (card.isFlipped || card.isMatched || currentSelected.size >= 2) {
            return
        }

        // Flip this card
        currentCards[cardIndex] = card.copy(isFlipped = true)
        val newSelected = currentSelected + cardIndex
        _memoryCards.value = currentCards
        _selectedCardIndices.value = newSelected

        if (newSelected.size == 2) {
            _matchMoves.value += 1
            val firstIdx = newSelected[0]
            val secondIdx = newSelected[1]

            val card1 = currentCards[firstIdx]
            val card2 = currentCards[secondIdx]

            if (card1.identifier == card2.identifier) {
                // Match found!
                ttsHelper.playGentleChime()
                viewModelScope.launch {
                    delay(300)
                    currentCards[firstIdx] = card1.copy(isMatched = true)
                    currentCards[secondIdx] = card2.copy(isMatched = true)
                    _memoryCards.value = currentCards
                    _selectedCardIndices.value = emptyList()
                    _matchedPairsCount.value += 1

                    val totalPairs = currentCards.size / 2
                    if (_matchedPairsCount.value >= totalPairs) {
                        _gameCompleted.value = true
                        gameTimerJob?.cancel()
                        val stars = if (_matchMoves.value <= totalPairs + 2) 3 else 2
                        repository.saveGameScore(
                            difficulty = _gameDifficulty.value,
                            moves = _matchMoves.value,
                            seconds = _gameSecondsElapsed.value,
                            stars = stars
                        )
                        if (_voiceReadingEnabled.value) {
                            ttsHelper.speak("Xabaax Baruah Deuta! Wonderful memory match! You did great.", _currentLanguage.value.code)
                        }
                    }
                }
            } else {
                // Not match, flip back after brief pause
                viewModelScope.launch {
                    delay(1000)
                    currentCards[firstIdx] = card1.copy(isFlipped = false)
                    currentCards[secondIdx] = card2.copy(isFlipped = false)
                    _memoryCards.value = currentCards
                    _selectedCardIndices.value = emptyList()
                }
            }
        }
    }

    // Orientation Quiz functions
    fun answerQuizQuestion(selectedIdx: Int, isCorrect: Boolean) {
        _quizSelectedOption.value = selectedIdx
        _quizAnswerChecked.value = true
        if (isCorrect) {
            _quizScore.value += 1
            ttsHelper.playGentleChime()
        }
    }

    fun nextQuizQuestion(totalQuestions: Int) {
        val next = _quizCurrentIndex.value + 1
        if (next < totalQuestions) {
            _quizCurrentIndex.value = next
            _quizSelectedOption.value = null
            _quizAnswerChecked.value = false
        } else {
            _quizCompleted.value = true
            viewModelScope.launch {
                repository.saveQuizResult(
                    score = _quizScore.value,
                    total = totalQuestions,
                    summary = "Daily check completed: ${_quizScore.value}/$totalQuestions correct"
                )
                if (_voiceReadingEnabled.value) {
                    ttsHelper.speak("Orientation check finished. Score ${_quizScore.value} out of $totalQuestions. Excellent work!", _currentLanguage.value.code)
                }
            }
        }
    }

    fun restartQuiz() {
        _quizCurrentIndex.value = 0
        _quizScore.value = 0
        _quizSelectedOption.value = null
        _quizAnswerChecked.value = false
        _quizCompleted.value = false
    }

    // Chat functions
    fun sendChatMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return

        val userMsg = ChatMessage(sender = MessageSender.USER, text = trimmed)
        _chatMessages.value = _chatMessages.value + userMsg

        _isGenerating.value = true
        viewModelScope.launch {
            val response = geminiService.generateCompanionResponse(
                userPrompt = trimmed,
                configuredKey = _geminiApiKey.value,
                modelName = _selectedModel.value,
                languageCode = _currentLanguage.value.code
            )

            val aiMsg = ChatMessage(sender = MessageSender.SMARAN_AI, text = response)
            _chatMessages.value = _chatMessages.value + aiMsg
            _isGenerating.value = false

            if (_voiceReadingEnabled.value) {
                ttsHelper.speak(response, _currentLanguage.value.code)
            }
        }
    }

    fun speakAloud(text: String) {
        ttsHelper.speak(text, _currentLanguage.value.code)
    }

    // SOS Functions
    fun triggerSos() {
        _sosActive.value = true
        ttsHelper.playSosAlertTone()
        if (_voiceReadingEnabled.value) {
            ttsHelper.speak("Emergency SOS initiated. Alerting son Rahul and ASHA worker Anjali Deka.", _currentLanguage.value.code)
        }
    }

    fun sendSosAlert() {
        _sosMessageSent.value = true
        ttsHelper.playGentleChime()
    }

    fun dismissSos() {
        _sosActive.value = false
        _sosMessageSent.value = false
        ttsHelper.stop()
    }

    // ASHA Visit Functions
    fun logAshaVisit(bpSys: Int, bpDia: Int, pulse: Int, spo2: Int, bloodSugar: String, supplyDays: Int, notes: String) {
        viewModelScope.launch {
            repository.addAshaVisit(
                AshaVisitRecord(
                    bpSystolic = bpSys,
                    bpDiastolic = bpDia,
                    pulse = pulse,
                    spo2 = spo2,
                    bloodSugar = bloodSugar,
                    medicineSupplyDays = supplyDays,
                    notes = notes,
                    isSyncedToNhm = true
                )
            )
            ttsHelper.playGentleChime()
        }
    }

    fun syncAshaToNhm(visitId: Int) {
        viewModelScope.launch {
            repository.markVisitSynced(visitId)
            ttsHelper.playGentleChime()
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsHelper.shutdown()
        gameTimerJob?.cancel()
    }
}
