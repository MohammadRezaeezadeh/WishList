package com.example.wishlist

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wishlist.Data.Wish
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navcontroller: NavController,
    viewmodel: WishViewModel
) {
    val contxt = LocalContext.current
    val isNavigating = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            topAppBar(
                title = "WishList",
                onBackNavClick = {
                    Toast.makeText(contxt, "OK", Toast.LENGTH_LONG).show()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(4.dp),
                contentColor = colorResource(id = R.color.white),
                containerColor = colorResource(id = R.color.anwser),
                onClick = {
                    if (!isNavigating.value) {
                        isNavigating.value = true
                        navcontroller.navigate("${Screen.AddScreen.route}/0L")
                        isNavigating.value = false
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Wish")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        val wishList = viewmodel.getAllWishes.collectAsState(initial = emptyList())
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            if (wishList.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "لیست آرزوها خالی است",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(wishList.value, key = { it.id }) { wish ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { dismissValue ->
                                if (dismissValue == SwipeToDismissBoxValue.EndToStart || dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                                    scope.launch {
                                        viewmodel.deleteWish(wish)
                                        snackbarHostState.showSnackbar("آرزو حذف شد")
                                    }
                                    true
                                } else {
                                    false
                                }
                            },
                            positionalThreshold = { totalWidth -> totalWidth * 0.25f } // حذف با کشیدن 25 درصد عرض صفحه
                        )
                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                // پس‌زمینه فقط هنگام کشیدن قرمز می‌شود
                                val backgroundColor = if (dismissState.currentValue == SwipeToDismissBoxValue.Settled) {
                                    colorResource(id = R.color.white) // هم‌رنگ کارت در حالت عادی
                                } else {
                                    Color.Red // قرمز هنگام کشیدن
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(backgroundColor)
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.White
                                        )
                                    }
                                }
                            },
                            content = {
                                wishItem(wish, isNavigating) {
                                    if (!isNavigating.value) {
                                        isNavigating.value = true
                                        navcontroller.navigate("${Screen.AddScreen.route}/${wish.id}")
                                        isNavigating.value = false
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun wishItem(wish: Wish, isNavigating: MutableState<Boolean>, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .clickable(enabled = !isNavigating.value) { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = wish.title,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.End
            )
            Text(
                text = wish.description,
                fontWeight = FontWeight.ExtraLight,
                textAlign = TextAlign.End
            )
        }
    }
}