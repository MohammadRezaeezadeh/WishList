package com.example.wishlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wishlist.Data.Graph
import com.example.wishlist.Data.Wish
import com.example.wishlist.Data.WishRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch



class WishViewModel(private val wishRepository: WishRepository= Graph.wishRepository): ViewModel() {
    var wishtitlestate by mutableStateOf("")

    var wishdiscription by mutableStateOf("")




    fun onwishtitlechang(newstring: String)
    {
        wishtitlestate = newstring
    }

    fun onwisdescriptionhchange(newstring: String)
    {
        wishdiscription = newstring
    }
    lateinit var getAllWishes: Flow<List<Wish>>
    init {
        viewModelScope.launch {
         getAllWishes =wishRepository.getAllWishes()
        }
    }
    fun addWish(wish: Wish)
    {
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.addWish(wish = wish)
        }
    }
    fun updateAWish(wish: Wish)
    {
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.updateAWish(wish)
        }
    }
    fun getAWishById(id: Long): Flow<Wish>
    {
        return wishRepository.getWishById(id)
    }
    fun deleteWish(wish: Wish)
    {
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.deleteAWish(wish)
        }
    }

}