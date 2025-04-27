package com.example.wishlist.Data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Wish::class],
    exportSchema = false,
    version = 1

)
abstract class WishDatabase: RoomDatabase() {

    abstract fun wishDao(): WishDao



}