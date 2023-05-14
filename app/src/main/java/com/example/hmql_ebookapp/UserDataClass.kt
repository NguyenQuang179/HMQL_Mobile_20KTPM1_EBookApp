package com.example.hmql_ebookapp

import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    //keep track of user in Main Activity
    var user: User? = null
}
data class UserBook(
    val bookID: String? = null,
    val bookName: String? = null,
    val status: String? = null,
    val readingProgress: Int? = null,
    val liked: Boolean? = null,
    val downloaded: Boolean? = null
)

data class User(
    val admin: Boolean? = null,
    val userID: String? = null,
    val email: String? = null,
    val name: String? = null,
    val listOfBooks: List<UserBook> = emptyList()

)