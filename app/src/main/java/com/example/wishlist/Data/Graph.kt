package com.example.wishlist.Data

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: WishDatabase
    val wishRepository by lazy {

            WishRepository(wishDao = database.wishDao())

    }
    fun provide(context:Context)
    {
        database = Room.databaseBuilder(context, WishDatabase::class.java,"wishList.dp").build()

    }
}