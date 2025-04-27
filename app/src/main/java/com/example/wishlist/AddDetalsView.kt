package com.example.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.wishlist.Data.Wish
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDetalsView(
    id: Long,
    viewmodel: WishViewModel,
    navcontroller: NavHostController
){
   val snackMessage = remember { mutableStateOf("Tanks") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    if(id!=0L)
    {
        val wish = viewmodel.getAWishById(id).collectAsState(initial = Wish(0L,"",""))
        viewmodel.wishtitlestate = wish.value.title
        viewmodel.wishdiscription = wish.value.description



    }else{
        viewmodel.wishtitlestate =""
        viewmodel.wishdiscription =""
    }


    Scaffold(
        topBar = { topAppBar(title = if (id!=0L) stringResource(id=R.string.Update_Wish)else stringResource(id=R.string.Add_Wish), onBackNavClick = {
            navcontroller.navigateUp()
        }) }
    ) {
        innerpading->
        Column(modifier = Modifier.padding(innerpading).wrapContentSize()
        , horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.height(10.dp))
            WishTextField(label = "Title", value = viewmodel.wishtitlestate, onValueChange = {viewmodel.onwishtitlechang(it)})
            WishTextField(label = "Details", value = viewmodel.wishdiscription, onValueChange = {viewmodel.onwisdescriptionhchange(it)})
            Spacer(modifier = Modifier.height(10.dp))
            Button(colors = ButtonDefaults.buttonColors(colorResource(id = R.color.button))
                ,onClick = {
                    if(viewmodel.wishtitlestate.isNotEmpty()&&viewmodel.wishtitlestate.isNotEmpty())
                    {
                        if (id!=0L){//update
                           viewmodel.updateAWish(
                               Wish(
                                   id= id,
                                   title = viewmodel.wishtitlestate.trim(),
                                   description = viewmodel.wishdiscription.trim()
                               )
                           )

                        }else{//add
                           viewmodel.addWish(
                               Wish(
                                   title = viewmodel.wishtitlestate.trim(),
                                   description = viewmodel.wishdiscription.trim()
                               )
                           )
                        }
                        scope.launch {
                            snackbarHostState.showSnackbar(snackMessage.value)


                        }

                    }

                    navcontroller.navigateUp()



                }
            ) {
                Text(text = if (id!=0L) stringResource(id=R.string.Update_Wish)else stringResource(id=R.string.Add_Wish)

                )



            }


        }
    }
}

@Composable
fun WishTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = Color.Black) },
        modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),

    )

}
