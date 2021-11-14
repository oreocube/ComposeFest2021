package com.composecodelab.layoutscodelab2

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.composecodelab.layoutscodelab2.ui.theme.LayoutsCodelab2Theme

@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {

        // 제약 주고 싶은 컴포넌트
        val (button1, button2, text) = createRefs()

        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button1) {
                top.linkTo(parent.top, margin = 16.dp) // 버튼 위쪽에 16dp 마진
            }
        ) {
            Text("Button 1")
        }

        Text("Text", Modifier.constrainAs(text) {
            top.linkTo(button1.bottom, margin = 16.dp)
            centerAround(button1.end)
        })

        val barrier = createEndBarrier(button1, text)
        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ) {
            Text("Button 2")
        }
    }
}

@Preview
@Composable
fun ConstraintLayoutContentPreview() {
    LayoutsCodelab2Theme {
        ConstraintLayoutContent()
    }
}

@Composable
fun LargeConstraintLayout() {
    ConstraintLayout {
        val text = createRef()

        val guideline = createGuidelineFromStart(fraction = 0.5f)
        Text(
            "This is a very very very very very very very long text",
            Modifier.constrainAs(text) {
                linkTo(start = guideline, end = parent.end)
                width = Dimension.preferredWrapContent // 분명히 사용 가능한 공간에서 줄바꿈
            }
        )
    }
}

@Preview
@Composable
fun LargeConstraintLayoutPreview() {
    LayoutsCodelab2Theme {
        LargeConstraintLayout()
    }
}

// 지금까지 예제에서는 제약 조건이 적용되는 컴포저블의 수정자와 함께 인라인으로 지정
// 그러나 제약 조건이 적용되는 레이아웃에서 분리된 상태로 유지하는 것이 중요한 경우가 존재

// 이러한 경우 ConstraintLayout에는 다른 방식으로 사용 가능

// 1. A의 패스 ConstraintSet에 매개 변수로 ConstraintLayout.
// 2. 수정자를 ConstraintSet사용하여 컴포저블에 생성된 참조를 할당합니다 layoutId.
// ConstraintLayout위 의 첫 번째 예에 적용된 이 API 모양 은 화면 너비에 최적화되어 다음과 같습니다.
@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (maxWidth < maxHeight) {
            decoupledConstraints(margin = 16.dp) // Portrait constraints
        } else {
            decoupledConstraints(margin = 32.dp) // Landscape constraints
        }

        ConstraintLayout(constraints) {
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.layoutId("button")
            ) {
                Text("Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
        }
    }
}