package com.xavier.calculator.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xavier.calculator.R
import com.xavier.calculator.api.EQUAL_TOKEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(
    viewModel: CalculatorViewModel,
    open: Boolean,
    onClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        TopBar(stringResource(id = R.string.history_record), open, onClicked)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(MaterialTheme.colorScheme.outline)
        )

        val history by viewModel.history.collectAsState(initial = listOf())
        if (history.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(2 / 3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_empty),
                    contentDescription = stringResource(id = R.string.empty)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(id = R.string.empty_history_record))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                /*reverseLayout = true,*/
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = history) {
                    OutlinedCard(onClick = {
                        viewModel.onItemClicked(it)
                        onClicked()
                    }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp, 12.dp)
                        ) {
                            Text(text = it.formula)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "$EQUAL_TOKEN ${it.outcome}")
                        }
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.clearHistory()
                    onClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
            ) {
                Text(
                    text = stringResource(id = R.string.clear_history),
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}