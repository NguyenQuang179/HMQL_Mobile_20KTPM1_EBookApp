package com.example.hmql_ebookapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    //signing
    val AUTH_REQUEST_CODE = 7192 // Any number you want
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var listener:FirebaseAuth.AuthStateListener
    lateinit var providers:List<AuthUI.IdpConfig>
    private fun init(){
        providers = arrayListOf(
AuthUI.IdpConfig.EmailBuilder().build(), //Email Builder
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

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.LoginTheme)
                .build(), AUTH_REQUEST_CODE
        )
//     val intent = Intent(this, ReadingScreen::class.java)
//        startActivity(intent)

//        supportFragmentManager.commit {
//            add<HomeFragment>(R.id.fragment_container_view)
////            setReorderingAllowed(true)
////            addToBackStack("name") // name can be null
//        }
//        val navBar = findViewById<NavigationBarView>(R.id.bottom_navigation)
//        //Bottom Navigation Menu
//            navBar.setOnItemSelectedListener{ item ->
//            when(item.itemId) {
//                R.id.item_1 -> {
//                    // Respond to navigation item 1 click
//                    supportFragmentManager.commit {
//                        replace<HomeFragment>(R.id.fragment_container_view)
//                        setReorderingAllowed(true)
//                        addToBackStack("home") // name can be null
//                    }
//                    true
//                }
//                R.id.item_2 -> {
//                    supportFragmentManager.commit {
//                        replace<SearchBookFragment>(R.id.fragment_container_view)
//                        setReorderingAllowed(true)
//                        addToBackStack("searchBook") // name can be null
//                    }                    // Respond to navigation item 2 click
//                    true
//
//                }
//                R.id.item_3 -> {
//                    // Respond to navigation item 3 click
//                    supportFragmentManager.commit {
//                        replace<AccountInformationFragment>(R.id.fragment_container_view)
//                        setReorderingAllowed(true)
//                        addToBackStack("accountInformation") // name can be null
//                    }
//                    true
//                }
//                R.id.item_4 -> {
//                    // Respond to navigation item 2 click
//                    //setCurrentFragment(Fragment01())
//                    supportFragmentManager.commit {
//                        replace<MyBooksFragment>(R.id.fragment_container_view)
//                        setReorderingAllowed(true)
//                        addToBackStack("myBooks") // name can be null
//                    }
//
//                    true
//                }
//                else -> false
//            }
//        }
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