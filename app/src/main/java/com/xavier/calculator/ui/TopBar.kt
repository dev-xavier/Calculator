package com.xavier.calculator.ui

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.xavier.calculator.R

@Composable
fun TopBar(titleText :String,open: Boolean, onClicked: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(text = titleText) },
        navigationIcon = {
            IconButton(onClick = onClicked) {
                Icon(
                    painter = if (open) painterResource(id = R.drawable.baseline_menu_open_24) else painterResource(
                        id = R.drawable.baseline_menu_24
                    ), contentDescription = stringResource(id = R.string.calculate)
                )
            }
        }
    )
}