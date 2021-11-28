package com.example.compose.rally

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import org.junit.Rule
import org.junit.Test
import java.util.*

class TopAppBarTest {

    @get: Rule
    val composeTestRule = createComposeRule()


    @Test
    fun rallyTopAppBarTest() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }
        Thread.sleep(5000)
    }

    @Test
    fun rallyTopAppBarTest_currentTabSelected() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()
    }

    @Test
    fun rallyTopAppBarTest_currentLabelExists() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }

        // 탭 안의 Text가 표시되는지 여부를 확인하기 위해,
        // 병합되지 않은 시맨틱 트리를 쿼리하여 useUnmergedTree = true를
        // onRoot라는 finder에 전달할 수 있다.
        // composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")

        composeTestRule
            .onNode(
                hasText(RallyScreen.Accounts.name.uppercase(Locale.getDefault())) and
                        hasParent(
                            hasContentDescription(RallyScreen.Accounts.name)
                        ),
                useUnmergedTree = true
            )
            .assertExists()
    }

    // 액션을 사용하여 RallyTopAppBar의 다른 탭을 클릭하면 선택 항목이 변경되는지 확인해보기
    // 힌트
    // 테스트 범위에는 RallyApp이 소유한 상태가 포함되어야 한다.
    // 행동이 아니라 상태를 확인하자. 호출된 객체와 방법에 의존하는 대신
    // UI 상태에 대한 assertion을 사용한다.

    @Test
    fun rallyTopAppBarTest_clickOtherTabs() {
        var currentScreen: RallyScreen = RallyScreen.Overview

        composeTestRule.setContent {
            RallyApp(currentScreen) { screen ->
                currentScreen = screen
            }
        }

        RallyScreen.values().forEach { screen->
            composeTestRule
                .onNodeWithContentDescription(screen.name)
                .performClick()
            assert(currentScreen == screen)
        }
    }
}