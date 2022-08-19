package com.xavier.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xavier.calculator.api.Injection
import com.xavier.calculator.ui.CalculatorScreen
import com.xavier.calculator.ui.CalculatorViewModel
import com.xavier.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this, Injection.provideViewModelFactory(
                context = this,
                owner = this
            )
        )[CalculatorViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val systemUiController = rememberSystemUiController()
                    val useDarkIcons = !isSystemInDarkTheme()
                    val surfaceColor = MaterialTheme.colorScheme.surface

                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = surfaceColor, darkIcons = useDarkIcons
                        )
                    }

                    CalculatorScreen(viewModel)
                }
            }
        }
    }
}