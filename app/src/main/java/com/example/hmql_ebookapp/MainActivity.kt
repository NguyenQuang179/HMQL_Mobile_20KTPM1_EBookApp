package com.example.hmql_ebookapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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
            AuthUI.IdpConfig.PhoneBuilder().build(), //Phone Builder

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
                        .build(), AUTH_REQUEST_CODE
                )
            }
        }
    }
//            RC_SIGN_IN
//        )
//    }

    // Register function
    public fun register(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful, do something here
                } else {
                    // Registration failed, display error message
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
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
                        .build(),
                    AUTH_REQUEST_CODE
                )
            }
    }
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {




        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme) //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.lightTheme)  //default app theme
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init();
//        val category1 = Category("tieu thuyet");
//        val category2 = Category("van hoc");
//        val newBook = Book("122fskjd3fsdffsadffsdafsaff11","Ten sach","Tac gia","Mo ta",2020,4.9, 200,"cover","pdf", arrayListOf(category1, category2))
//        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("book")
//        ref.child(newBook.bookID).setValue(newBook) { databaseError, databaseReference ->
//            if (databaseError != null) {
//                Log.e("Firebase", "Error adding book: ${databaseError.message}")
//            } else {
//                Log.i("Firebase", "Book added successfully")
//            }
//        }
//
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
//                .build(), AUTH_REQUEST_CODE
//        )
//     val intent = Intent(this, ReadingScreen::class.java)
//        startActivity(intent)

        supportFragmentManager.commit {
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
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this@MainActivity, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()
            } else {
                // Sign in failed
                Toast.makeText(this@MainActivity, "Sign in failed. Please try again later.", Toast.LENGTH_SHORT).show()
                finish() // Close the app if the sign in failed
            }
        }
    }
}