package com.example.hmql_ebookapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    //signing
    val AUTH_REQUEST_CODE = 7192 // Any number you want
    val AUTH_REGISTER_CODE = 7193 // Any number you want
    val AUTH_LOGOUT_CODE = 7194 // Any number you want
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var listener:FirebaseAuth.AuthStateListener
    lateinit var providers:List<AuthUI.IdpConfig>
    public fun init(){ //init login
        providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(true).build(), //Email Builder
            AuthUI.IdpConfig.GoogleBuilder().build(), //Google Builder
            //AuthUI.IdpConfig.FacebookBuilder().build(), //Facebook Builder
            //AuthUI.IdpConfig.PhoneBuilder().build(), //Phone Builder

        )
        firebaseAuth = FirebaseAuth.getInstance()
        listener = FirebaseAuth.AuthStateListener { myFirebaseAuth ->
            val user = myFirebaseAuth.currentUser
            if(user != null){
                //User already logged in
                Toast.makeText(this@MainActivity, "Welcome Back", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(this@MainActivity, "Opening login screen", Toast.LENGTH_SHORT).show()
                //User not logged in
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.logo)
                        .build(), AUTH_REQUEST_CODE
                )
            }
        }

    }
//            RC_SIGN_IN
//        )
//    }

    // Logout function
    fun logout(activity: Activity) {
        AuthUI.getInstance()
            .signOut(activity)
            .addOnCompleteListener {
                // Successfully signed out, do something here
                activity.startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.logo)
                        .build(),
                    AUTH_REQUEST_CODE
                )
            }
    }
    @SuppressLint("MissingInflatedId")
    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            supportFragmentManager.popBackStack()
        }
    }
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme) //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.lightTheme)  //default app theme
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init();
//        startActivityForResult(
//            AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(providers)
//                .setTheme(R.style.LoginTheme)
//                .build(),
//            AUTH_REQUEST_CODE
//        )

//        for (i in 1..10){
//            val listOfBook = arrayListOf<String>((i+1).toString(),(i+2).toString(),(i+3).toString(),(i+4).toString())
//            val newAuthor = Author("Tac gia $i", "$i","Mo ta tac gia","img",listOfBook)
//            val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("author")
//            ref.child(newAuthor.authorID).setValue(newAuthor) { databaseError, databaseReference ->
//                if (databaseError != null) {
//                    Log.e("Firebase", "Error adding book: ${databaseError.message}")
//                } else {
//                    Log.i("Firebase", "Book added successfully")
//                }
//            }
//        }

        //init the ViewModel for mainActivity
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

//        val list = arrayListOf<Book>();
//        val ref1: DatabaseReference = FirebaseDatabase.getInstance().getReference("book")
//        ref1.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    for (child in snapshot.children){
//                        val book = child.getValue(Book::class.java)
//                        list.add(book!!);
//                    }
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
//        Log.d("Book on main activity", "Number of books: ${list.size}")
//
//        val ref2: DatabaseReference = FirebaseDatabase.getInstance().getReference("book/sadfsdfadf")
//        ref2.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                if (snapshot.exists()){
//                        val data = snapshot.getValue(Book::class.java)
//                        for (category in data!!.categories){
//                            Log.i("Firebase", category.toString());
//                        }
//                    }
//                }
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })

//        startActivityForResult(
//            AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(providers)
//                .setTheme(R.style.LoginTheme)
//                .setLogo(R.drawable.logo)
//                .build(), AUTH_REQUEST_CODE
//        )


        supportFragmentManager.commit {
            //add<ReadingFragment>(R.id.fragment_container_view)
            add<HomeFragment>(R.id.fragment_container_view)
//            setReorderingAllowed(true)
//            addToBackStack("name") // name can be null
        }
        val navBar = findViewById<NavigationBarView>(R.id.bottom_navigation)
        //Bottom Navigation Menu
            navBar.setOnItemSelectedListener{ item ->
            when(item.itemId) {
                R.id.item_1 -> {
                    // Respond to navigation item 1 click
                    supportFragmentManager.commit {
                        replace<HomeFragment>(R.id.fragment_container_view)
                        setReorderingAllowed(true)
                        addToBackStack("home") // name can be null
                    }
                    true
                }
                R.id.item_2 -> {
                    supportFragmentManager.commit {
                        replace<SearchBookFragment>(R.id.fragment_container_view)
                        setReorderingAllowed(true)
                        addToBackStack("searchBook") // name can be null
                    }                    // Respond to navigation item 2 click
                    true

                }
                R.id.item_3 -> {
                    // Respond to navigation item 3 click
                    supportFragmentManager.commit {
                        replace<AccountInformationFragment>(R.id.fragment_container_view)
                        setReorderingAllowed(true)
                        addToBackStack("accountInformation") // name can be null
                    }
                    true
                }
                R.id.item_4 -> {
                    // Respond to navigation item 2 click
                    //setCurrentFragment(Fragment01())
                    supportFragmentManager.commit {
                        replace<MyBooksFragment>(R.id.fragment_container_view)
                        setReorderingAllowed(true)
                        addToBackStack("myBooks") // name can be null
                    }

                    true
                }
                else -> false
            }
       }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTH_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // User successfully signed in
                //Add a check for user isAdmin if yes launch Activity



                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this@MainActivity, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()

                // Add user data to the database if it doesn't exist

                val database = FirebaseDatabase.getInstance()
                val usersRef = database.getReference("Users")
                val uid = user?.uid
                val name = user?.displayName
                val email = user?.email
                val userRef = usersRef.child(uid.toString())
                userRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)
                        userViewModel.user = user
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle errors here
                    }
                })
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        Log.e("Firebase", "onDataChange")
                        if (!dataSnapshot.exists()) {
                            // Get a reference to the current user's list of books
                            val userBooksRef = usersRef.child(uid.toString()).child("listOfBooks")

                        // Create a new UserBook object
                            val book = UserBook("bookID", "bookName", 1, 1, true, false, 1.0, "", "bookAuthor")

                             // Add the book to the list of books for the current user
                            //val newList = user?.listOfBooks.orEmpty().toMutableList()
                            val newList = mutableListOf<UserBook>()
                            newList.add(book)
                            //userRef.child("listOfBooks").setValue(newList)

                            val newUser = User(false,uid, email, name, newList)
                            userRef.setValue(newUser)

                        }

                        // update the fragment_account_information

                        val user = dataSnapshot.getValue(User::class.java)

                        userViewModel.user = user
                        if (user != null) {
                            if(user.admin == true){
                                startActivity(Intent(this@MainActivity, AdminMainActivity::class.java))
                            }

                            else{
                                // Reload to home fragment
                                supportFragmentManager.commit {
                                    replace<HomeFragment>(R.id.fragment_container_view)
                                    setReorderingAllowed(true)
                                    addToBackStack("home") // name can be null
                                }
                                //set nav bar
                                val navBar = findViewById<NavigationBarView>(R.id.bottom_navigation)
                                navBar.selectedItemId = R.id.item_1

                                //introUserTV.text = "Hi ${user?.name}"
                            }
                        }


                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("TAG", "onCancelled", databaseError.toException())
                    }
                })
                userRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)
                        userViewModel.user = user
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle errors here
                    }
                })
                //Get current user data from firebase and





            } else {
                // Sign in failed
                Toast.makeText(this@MainActivity, "Sign in failed. Please try again later.", Toast.LENGTH_SHORT).show()
                finish() // Close the app if the sign in failed
            }
        }
    }
}