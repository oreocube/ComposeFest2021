package com.example.basicscodelab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basicscodelab2.ui.theme.BasicsCodelab2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            BasicsCodelab2Theme {
//                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//                    Greeting("Android")
//                }
//            }

            MessageCard(Message("Android", "Jetpack Compose"))
        }
    }
}

data class Message(val author: String, val body: String)

@Composable // composable 함수 정의
fun MessageCard(msg: Message) {
    // 메시지에 패딩 넣기
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                // 이미지 사이즈를 40dp로 조절
                .size(40.dp)
                // 이미지를 동그라미 모양으로 자르기
                .clip(CircleShape)
        )

        // Image 와 Column 사이에 가로로 공간 띄우기
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = msg.author)
            // author, body 텍스트 사이 수직으로 공간 띄우기
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = msg.body)
        }
    }

}

@Preview
@Composable
fun PreviewMessageCard() {
    // MessageCard() 함수를 직접 미리 볼 수는 없다. @Preview 주석을 추가하면 볼수 있다.
    MessageCard(
        msg = Message("Colleague", "Hey, take a look at Jetpack Compose")
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BasicsCodelab2Theme {
        Greeting("Android")
    }
}