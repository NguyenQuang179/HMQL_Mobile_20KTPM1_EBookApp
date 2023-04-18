package com.example.hmql_ebookapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.hmql_ebookapp.TranslateAPI.OnTranslationCompleteListener
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor


class ReadingScreen : AppCompatActivity() {
    lateinit var extractedTV: TextView

    private fun showTranslateDialog(selectedText: String){
        // processing translation
        val translate: TranslateAPI = TranslateAPI()
        translate.setOnTranslationCompleteListener(object :
            TranslateAPI.OnTranslationCompleteListener {
            override fun onStartTranslation() {
                // here you can perform initial work before translated the text like displaying progress bar
            }

            override fun onCompleted(text: String) {
                // "text" variable will give you the translated text
                findViewById<TextView>(R.id.translated_text).setText(text)
            }

            override fun onError(e: java.lang.Exception) {}
        })
        translate.execute(
            selectedText, // text to translate
            "en", // from language code
            "vi" // to language code
        )

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.translate_popup)
        dialog.show()
    }

    private val mActionModeCallback = object : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            menu.add(Menu.NONE, 1, Menu.NONE, "Translate").setOnMenuItemClickListener {
                val start = extractedTV.selectionStart
                val end = extractedTV.selectionEnd
                val selectedText = extractedTV.text?.substring(start, end)
                // perform translation logic here
                //Toast.makeText(this@ReadingScreen, "Translate: $selectedText", Toast.LENGTH_SHORT).show()
                if (selectedText != null) {
                    showTranslateDialog(selectedText)
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