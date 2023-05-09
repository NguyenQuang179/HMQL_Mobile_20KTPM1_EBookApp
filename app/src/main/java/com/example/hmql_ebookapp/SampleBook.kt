package com.example.hmql_ebookapp

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
    var categories: List<Category> = emptyList()
) {
    constructor() : this("", "", "", "", 0, 0.0, 0, "", "", emptyList())
}



data class SampleBook(val bookName: String, val authorName: String, val bookImg : Int)