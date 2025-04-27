package com.example.wishlist.Data

import kotlinx.coroutines.flow.Flow

class WishRepository(private val wishDao: WishDao) {

    suspend fun addWish(wish: Wish) {
        wishDao.addWish(wish)
    }

    suspend fun getAllWishes(): Flow<List<Wish>> = wishDao.getAllWishes()


     fun getWishById(id: Long): Flow<Wish>
    {
        return wishDao.getWishById(id)
    }
    suspend fun updateAWish(wish: Wish)
    {
        wishDao.updateWish(wish)
    }
    suspend fun deleteAWish(wish: Wish)
    {
        wishDao.deleteWish(wish)
    }




}