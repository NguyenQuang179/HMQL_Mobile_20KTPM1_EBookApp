package com.example.hmql_ebookapp

import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    //keep track of user in Main Activity
    var user: User? = null
}
data class UserBook(
    val bookID: String? = null,
    val bookName: String? = null,
    val status: Int? = null,
    val readingProgress: Int? = null,
    val liked: Boolean? = null,
    val downloaded: Boolean? = null,
    val reviewScore: Double? = null,
    var cover: String? = null,
    var author: String? = null
)

data class UserReview(
    val userName: String? = null,
    val userAvatar: String? = null,
    val star: String? = null,
    val date: String? = null,
    val content: String? = null
)

data class User(
    val admin: Boolean? = null,
    val userID: String? = null,
    val userAvatar: String? = null,
    val email: String? = null,
    val name: String? = null,
    val listOfBooks: List<UserBook> = emptyList()

)