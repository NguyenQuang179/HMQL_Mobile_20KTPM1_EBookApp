package com.example.hmql_ebookapp

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class Translator() {

    fun translateText(sourceText: String, targetLanguage: String, listener: OnTranslationCompleteListener) {
                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(targetLanguage)
                    .build()

                val translator = Translation.getClient(options)

                translator.downloadModelIfNeeded()
                    .addOnSuccessListener {
                        translator.translate(sourceText)
                            .addOnSuccessListener { translatedText ->
                                listener.onCompleted(translatedText)
                            }
                            .addOnFailureListener { exception ->
                                listener.onError(exception)
                            }
                            .addOnCompleteListener {
                                translator.close()
                            }
                    }
                    .addOnFailureListener { exception ->
                        listener.onError(exception)
                    }
            }


    interface OnTranslationCompleteListener {
        fun onCompleted(translatedText: String)
        fun onError(exception: Exception)
    }
}