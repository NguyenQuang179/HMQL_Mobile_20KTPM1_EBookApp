package com.example.hmql_ebookapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.google.mlkit.nl.translate.TranslateLanguage
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


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
        Log.d("NoteClickableSpan", "onClick triggered for text: $noteText")

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

class RetrievePDFFromURL(pdfView: PDFView, isVertical : Boolean, curPageEt : EditText) : AsyncTask<String, Void, InputStream>() {
    // on below line we are creating a variable for our pdf view.
    @SuppressLint("StaticFieldLeak")
    val pdfView: PDFView = pdfView
    val isVertical = isVertical
    val curPageEt = curPageEt
    val curPage = curPageEt.text.toString().toInt() - 1

    // on below line we are calling our do in background method.
    override fun doInBackground(vararg params: String?): InputStream? {
        // on below line we are creating a variable for our input stream.
        var inputStream: InputStream? = null
        try {
            // on below line we are creating an url
            // for our url which we are passing as a string.
            val url = URL(params.get(0))

            // on below line we are creating our http url connection.
            val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection

            // on below line we are checking if the response
            // is successful with the help of response code
            // 200 response code means response is successful
            if (urlConnection.responseCode == 200) {
                // on below line we are initializing our input stream
                // if the response is successful.
                inputStream = BufferedInputStream(urlConnection.inputStream)
            }
        }
        // on below line we are adding catch block to handle exception
        catch (e: Exception) {
            // on below line we are simply printing
            // our exception and returning null
            e.printStackTrace()
            return null
        }
        // on below line we are returning input stream.
        return inputStream
    }

    override fun onPostExecute(result: InputStream?) {
        // on below line we are loading url within our
        // pdf view on below line using input stream.
        pdfView.fromStream(result)
            .swipeHorizontal(true)
            .load()
        var isNightMode : Boolean = false
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) isNightMode = true
        pdfView.fromAsset("samplebook.pdf")
            .nightMode(isNightMode)
            .swipeHorizontal(!isVertical)
            .defaultPage(curPage)
            .pageSnap(true)
            .onLongPress {
                Toast.makeText(pdfView.context, pdfView.currentPage.toString(), Toast.LENGTH_SHORT).show()
            }
            .onPageChange(OnPageChangeListener { page, pageCount ->
                curPageEt.setText((pdfView.currentPage + 1).toString())
            })
            .load()
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

//    VARIABLES
    lateinit var extractedTV: TextView
    var isVertical : Boolean = true
    var isText : Boolean = true
    var pages = ArrayList<String>()

    private fun extractData() {
        try {
            var extractedText = ""
            val pdfReader: PdfReader = PdfReader("res/raw/samplebook.pdf")
            pdfReader.removeAnnotations()
            val n = pdfReader.numberOfPages

            for (i in 0 until n) {
//                extractedText =
//                    """
//                 $extractedText${
//                        PdfTextExtractor.getTextFromPage(pdfReader, i + 1).trim { it <= ' ' }
//                    }
//                 """.trimIndent()
                extractedText = PdfTextExtractor.getTextFromPage(pdfReader, i + 1)
                pages.add(extractedText)
                // to extract the PDF content from the different pages
            }
            //extractedTV.setText(extractedText)
            pdfReader.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extractData()
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        extractedTV = view.findViewById(R.id.pdfContentTv)
//        extractedTV.movementMethod = LinkMovementMethod.getInstance()
//        extractedTV.customSelectionActionModeCallback = mActionModeCallback

        var totalPageTv = view.findViewById<TextView>(R.id.totalPageTv)
        totalPageTv.text = "/ ${pages.size}"
        var curPageEt = view.findViewById<EditText>(R.id.curPageEt)
        curPageEt.setText("1")

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(null)

        var pdfView = view.findViewById<PDFView>(R.id.pdfview)
        pdfView.visibility = View.GONE

        var pagesRv = view.findViewById<RecyclerView>(R.id.pagesRv)
        var adapter = PDFReaderAdapter(pages)
        //adapter.pageTV.customSelectionActionModeCallback = mActionModeCallback
        pagesRv.adapter = adapter
        pagesRv.layoutManager = LinearLayoutManager(requireContext())

//        adapter.onItemClick = { page ->
//            //Toast.makeText(requireContext(), (pages.indexOf(page) + 1).toString(), Toast.LENGTH_SHORT).show()
//        }

//        Paging Text View
        pagesRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position: Int = (pagesRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    Toast.makeText(requireContext(), (position + 1).toString(), Toast.LENGTH_SHORT).show()
                    curPageEt.setText((position + 1).toString())
                }
            }
        })

//        Next Page Btn
        val nextBtn = view.findViewById<Button>(R.id.nextPageBtn)
        nextBtn.setOnClickListener {
            curPageEt.setText((curPageEt.text.toString().toInt() + 1).toString())
            pagesRv.scrollToPosition(curPageEt.text.toString().toInt() - 1)
            pdfView.jumpTo(curPageEt.text.toString().toInt() - 1)
        }

//        Prev Page Btn
        val prevBtn = view.findViewById<Button>(R.id.prevPageBtn)
        prevBtn.setOnClickListener {
            curPageEt.setText((curPageEt.text.toString().toInt() - 1).toString())
            pagesRv.scrollToPosition(curPageEt.text.toString().toInt() - 1)
            pdfView.jumpTo(curPageEt.text.toString().toInt() - 1)
        }

        curPageEt.setOnFocusChangeListener{ _, hasFocus ->
//            Not Focus
            if(!hasFocus){
                Toast.makeText(requireContext(), curPageEt.text.toString(), Toast.LENGTH_SHORT).show()
                var totalPageNum : Int = totalPageTv.text.toString().substring(2).toString().toInt()
                if((curPageEt.text.toString() == "") || (curPageEt.text.toString().toInt() > totalPageNum) || (curPageEt.text.toString().toInt() < 1) )
                    // set back text to current page
                    curPageEt.setText(((pagesRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 1).toString())
                else {
                    pagesRv.scrollToPosition(curPageEt.text.toString().toInt() - 1)
                    pdfView.jumpTo(curPageEt.text.toString().toInt() - 1)
                }
            }
//            Focus
        }

        val scrollModeBtn = view.findViewById<Button>(R.id.scrollModeBtn)
        scrollModeBtn.setOnClickListener {
            if(isVertical) {
                isVertical = false
                snapHelper.attachToRecyclerView(pagesRv)
                scrollModeBtn.text = "\uf337"
                pagesRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                pagesRv.scrollToPosition(curPageEt.text.toString().toInt() - 1)
                if(!isText) RetrievePDFFromURL(pdfView, isVertical, curPageEt).execute("")
            }
            else {
                isVertical = true
                snapHelper.attachToRecyclerView(null)
                scrollModeBtn.text = "\uf338"
                pagesRv.layoutManager = LinearLayoutManager(requireContext())
                pagesRv.scrollToPosition(curPageEt.text.toString().toInt() - 1)
                if(!isText) RetrievePDFFromURL(pdfView, isVertical, curPageEt).execute("")
            }
            //pagesRv.scrollToPosition(curPageEt.text.toString().toInt() - 1)
            //pagesRv.scrollToPosition(5)
        }

        val viewModebtn = view.findViewById<Button>(R.id.viewModeBtn)
        viewModebtn.setOnClickListener {
            if(isText) {
                isText = false
                pdfView.visibility = View.VISIBLE
                pagesRv.visibility = View.GONE
                RetrievePDFFromURL(pdfView, isVertical, curPageEt).execute("")
            }
            else {
                isText = true
                pagesRv.visibility = View.VISIBLE
                pdfView.visibility = View.GONE
            }
        }

        val settingBtn = view.findViewById<ImageButton>(R.id.settingBtn)
        settingBtn!!.setOnClickListener {
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