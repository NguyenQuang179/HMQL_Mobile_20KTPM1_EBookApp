package com.example.hmql_ebookapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.mlkit.nl.translate.TranslateLanguage
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteClickableSpan(var noteText: String) : ClickableSpan() {

    override fun onClick(widget: View) {
        val popup = PopupWindow(widget.context)
        popup.isFocusable = true
        popup.isTouchable = true
        val view = LayoutInflater.from(widget.context).inflate(R.layout.popup_note, widget.parent as ViewGroup, false)

        val etNoteText = view.findViewById<EditText>(R.id.et_note_input)
        etNoteText.setText(noteText)
        etNoteText.requestFocus() // set focus on the EditText
        val imm = widget.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(etNoteText, InputMethodManager.SHOW_IMPLICIT)
        val btnSave = view.findViewById<Button>(R.id.btn_save_note)
        btnSave.setOnClickListener {
            val updatedNoteText = etNoteText.text.toString()
            if (updatedNoteText.isNotEmpty()) {
                popup.dismiss()
                noteText = updatedNoteText
                val newNote = SpannableString("[$noteText] ")
                newNote.setSpan(
                    NoteClickableSpan(noteText),
                    0, newNote.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                widget.invalidate()
            } else {
                Toast.makeText(widget.context, "Note cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        view.findViewById<Button>(R.id.btn_close_note).setOnClickListener {
            popup.dismiss()
        }
        popup.contentView = view
        popup.showAsDropDown(widget)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = ds.linkColor
        ds.isUnderlineText = false
    }
}


class ReadingFragment : Fragment() {
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
                    val translationCompleteListener = this@ReadingFragment.context?.let { it1 ->
                        MyTranslationCompleteListener(
                            it1
                        )
                    }
                    if (translationCompleteListener != null) {
                        translator.translateText(selectedText, TranslateLanguage.VIETNAMESE, translationCompleteListener)
                    }
                }
                mode.finish()
                true
            }
            menu.add(Menu.NONE, 2, Menu.NONE, "Highlight").setOnMenuItemClickListener {
                val start = extractedTV.selectionStart
                val end = extractedTV.selectionEnd
                if (start != -1 && end != -1) {
                    val selectedText = extractedTV.text.subSequence(start, end)
                    val hightlightStr = SpannableStringBuilder(extractedTV.text)
                    hightlightStr.setSpan(
                        BackgroundColorSpan(Color.YELLOW),
                        start,
                        end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    extractedTV.text = hightlightStr
                }
                mode.finish()
                true
            }
            menu.add(Menu.NONE, 3, Menu.NONE, "Add Notes").setOnMenuItemClickListener {
                val start = extractedTV.selectionStart
                val end = extractedTV.selectionEnd
                if (start != -1 && end != -1) {
                    val selectedText = extractedTV.text.subSequence(start, end)
                    val noteText = "This is a note for the selected text."
                    val spannableStringBuilder = SpannableStringBuilder(extractedTV.text)
                    spannableStringBuilder.setSpan(NoteClickableSpan("lala"), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    extractedTV.text = spannableStringBuilder
                }
                mode.finish()
                true
            }
            menu.add(Menu.NONE, 4, Menu.NONE, "Read Outloud").setOnMenuItemClickListener {
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var extractedTV: TextView

    private fun extractData() {
        try {
            var extractedText = ""

            val pdfReader: PdfReader = PdfReader("res/raw/sample2.pdf")

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
            extractedTV.setText(extractedText)
            pdfReader.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        extractedTV = view.findViewById(R.id.pdfContentTv)
        extractData()
        extractedTV.movementMethod = LinkMovementMethod.getInstance()
        extractedTV.customSelectionActionModeCallback = mActionModeCallback


        val settingBtn = view.findViewById<ImageButton>(R.id.settingBtn)
        settingBtn!!.setOnClickListener(){
            requireActivity().supportFragmentManager.commit {
                replace<SettingFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
                addToBackStack("settingFragment")
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReadingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReadingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}