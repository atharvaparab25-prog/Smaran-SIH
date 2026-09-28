package com.example.model

enum class AppLanguage(
    val displayName: String,
    val nativeName: String,
    val code: String,
    val region: String = "Northeast India"
) {
    ASSAMESE("Assamese", "অসমীয়া", "as", "Assam"),
    ENGLISH("English", "English", "en", "NER & Global"),
    BODO("Bodo", "बर'", "brx", "Bodoland / Assam"),
    MEITEI("Manipuri / Meitei", "মৈতৈলোন্", "mni", "Manipur"),
    BENGALI("Bengali", "বাংলা", "bn", "Barak Valley / Tripura"),
    KHASI("Khasi", "Khasi", "kha", "Meghalaya"),
    MIZO("Mizo", "Mizo", "lus", "Mizoram"),
    GARO("Garo", "A·chik", "grt", "Meghalaya"),
    HINDI("Hindi", "हिन्दी", "hi", "National")
}

enum class UserRole(val title: String, val subtitle: String, val defaultName: String) {
    PATIENT("Patient", "Bhaben Baruah • Guwahati", "Bhaben Baruah"),
    CAREGIVER("Family Caregiver", "Rahul Baruah (Son)", "Rahul Baruah"),
    ASHA_WORKER("ASHA Worker", "Anjali Deka • Sub-Center 04", "Anjali Deka")
}

enum class ScreenDestination(val title: String, val category: String) {
    // Onboarding
    SPLASH("Splash Screen", "ONBOARDING"),
    LANGUAGE_ACCESS("Language & Access", "ONBOARDING"),
    LOGIN_OTP("Login / OTP", "ONBOARDING"),

    // Patient App
    PATIENT_HOME("Patient Home", "PATIENT APP"),
    GAMES_MENU("Games Menu", "PATIENT APP"),
    MEMORY_MATCH("Memory Match (play)", "PATIENT APP"),
    ORIENTATION_QUIZ("Daily Orientation Quiz", "PATIENT APP"),
    REMINDERS("Reminders", "PATIENT APP"),
    AI_CHAT("AI Companion Chat", "PATIENT APP"),
    MY_FAMILY("My Family", "PATIENT APP"),
    SOS_ALERT("SOS Alert", "PATIENT APP"),

    // Caregiver / ASHA App
    CAREGIVER_DASHBOARD("Caregiver Dashboard", "CAREGIVER / ASHA APP"),
    COGNITIVE_TREND("Cognitive Trend", "CAREGIVER / ASHA APP"),
    MANAGE_REMINDERS("Manage Reminders", "CAREGIVER / ASHA APP"),
    ASHA_FIELD_VISIT("ASHA Field Visit", "CAREGIVER / ASHA APP"),

    // Shared
    SETTINGS_API("Settings & API", "SHARED")
}

enum class BottomNavItem(val title: String, val destination: ScreenDestination) {
    HOME("Home", ScreenDestination.PATIENT_HOME),
    GAMES("Games", ScreenDestination.GAMES_MENU),
    SMARAN_AI("Smaran AI", ScreenDestination.AI_CHAT),
    REMINDERS("Reminders", ScreenDestination.REMINDERS),
    SETTINGS("Settings", ScreenDestination.SETTINGS_API)
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSpoken: Boolean = false
)

enum class MessageSender {
    USER,
    SMARAN_AI,
    SYSTEM
}

data class FamilyMember(
    val id: String,
    val name: String,
    val relation: String,
    val bio: String,
    val voiceClipText: String,
    val memoryPrompt: String,
    val iconEmoji: String
)
