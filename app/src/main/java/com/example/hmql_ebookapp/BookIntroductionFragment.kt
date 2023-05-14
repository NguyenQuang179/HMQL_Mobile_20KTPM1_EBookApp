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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import java.time.LocalDateTime

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
fun addBookToUserList(user: User?, book: Book) { //Function to add Book to User's list of books
    if (user != null) {
        val bookList = user.listOfBooks.toMutableList()
        val existingBookIndex = bookList.indexOfFirst { it.bookID == book.bookID }
        if (existingBookIndex >= 0) {
            // If the book already exists in the list, remove it
            bookList.removeAt(existingBookIndex)
        }
        // Add the book to the beginning of the list
        bookList.add(0, UserBook(book.bookID, book.title, "status", 1, true, false))
        // Update the user's list of books in Firebase
        val usersRef = FirebaseDatabase.getInstance().getReference("Users")
        val userRef = user.userID?.let { usersRef.child(it) }
        if (userRef != null) {
            userRef.child("listOfBooks").setValue(bookList)
        }
    }
}
class BookIntroductionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sampleBookList : ArrayList<SampleBook>
    lateinit var bookNameList : Array<String>
    lateinit var authorNameList : Array<String>
    lateinit var bookImgIdList : Array<Int>

    lateinit var bookID: String;
    lateinit var userID: String;
    private var bookRelated = ArrayList<Book>()
    private var recommendationAdapter = RecommendationAdapter(bookRelated)
    private lateinit var RecommendationBooksRV: RecyclerView
    private lateinit var data: Book

    private lateinit var adapter_review: ReviewAdapterClass
    private fun sampleDataInit() {
        val ref2: DatabaseReference = FirebaseDatabase.getInstance().getReference("book/${bookID}")
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    data = snapshot.getValue(Book::class.java)!!
                    val bookTV = view?.findViewById<TextView>(R.id.bookTV);
                    bookTV!!.setText(data!!.title)
                    val authorTV = view?.findViewById<TextView>(R.id.authorTV);
                    authorTV!!.setText(data!!.author)
                    val ratingTV = view?.findViewById<TextView>(R.id.RatingTV);
                    ratingTV!!.setText(data!!.averageStar.toString())
                    val Synopsis_detailTV = view?.findViewById<TextView>(R.id.Synopsis_detailTV);
                    Synopsis_detailTV!!.setText(data!!.description)
                    val bookIV = view?.findViewById<ImageView>(R.id.bookIV);
                    if (bookIV != null) {
                        Glide.with(requireContext())
                            .load(data.cover)
                            .into(bookIV)
                    };
                    if (data.reviews.size > 0){
                        reviewList = data.reviews as ArrayList<Review>
                        Log.i("review", "${reviewList.size}")
                        adapter_review = ReviewAdapterClass(reviewList)
                        ReviewRV.adapter = adapter_review
                    }
                    if (data.categories.size > 0){
                        categoryList = data.categories as ArrayList<Category>
                        adapterTags = TagsAdapterClass(categoryList)
                        TagsRV.adapter = adapterTags
                    }
                    bookPDFLink = data.pdf

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun recommendDataInit() {
        val ref2: DatabaseReference = FirebaseDatabase.getInstance().getReference("book")
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val book = child.getValue(Book::class.java)
                        if (book!!.bookID != data.bookID)
                        {
                            if (book!!.categories.size > 0)
                            {
                                if (data.categories.intersect(book.categories).isNotEmpty()){
                                    book?.let { bookRelated.add(it) }
                                }
                                else if (data.author == book.author){
                                    book?.let { bookRelated.add(it) }
                                }
                                else{

                                }
                            }
                        }

                    }
                    Log.d("Books related size", "Number of books: ${bookRelated.size}")
                    bookRelated.sortDescending();
                    if (bookRelated.size > 5) bookRelated = ArrayList(bookRelated.subList(0, 5))
                    recommendationAdapter = RecommendationAdapter(bookRelated)
                    RecommendationBooksRV.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    RecommendationBooksRV.adapter = recommendationAdapter
                    recommendationAdapter.onItemClick = { book ->
                        Log.i("In", "Chuyen trang")
                        val bundle = Bundle()
                        bundle.putString("bookID", book.bookID)

                        val fragment = BookIntroductionFragment()
                        fragment.arguments = bundle

                        requireActivity().supportFragmentManager.commit {
                            replace(R.id.fragment_container_view, fragment)
                            setReorderingAllowed(true)
                            addToBackStack("BookIntroductionFragment")
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private var reviewList = ArrayList<Review>()
    lateinit var reviewerImageList : Array<Int>
    lateinit var reviewerNameList : Array<String>
    lateinit var reviewRatingValueList : Array<Float>
    lateinit var reviewRateList : Array<String>
    lateinit var reviewTextList : Array<String>
    lateinit var ReviewRV : RecyclerView
    lateinit var TagsRV: RecyclerView
    lateinit var adapterTags: TagsAdapterClass
    private var categoryList = ArrayList<Category>()

    lateinit var bookPDFLink : String
    lateinit var bookFavoriteStatus : String

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
        val bundle = arguments
        if (bundle != null) {
            bookID = bundle.getString("bookID").toString()
            // Sử dụng giá trị dữ liệu trong SearchResultFragment
        }

        //set the user info
        val userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        val user = userViewModel.user



        sampleDataInit()
        recommendDataInit()
        TagsRV = view.findViewById<RecyclerView>(R.id.TagsRV)
//        val ChaptersRV = view.findViewById<RecyclerView>(R.id.ChaptersRV)
        RecommendationBooksRV = view.findViewById<RecyclerView>(R.id.RecommendationBooksRV)
        ReviewRV = view.findViewById<RecyclerView>(R.id.ReviewRV)

        val SeeMoreRecBtn = view.findViewById<Button>(R.id.SeeMoreRecBtn)
        val AddReviewButton = view.findViewById<Button>(R.id.WriteReviewBtn)

        val ReadButton = view.findViewById<Button>(R.id.ReadBtn)

        val BackButton = view.findViewById<ImageButton>(R.id.backBtn)
        val LikedButton = view.findViewById<ImageButton>(R.id.LikeBtn)
        val DownloadButton = view.findViewById<ImageButton>(R.id.downloadBtn)

        for (book in user?.listOfBooks!!) {

            if(book.bookID == bookID){

                if(book.liked == true){
//                    Toast.makeText(this.context,"${book.bookName} is liked",Toast.LENGTH_SHORT).show()
                    LikedButton.setImageResource(R.drawable.bookmark_solid)
                    LikedButton.tag = "bookmarked"
                }
                else {
//                    Toast.makeText(this.context,"${book.bookName} is not liked",Toast.LENGTH_SHORT).show()
                }

            }
        }

        // this creates a vertical layout Manager
        TagsRV.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
//        ChaptersRV.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
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
        val adapter_chapters = ChapterAdapterClass(data_chapters)

        adapter_review = ReviewAdapterClass(reviewList)

        // Setting the Adapter with the recyclerview
//        ChaptersRV.adapter = adapter_chapters
        RecommendationBooksRV.adapter = recommendationAdapter
        recommendationAdapter.onItemClick = { book ->
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
                // Add To History, if already in history then pop it out and push it to the top

                //set the user info
                val userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
                val user = userViewModel.user
                addBookToUserList(user, data)
                //Update user after update

            }
        }

        DownloadButton.setOnClickListener{
            Toast.makeText(this.context, "PDF Link $bookPDFLink", Toast.LENGTH_SHORT).show()
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
        val url : String = bookPDFLink
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val review_text = data!!.extras!!.getString("REVIEW")
            val rating_value = data.extras!!.getString("RATING")
            val rating_bar_value = rating_value + "f"
            Log.d("REVIEW", review_text.toString())
            Log.d("RATING", rating_value.toString())

            val newReview = Review("new ", "New User", rating_value.toString().toInt(), review_text.toString(), LocalDateTime.now().toString() )

            reviewList.add(0, newReview)

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