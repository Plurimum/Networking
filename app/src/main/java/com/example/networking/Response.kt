package com.example.networking

data class Request(val response: Response)

data class Response(val count: Int, val items: Array<Item>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Response

        if (count != other.count) return false
        if (!items.contentEquals(other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = count
        result = 31 * result + items.contentHashCode()
        return result
    }
}

data class Item(val album_id: Int, val date: Int, val id: Int
                , val owner_id: Int, val has_tags: Boolean, val post_id: Int, val sizes: Array<Size>
                , val text: String, val user_id: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (album_id != other.album_id) return false
        if (date != other.date) return false
        if (id != other.id) return false
        if (owner_id != other.owner_id) return false
        if (has_tags != other.has_tags) return false
        if (post_id != other.post_id) return false
        if (!sizes.contentEquals(other.sizes)) return false
        if (text != other.text) return false
        if (user_id != other.user_id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = album_id
        result = 31 * result + date
        result = 31 * result + id
        result = 31 * result + owner_id
        result = 31 * result + has_tags.hashCode()
        result = 31 * result + post_id
        result = 31 * result + sizes.contentHashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + user_id
        return result
    }
}

data class Size(val height: Int, val url: String, val type: String, val width: Int)