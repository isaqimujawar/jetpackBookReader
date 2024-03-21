package com.maddy.jetpackbookreader.common.model

data class UserBookReader(
    val userId: String,
    val displayName: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val quote: String,
    val avatarUrl: String,
    val profession: String,
    val id: String?,
) {

    /**
     *
     * Maps new user as a Map<String, Any>, because Firebase stores data as a HashMap.
     */
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            // key to value
            "user_id" to this.userId,
            "display_name" to this.displayName,
            "email" to this.email,
            "first_name" to this.firstName,
            "last_name" to this.lastName,
            "quote" to this.quote,
            "avatar_url" to this.avatarUrl,
            "profession" to this.profession
        )
    }
}