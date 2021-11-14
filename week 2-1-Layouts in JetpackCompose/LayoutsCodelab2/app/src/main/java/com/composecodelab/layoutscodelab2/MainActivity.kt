package com.composecodelab.layoutscodelab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.composecodelab.layoutscodelab2.ui.theme.LayoutsCodelab2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsCodelab2Theme {
                LayoutsCodelab()
            }
        }
    }
}
