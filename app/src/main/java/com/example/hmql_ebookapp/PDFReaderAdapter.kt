package com.example.hmql_ebookapp

import android.graphics.Color
import android.graphics.Typeface
import android.speech.tts.TextToSpeech
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.mlkit.nl.translate.TranslateLanguage

class PDFReaderAdapter(private val pages: ArrayList<String>, private val user: User?, private val spannablePages: List<SpannableStringBuilder>)
    : RecyclerView.Adapter<PDFReaderAdapter.ViewHolder>(){

    lateinit var extractedTV : TextView
    var fontSize : Float = 14.0F
    var typeface : Typeface = Typeface.DEFAULT
    lateinit var tts: TextToSpeech
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
                    spannableStringBuilder.setSpan(NoteClickableSpan("Add Note", start, end, curPos), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    extractedTV.text = spannableStringBuilder
                    spannablePages[curPos].setSpan(NoteClickableSpan("Add Note", start, end, curPos), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    // Store note data in Firebase Realtime Database
                    val database = FirebaseDatabase.getInstance()
                    val bookRef = database.getReference("/Users/${user!!.userID}/listOfBooks/0")
                    val noteData = NoteData(noteText, start, end, curPos)
                    //Get list at that point
                    bookRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userBook = snapshot.getValue(UserBook::class.java)
                            val notesData = userBook?.notes
                            val noteDataList = notesData?.curPos?.toMutableList() ?: mutableListOf()
                            // Get the current list of NoteData objects, or create a new mutable list if it's null

                            noteDataList.add(noteData)
                            // Add the new NoteData object to the list
                            if (notesData == null) {
                                userBook?.notes = NotesData(noteDataList)
                            } else {
                                notesData.curPos = noteDataList
                            }

                            // Update the UserBook object on Firebase
                            bookRef.setValue(userBook)

                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle errors here
                        }
                    })

                }
                mode.finish()
                true
            }
            menu.add(Menu.NONE, 4, Menu.NONE, "Read Outloud").setOnMenuItemClickListener {
                Log.i("in", "true")

                val start = extractedTV.selectionStart
                val end = extractedTV.selectionEnd
                val selectedText = extractedTV.text?.substring(start, end)
                if (selectedText != null) {
                    Log.i("Text", selectedText)
                    tts!!.speak(selectedText.toString(), TextToSpeech.QUEUE_FLUSH, null,"")
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
        pageView.findViewById<TextView>(R.id.pageTv).textSize = fontSize
        pageView.findViewById<TextView>(R.id.pageTv).typeface = typeface
        return ViewHolder(pageView)
    }
    var curPos: Int = 0;
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        curPos = position
        val page: String = pages.get(position)
        val spannablePage: SpannableStringBuilder = spannablePages[position]
//        Log.e("adapter","binding ${position} and ${spannablePages.size}")
//        Log.e("adapter","binding ${position} and ${spannablePages[5]}")
        // Set item views based on your views and data model
        val pageTv = holder.pageTv
        // pageTv.text = page

        pageTv.text = spannablePage
        pageTv.textSize = fontSize
        pageTv.typeface = typeface
        extractedTV = holder.pageTv
        extractedTV.movementMethod = LinkMovementMethod.getInstance()
        extractedTV.isClickable = true
        extractedTV.isFocusable = true
        extractedTV.isFocusableInTouchMode = true
        extractedTV.customSelectionActionModeCallback = mActionModeCallback

    }

    override fun getItemCount(): Int = pages.size

    fun setFontSize(size : Float) : Boolean {
        fontSize = size
        return true
    }
}