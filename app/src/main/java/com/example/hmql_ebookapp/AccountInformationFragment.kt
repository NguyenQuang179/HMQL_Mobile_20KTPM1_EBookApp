package com.example.hmql_ebookapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountInformationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_account_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        val signoutButton = view.findViewById<TextView>(R.id.signOutTv)
        signoutButton.setOnClickListener {
            (activity as MainActivity).logout(requireActivity())
        }
        var settingTv = view.findViewById<TextView>(R.id.settingTv)
        settingTv.setOnClickListener(){
            requireActivity().supportFragmentManager.commit {
                replace<SettingFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
                addToBackStack("settingFragment") // name can be null
            }
        }

        val userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        val user = userViewModel.user
        val accountNameTV = view.findViewById<TextView>(R.id.Reviewer_nameTV)
        val accountEmailTV = view.findViewById<TextView>(R.id.emailTv)
        //and in fragment_home
        accountNameTV.text = user?.name
        accountEmailTV.text = user?.email
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountInformationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountInformationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    val AUTH_REQUEST_CODE = 7192 // Any number you want
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var listener:FirebaseAuth.AuthStateListener
    lateinit var providers:List<AuthUI.IdpConfig>
    public fun init(){
        providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), //Email Builder
            AuthUI.IdpConfig.GoogleBuilder().build(), //Google Builder
            //AuthUI.IdpConfig.FacebookBuilder().build(), //Facebook Builder
            //AuthUI.IdpConfig.PhoneBuilder().build(), //Phone Builder

        )
        firebaseAuth = FirebaseAuth.getInstance()
        listener = FirebaseAuth.AuthStateListener { myFirebaseAuth ->
            val user = myFirebaseAuth.currentUser
            if(user != null){
                //User already logged in
                Toast.makeText(requireContext(), "Welcome Back", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), "Opening login screen", Toast.LENGTH_SHORT).show()
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
}