package com.composecodelab.layoutscodelab2

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composecodelab.layoutscodelab2.ui.theme.LayoutsCodelab2Theme

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        // 1. children 측정
        val placeables = measurables.map { measurable ->
            // 자식들의 constraints 각각 측정 (한번만 계산)
            measurable.measure(constraints)
        }

        // Track the y co-ord we have placed children up to
        var yPosition = 0

        // 레이아웃 크기를 최대로 잡는다.
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children in the parent layout
            placeables.forEach { placeable ->
                // 2. parent 안에 위치시키기
                placeable.placeRelative(x = 0, y = yPosition)

                // 다음을 위해 y 좌표를 증가시킨다.
                yPosition += placeable.height
            }
        }
    }
}


// 맞춤형 레이아웃 만들기
// View 시스템에서는 사용자 정의 레이아웃을 생성하려면 ViewGroup 측정 및 레이아웃 기능을 확장하고 구현해야 했다.
// Compose에서는 Layout 컴포저블을 사용하여 함수를 작성하기만 하면 된다.

// Compose의 레이아웃 원칙
// 일부 Composable 메서드는 호출될 때 화면에 렌더링될 UI 트리에 추가되는 UI 조각을 리턴한다.
// 각각의 조각은 최소/최대의 weight/height를 스스로 계산해야 한다.
// 자식이 있는 경우 자체 크기를 결정하는데 도움이 되도록 각 요소를 측정할 수 있다.
// compose UI 는 다중 패스 측정을 허용하지 않는다. -> 자식을 두번 이상 측정하지 않을 수 있다.
// 단일 패스 측정은 성능이 좋고, Compose 가 깊은 UI 트리를 효율적으로 처리할 수 있게 한다.

// layout 수정자를 사용해서 요소를 측정하고 배치하는 방법을 수동으로 제어
fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp // 상단에서부터 첫번째 기준선까지의 거리
) = this.then(
    layout { measurable, constraints ->
        // measurable : 측정하고 배치할 대상(자식),
        // constraints : 자식의 너비와 높이에 대한 최소값과 최대값
        val placeable = measurable.measure(constraints)

        // Check the composable has a first baseline
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        // Height of the composable with padding - first baseline
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            // Where the composable gets placed
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
    LayoutsCodelab2Theme {
        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
    }
}

@Preview
@Composable
fun TextWithNormalPaddingPreview() {
    LayoutsCodelab2Theme {
        Text("Hi there!", Modifier.padding(top = 32.dp))
    }
}