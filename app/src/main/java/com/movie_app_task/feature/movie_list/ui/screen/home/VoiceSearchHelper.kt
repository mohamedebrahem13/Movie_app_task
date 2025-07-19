package com.movie_app_task.feature.movie_list.ui.screen.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.movie_app_task.R
import java.util.Locale

class VoiceSearchHelper(
    private val context: Context,
    private val onResult: (String) -> Unit,
    private val onError: ((String) -> Unit)? = null
) {

    private var speechRecognizer: SpeechRecognizer? = null

    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onError?.invoke("Speech recognition not available on this device")
            return
        }

        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        }

        val listener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                val errorMsg = getErrorMessage(error)
                onError?.invoke(errorMsg)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val result = matches?.firstOrNull() ?: ""
                onResult(result)
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }

        speechRecognizer?.setRecognitionListener(listener)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to search...")
        }

        speechRecognizer?.startListening(intent)
    }

    fun destroy() {
        speechRecognizer?.stopListening()
        speechRecognizer?.cancel()
        speechRecognizer?.destroy()
        speechRecognizer = null
    }

    private fun getErrorMessage(errorCode: Int): String {
        val errorMessages = mapOf(
            SpeechRecognizer.ERROR_AUDIO to R.string.audio_recording_error,
            SpeechRecognizer.ERROR_CLIENT to R.string.client_side_error,
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS to R.string.insufficient_permissions,
            SpeechRecognizer.ERROR_NETWORK to R.string.network_error,
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT to R.string.network_timeout,
            SpeechRecognizer.ERROR_NO_MATCH to R.string.no_match_found,
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY to R.string.speech_recognizer_is_busy,
            SpeechRecognizer.ERROR_SERVER to R.string.server_error,
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT to R.string.no_speech_input
        )

        val stringResId = errorMessages[errorCode] ?: R.string.unknown_error
        return context.getString(stringResId)
    }
}
