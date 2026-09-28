package com.example.service

import android.content.Context
import android.media.ToneGenerator
import android.media.AudioManager
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TtsHelper(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
            toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)
        } catch (e: Exception) {
            Log.e("TtsHelper", "Failed to initialize TTS", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.language = Locale.ENGLISH
            tts?.setSpeechRate(0.85f) // Gentle, calm speed for elders
            tts?.setPitch(1.0f)
        } else {
            Log.w("TtsHelper", "TTS Initialization failed")
        }
    }

    fun speak(text: String, languageCode: String = "en") {
        if (!isInitialized || tts == null) return

        try {
            val primaryLocale = when (languageCode) {
                "as" -> Locale.forLanguageTag("as-IN")
                "bn" -> Locale.forLanguageTag("bn-IN")
                "hi" -> Locale.forLanguageTag("hi-IN")
                "brx" -> Locale.forLanguageTag("brx-IN")
                "mni" -> Locale.forLanguageTag("mni-IN")
                "kha" -> Locale.forLanguageTag("kha-IN")
                "lus" -> Locale.forLanguageTag("lus-IN")
                "grt" -> Locale.forLanguageTag("grt-IN")
                else -> Locale.ENGLISH
            }
            val avail = tts?.isLanguageAvailable(primaryLocale) ?: TextToSpeech.LANG_NOT_SUPPORTED
            if (avail >= TextToSpeech.LANG_AVAILABLE) {
                tts?.language = primaryLocale
            } else {
                // Indic phonetic fallback if device doesn't have local NER TTS package
                val fallbackLocale = when (languageCode) {
                    "as", "mni" -> Locale.forLanguageTag("bn-IN")
                    "brx" -> Locale.forLanguageTag("hi-IN")
                    else -> Locale.ENGLISH
                }
                val fbAvail = tts?.isLanguageAvailable(fallbackLocale) ?: TextToSpeech.LANG_NOT_SUPPORTED
                if (fbAvail >= TextToSpeech.LANG_AVAILABLE) {
                    tts?.language = fallbackLocale
                } else {
                    tts?.language = Locale.ENGLISH
                }
            }

            // Clean text of emojis before speaking
            val cleanText = text.replace(Regex("[^\\p{L}\\p{Nd}\\p{P}\\p{Z}]"), "")
            tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "SmaranUtterance")
        } catch (e: Exception) {
            Log.e("TtsHelper", "Error speaking text", e)
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun playGentleChime() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
        } catch (e: Exception) {
            Log.e("TtsHelper", "Error playing chime", e)
        }
    }

    fun playSosAlertTone() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 1500)
        } catch (e: Exception) {
            Log.e("TtsHelper", "Error playing SOS tone", e)
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        toneGenerator?.release()
    }
}
