/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.rally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navDeepLink
import com.example.compose.rally.RallyScreen.*
import com.example.compose.rally.data.UserData
import com.example.compose.rally.ui.accounts.AccountsBody
import com.example.compose.rally.ui.accounts.SingleAccountBody
import com.example.compose.rally.ui.bills.BillsBody
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.overview.OverviewBody
import com.example.compose.rally.ui.theme.RallyTheme

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        val allScreens = RallyScreen.values().toList()
        // rememberSaveable을 사용하면 구성변경(화면 회전)에서도 NavController가 살아남는다.
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = RallyScreen.fromRoute(
            backstackEntry.value?.destination?.route
        )
        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = allScreens,
                    onTabSelected = { screen ->
                        navController.navigate(screen.name)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            RallyNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Overview.name,
        modifier = modifier
    ) {
        composable(Overview.name) {
            OverviewBody(
                onClickSeeAllAccounts = { navController.navigate(Accounts.name) },
                onClickSeeAllBills = { navController.navigate(Bills.name) },
                onAccountClick = { name ->
                    navigateToSingleAccount(navController, name)
                }
            )
        }
        composable(Accounts.name) {
            AccountsBody(accounts = UserData.accounts) { name ->
                navigateToSingleAccount(
                    navController = navController,
                    accountName = name
                )
            }
        }
        composable(Bills.name) {
            BillsBody(bills = UserData.bills)
        }

        // 행을 클릭하면 개별 계좌의 세부 정보를 표시하는 Account 화면을 추가하자

        // RallyActivity에서 Accounts/{name} 인자를 사용하여
        // NavHost에 새 컴포저블을 추가
        val accountsName = Accounts.name
        composable(
            // 새 목적지
            "$accountsName/{name}",
            // 새 목적지에 대한 navArguments 목록
            arguments = listOf(
                // String 타입의 "name"이라는 단일 인자를 정의
                navArgument("name") {
                    type = NavType.StringType
                }
            ),
            // navDeepLink 기능을 사용하여 딥링크 목록을 추가한다.
            // uriPattern을 전달하고, 위의 인텐트 필터에 대해 일치하는 uri를 제공한다.
            // deepLinks 매개변수를 사용하여 생성된 딥 링크를 컴포저블에 전달한다.
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "rally://$accountsName/{name}"
                }
            )
        ) { entry ->
            // 컴포저블의 바디

            // 각 컴포저블의 body는 현재 목적지의 경로와 인자를 모델링하는
            // 현 NavBackStackEntry의 매개변수를 수신한다.
            // arguments를 사용하여 인자를 가져올 수 있다.

            // NavBackStackEntry의 인자들 중 "name"을 검색한다.
            val accountName = entry.arguments?.getString("name")
            // UserData에서 일치하는 첫번째 이름을 찾는다.
            val account = UserData.getAccount(accountName)
            // 계좌를 SingleAccountBody로 전달한다.
            SingleAccountBody(account = account)
        } // end of composable

    }
}

private fun navigateToSingleAccount(
    navController: NavHostController,
    accountName: String
) {
    // navController를 사용하여 컴포저블로 이동할 수 있다.
    navController.navigate("${Accounts.name}/$accountName")
}
