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

}