package com.composecodelab.layoutscodelab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
fun LayoutsCodelab() {
    Scaffold(
        // Scaffold 는 기본적인 material design 레이아웃 구조를 구현한다.
        // 여러 compose 객체를 이용하여 화면을 구성하는 객체
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "LayoutsCodelab")
                },
                actions = {
                    // TopAppBar 에 추가 메뉴 아이콘을 넣을 수 있다.
                    // 여러개 넣을 수도 있다.
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(8.dp) // extra padding
        // extra padding 을 BodyContent 의 Column 에서 지정할 수도 있고
        // BodyContent 를 호출할 때 지정할 수도 있다. -> 일괄 적용할 때 유용
        // 경우에 맞게 활용 가능하다.
    ) {
        Text(text = "Hi there!")
        Text(text = "Thanks for going through the Layouts codelab")
    }
}

@Preview
@Composable
fun LayoutsCodelabPreview() {
    LayoutsCodelab2Theme {
        LayoutsCodelab()
    }
}