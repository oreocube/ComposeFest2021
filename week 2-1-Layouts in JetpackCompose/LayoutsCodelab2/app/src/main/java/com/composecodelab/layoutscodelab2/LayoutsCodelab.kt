package com.composecodelab.layoutscodelab2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composecodelab.layoutscodelab2.ui.theme.LayoutsCodelab2Theme

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

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
    Row(
        modifier = modifier
            .background(color = Color.LightGray, shape = RectangleShape)
            .padding(16.dp)
            .size(200.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        StaggeredGrid {
            for (topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}

@Preview
@Composable
fun LayoutsCodelabPreview() {
    LayoutsCodelab2Theme {
        BodyContent()
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        /* 1. 자식을 measure */
        // 각 행의 너비를 추적
        val rowWidths = IntArray(rows) { 0 }

        // 각 행의 최대 높이를 추적
        val rowHeights = IntArray(rows) { 0 }

        // Don't constrain child views further, measure them with given constraints
        // 측정된 children 리스트
        val placeables = measurables.mapIndexed { index, measurable ->

            // Measure each child
            val placeable = measurable.measure(constraints)

            // Track the width and max height of each row
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)

            placeable
        }

        /* 2. 실제로 배치하기 전에 격자의 크기(전체 너비 및 높이)를 계산 */

        // 격자의 너비는 가장 넓은 너비
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // 격자의 높이는 각 행에서 가장 키 큰 요소의 합
        // 높이 제약조건으로 강제
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // 각 row의 Y 값은 이전 행들의 높이를 축적한 값을 이용해 계산
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowHeights[i - 1]
        }

        /* 3. 부모 레이아웃에 배치 */
        // 부모 레이아웃의 크기를 정한다.
        layout(width, height) {
            // x cord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun ChipPreview() {
    LayoutsCodelab2Theme {
        Chip(text = "Hi there")
    }
}