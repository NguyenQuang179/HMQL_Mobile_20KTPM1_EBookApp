package com.example.hmql_ebookapp

data class Author (
    var name: String = "",
    var authorID: String = "",
    var description: String = "",
    var img:String = "",
    var listOfBooks: List<String> = emptyList<String>()
)