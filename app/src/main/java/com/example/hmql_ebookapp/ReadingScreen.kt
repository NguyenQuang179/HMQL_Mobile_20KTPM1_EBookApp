package com.example.hmql_ebookapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.mlkit.nl.translate.TranslateLanguage
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor

class MyTranslationCompleteListener(val context: Context) : Translator.OnTranslationCompleteListener {
    override fun onCompleted(translatedText: String) {
        // Handle the completion event here
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.translate_popup)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<TextView>(R.id.translated_text).setText(translatedText)
        dialog.show()
    }

    override fun onError(exception: Exception) {
        // Handle the error event here
    }
}

class ReadingScreen : AppCompatActivity() {
    lateinit var extractedTV : TextView

    private val mActionModeCallback = object : ActionMode.Callback {
        // init the Translator class:
        val translator = Translator()
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            menu.add(Menu.NONE, 1, Menu.NONE, "Translate").setOnMenuItemClickListener {

                val start = extractedTV.selectionStart
                val end = extractedTV.selectionEnd
                val selectedText = extractedTV.text?.substring(start, end)
                // perform translation logic here
                //Toast.makeText(this@ReadingScreen, "Translate: $selectedText", Toast.LENGTH_SHORT).show()
                if (selectedText != null) {
                    val translationCompleteListener = MyTranslationCompleteListener(this@ReadingScreen)
                    translator.translateText(selectedText, TranslateLanguage.VIETNAMESE, translationCompleteListener)
                }
                mode.finish()
                true
            }
            return true
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {}
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }
    }
    /*override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        Log.d(TAG, "onCreateContextMenu: called")

        if (v.id == R.id.pdfContentTv) { // replace myTextView with the ID of your TextView
            menu.add(Menu.NONE, 1, Menu.NONE, "Translate").setOnMenuItemClickListener {
                val start = extractedTV.selectionStart
                val end = extractedTV.selectionEnd
                val selectedText = extractedTV.text?.substring(start, end)
                // perform translation logic here
                Toast.makeText(this, "Translate: $selectedText", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme) //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.lightTheme)  //default app theme
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading_screen)

        extractedTV = findViewById(R.id.pdfContentTv)
        extractData()

        //registerForContextMenu(extractedTV)
        extractedTV.customSelectionActionModeCallback = mActionModeCallback

        val settingBtn = findViewById<ImageButton>(R.id.settingBtn)
        settingBtn!!.setOnClickListener(){
            val intent : Intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

    }

    private fun extractData() {
        try {
            var extractedText = ""

            val pdfReader: PdfReader = PdfReader("res/raw/sample.pdf")

            val n = pdfReader.numberOfPages

            for (i in 0 until n) {
                extractedText =
                    """
                 $extractedText${
                        PdfTextExtractor.getTextFromPage(pdfReader, i + 1).trim { it <= ' ' }
                    }
                  
                 """.trimIndent()
                // to extract the PDF content from the different pages
            }
            extractedTV.setText("Guilty" + extractedText)
            pdfReader.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

}