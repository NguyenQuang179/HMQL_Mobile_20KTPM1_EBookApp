package com.example.hmql_ebookapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@SuppressLint("NotifyDataSetChanged")
@Deprecated("Deprecated in Java")
@Suppress("DEPRECATION")

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookIntroductionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@Suppress("DEPRECATION")
class BookIntroductionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sampleBookList : ArrayList<SampleBook>
    lateinit var bookNameList : Array<String>
    lateinit var authorNameList : Array<String>
    lateinit var bookImgIdList : Array<Int>

    private fun sampleDataInit() {
        // Favourite Books
        sampleBookList = arrayListOf<SampleBook>()

        bookNameList = arrayOf(
            "Born a crime: Stories from a S...",
            "Merry Christmas",
            "Little Blue Truck's Halloween",
            "Born a crime: Stories from a S...",
            "Merry Christmas",
            "Little Blue Truck's Halloween",
            "Born a crime: Stories from a S...",
            "Merry Christmas",
            "Little Blue Truck's Halloween",
            "Born a crime: Stories from a S..."
        )

        authorNameList = arrayOf(
            "Alice Schertle, Jill McElmurry",
            "Alice Schertle",
            "Jill McElmurry",
            "Bret Bais",
            "Liana Moriatory",
            "Alice Schertle, Jill McElmurry",
            "Alice Schertle",
            "Jill McElmurry",
            "Bret Bais",
            "Liana Moriatory"
        )

        bookImgIdList = arrayOf(
            R.drawable.favbookimg1,
            R.drawable.favbookimg2,
            R.drawable.favbookimg3,
            R.drawable.favbookimg1,
            R.drawable.favbookimg2,
            R.drawable.favbookimg3,
            R.drawable.favbookimg1,
            R.drawable.favbookimg2,
            R.drawable.favbookimg3,
            R.drawable.favbookimg1
        )

        for(i in bookNameList.indices) {
            val sampleBook = SampleBook(bookNameList[i], authorNameList[i], bookImgIdList[i])
            sampleBookList.add(sampleBook)
        }
    }

    private  lateinit var sampleReviewList : ArrayList<ReviewViewModel>
    lateinit var reviewerImageList : Array<Int>
    lateinit var reviewerNameList : Array<String>
    lateinit var reviewRatingValueList : Array<Float>
    lateinit var reviewRateList : Array<String>
    lateinit var reviewTextList : Array<String>
    lateinit var ReviewRV : RecyclerView
    private fun sampleReviewInit() {
        // Favourite Books
        sampleReviewList = arrayListOf<ReviewViewModel>()


        reviewerNameList = arrayOf(
            "Luu Hoang Minh",
            "Truong Gia Huy",
            "Nguyen Ngoc Quang",
            "Ha Tuan Lam"
        )

        reviewerImageList = arrayOf(
            R.drawable.sampleauthor1,
            R.drawable.sampleauthor2,
            R.drawable.sampleauthor3,
            R.drawable.sampleauthor1
        )

        reviewRatingValueList = arrayOf(
            5.0f,
            4.0f,
            2.5f,
            1.5f
        )

        reviewRateList = arrayOf(
            "5.0",
            "4.0",
            "2.5",
            "1.5"
        )

        reviewTextList = arrayOf(
            "This book is amazing",
            "This book is shit",
            "This book change my life, I am now a millionaire",
            "This book makes me change my way of life"
        )

        for(i in reviewerNameList.indices) {
            val sampleReview = ReviewViewModel(reviewerImageList[i], reviewerNameList[i], reviewRatingValueList[i], reviewRateList[i],reviewTextList[i] )
            sampleReviewList.add(sampleReview)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    val STORAGE_PERMISSION_CODE: Int = 1000
    val REQUEST_CODE = 123
    var thiscontext: Context? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        thiscontext = container!!.context
        return inflater.inflate(R.layout.fragment_book_introduction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sampleDataInit()
        sampleReviewInit()

        val TagsRV = view.findViewById<RecyclerView>(R.id.TagsRV)
        val ChaptersRV = view.findViewById<RecyclerView>(R.id.ChaptersRV)
        val RecommendationBooksRV = view.findViewById<RecyclerView>(R.id.RecommendationBooksRV)
        ReviewRV = view.findViewById<RecyclerView>(R.id.ReviewRV)

        val SeeMoreRecBtn = view.findViewById<Button>(R.id.SeeMoreRecBtn)
        val AddReviewButton = view.findViewById<Button>(R.id.WriteReviewBtn)

        val ReadButton = view.findViewById<Button>(R.id.ReadBtn)

        val BackButton = view.findViewById<ImageButton>(R.id.backBtn)
        val LikedButton = view.findViewById<ImageButton>(R.id.LikeBtn)
        val DownloadButton = view.findViewById<ImageButton>(R.id.downloadBtn)

        LikedButton.tag = "bookmark"

        // this creates a vertical layout Manager
        TagsRV.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        ChaptersRV.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        RecommendationBooksRV.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        ReviewRV.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        val data_tags = ArrayList<TagsViewModel>()
        val data_chapters = ArrayList<ChapterViewModel>()

        for (i in 1..20) {
            data_tags.add(TagsViewModel("Tags " + i))
        }

        for (i in 1..20) {
            data_chapters.add(ChapterViewModel("Ch. " + i, "Chapter name " + i))
        }

        // This will pass the ArrayList to our Adapter
        val adapter_tags = TagsAdapterClass(data_tags)
        val adapter_chapters = ChapterAdapterClass(data_chapters)
        val adapter_recommendations = RecommendationAdapter(sampleBookList)
        val adapter_review = ReviewAdapterClass(sampleReviewList)

        // Setting the Adapter with the recyclerview
        TagsRV.adapter = adapter_tags
        ChaptersRV.adapter = adapter_chapters
        RecommendationBooksRV.adapter = adapter_recommendations
        adapter_recommendations.onItemClick = { book ->
            requireActivity().supportFragmentManager.commit {
                replace<BookIntroductionFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
                addToBackStack("bookIntroductionFragment")
            }
        }

        ReviewRV.adapter = adapter_review

        SeeMoreRecBtn!!.setOnClickListener() {
//            Toast.makeText(this.context, "See More Book Button Clicked", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.commit {
                replace<HomeFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
                addToBackStack("homeFragment")
            }
        }

        LikedButton.setOnClickListener {
            if (LikedButton.tag == "bookmark") {
                LikedButton.setImageResource(R.drawable.bookmark_solid)
                LikedButton.tag = "bookmarked"
            } else {
                LikedButton.setImageResource(R.drawable.bookmark_regular)
                LikedButton.tag = "bookmark"
            }
        }

        ReadButton.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace<ReadingFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
                addToBackStack("readFragment")
            }
        }

        DownloadButton.setOnClickListener{
            Toast.makeText(this.context, "Downloading!", Toast.LENGTH_SHORT).show()
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (requireActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied, request it

                    //show popup for runtime permisison
                    requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                }
                else{
                    //permission already granted, perform download
                    startDownloading()
                }
            }
            else{
                //system os is less than marshmallow, runtime permission not required, perform downlaod
                startDownloading()
            }
        }

        BackButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        AddReviewButton.setOnClickListener {
            val reviewPopUp = ReviewPopupFragment()
            reviewPopUp.setTargetFragment(this, REQUEST_CODE)
            reviewPopUp.show((this.context as AppCompatActivity).supportFragmentManager, "reviewPopup")
        }
    }

    private fun startDownloading() {

        //get text/url from edit text
        val url : String = "https://arxiv.org/pdf/2203.14367.pdf"
//        url = urlEt.text.toString() TODO("Get book link to download")

        //download request
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The file is downloading...")

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")

        //get download service, and enqueue file
        val manager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted, perform download
                    startDownloading()
                }
                else {
                    //permission from popup was denied, show error message
                    Toast.makeText(this.context, "Permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val review_text = data!!.extras!!.getString("REVIEW")
            val rating_value = data.extras!!.getString("RATING")
            val rating_bar_value = rating_value + "f"
            Log.d("REVIEW", review_text.toString())
            Log.d("RATING", rating_value.toString())

            val new_review = ReviewViewModel(R.drawable.sampleauthor3, "New User", rating_bar_value.toFloat(), rating_value.toString(), review_text.toString() )

            sampleReviewList.add(0, new_review)

            ReviewRV.adapter?.notifyDataSetChanged()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookIntroductionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookIntroductionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}