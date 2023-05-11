package com.example.hmql_ebookapp


data class UserBook(
    val bookID: String,
    val bookName: String,
    val status: String,
    val readingProgress: Int,
    val isLiked: Boolean,
    val isDownloaded: Boolean
)

data class User(
    val userID: String?,
    val email: String?,
    val username: String?,
    val listOfBooks: List<UserBook> = emptyList()
)