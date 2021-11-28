package com.example.compose.rally

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RallyNavHostTest {
    // 컴포즈 테스트 규칙 생성
    @get: Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: NavHostController

    // 테스트보다 먼저 실행
    @Before
    fun setupRallyNavHost() {
        composeTestRule.setContent {
            navController = rememberNavController()
            RallyNavHost(navController = navController)
        }
    }

    // Test 어노테이션을 달고 public 테스트 함수를 만든다.
    // 해당 함수에서 테스트할 내용을 설정해야 한다.
    @Test
    fun rallyNavHost() {
        // OverviewScreen이 표시되는지 확인해보기
        composeTestRule
            .onNodeWithContentDescription("Overview Screen")
            .assertIsDisplayed()
    }

    // UI 및 테스트 규칙을 통한 테스트
    // Accounts Screen으로 연결되는 All Accounts 버튼을 클릭하는 테스트
    @Test
    fun rallyNavHost_navigateToAllAccounts_viaUI() {
        composeTestRule
            .onNodeWithContentDescription("All Accounts")
            .performClick()
        composeTestRule
            .onNodeWithContentDescription("Accounts Screen")
            .assertIsDisplayed()
    }

    // UI 및 navController를 통한 테스트
    @Test
    fun rallyNavHost_navigateToBills_viaUI() {
        // "All Bills"를 클릭할 때
        composeTestRule.onNodeWithContentDescription("All Bills").apply {
            performScrollTo()
            performClick()
        }
        // 현재 경로가 "Bills"인지 확인
        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "Bills")
    }

    // navController를 통한 테스트
    // navController.navigate를 직접호출하는 것
    // 주의사항 - UI 스레드에서 호출해야 한다.

    // 메인 스레드 디스패처와 함께 코루틴을 사용해 수행할 수 있다.
    // 그리고 새로운 상태에 대한 Assertion을 만들기 전에 호출이 발생해야 하므로,
    // runBlocking 호출로 래핑해야 한다.
    @Test
    fun rallyNavHost_navigateToAllAccounts_callingNavigate() {
        runBlocking {
            withContext(Dispatchers.Main) {
                navController.navigate(RallyScreen.Accounts.name)
            }
        }
        composeTestRule
            .onNodeWithContentDescription("Accounts Screen")
            .assertIsDisplayed()
    }
}