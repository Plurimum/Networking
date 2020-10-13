package com.example.networking

data class Request(val response: Response)

data class Response(val count: Int, val items: Array<Item>)

data class Item(val album_id: Int, val date: Int, val id: Int
                , val owner_id: Int, val has_tags: Boolean, val post_id: Int, val sizes: Array<Size>
                , val text: String, val user_id: Int)

data class Size(val height: Int, val url: String, val type: String, val width: Int)