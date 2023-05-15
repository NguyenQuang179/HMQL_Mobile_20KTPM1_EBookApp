package com.example.hmql_ebookapp

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Serializable
import java.util.Date

data class Book(
    var bookID: String = "",
    var title: String = "",
    var author: String = "",
    var description: String = "",
    var year: Int = 0,
    var averageStar: Double = 0.0,
    var views: Int = 0,
    var cover: String = "",
    var pdf: String = "",
    var categories: List<Category> = emptyList(),
    var reviews: List<Review> = emptyList()
) : Comparable<Book> {
    override fun compareTo(other: Book): Int = this.averageStar.compareTo(other.averageStar)
    constructor() : this("", "", "", "", 0, 0.0, 0, "", "", emptyList())
}

data class Review(
    var userName: String,
    var userAvatar: String,
    val rating : String,
    var content: String,
    var datetime: String
): Comparable<Review> {
    override fun compareTo(other: Review): Int = this.datetime.compareTo(other.datetime)
    constructor() : this("", "", "0","0", "",)

}

@kotlinx.serialization.Serializable
data class recentSearch(
    val searchs: ArrayList<String>
        ): Serializable



data class SampleBook(var bookName: String, var authorName: String, var bookImg : Int) : Serializable
