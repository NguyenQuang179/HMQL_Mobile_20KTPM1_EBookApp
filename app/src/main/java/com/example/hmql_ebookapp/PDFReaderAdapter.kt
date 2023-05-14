package com.example.hmql_ebookapp

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.nl.translate.TranslateLanguage

class PDFReaderAdapter(private val pages : ArrayList<String>)
    : RecyclerView.Adapter<PDFReaderAdapter.ViewHolder>(){

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
                    val translationCompleteListener = extractedTV.context.let { it1 ->
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

    var onItemClick: ((String) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val pageTv : TextView = listItemView.findViewById<TextView>(R.id.pageTv)!!
        init { listItemView.setOnClickListener { onItemClick?.invoke(pages[adapterPosition]) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val pageView : View = inflater.inflate(R.layout.pdf_reader_file_layout, parent, false)
        return ViewHolder(pageView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val page: String = pages.get(position)
        // Set item views based on your views and data model
        val pageTv = holder.pageTv
        pageTv.setText(page)
        pageTv.textSize = 20.0F
        extractedTV = holder.pageTv
        extractedTV.setMovementMethod(LinkMovementMethod.getInstance());
        extractedTV.isClickable = true
        extractedTV.isFocusable = true
        extractedTV.isFocusableInTouchMode = true
        extractedTV.customSelectionActionModeCallback = mActionModeCallback

    }

    override fun getItemCount(): Int = pages.size
}