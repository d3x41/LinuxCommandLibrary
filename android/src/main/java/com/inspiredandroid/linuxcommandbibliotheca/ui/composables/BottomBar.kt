package com.inspiredandroid.linuxcommandbibliotheca.ui.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.inspiredandroid.linuxcommandbibliotheca.Screen
import com.inspiredandroid.linuxcommandbibliotheca.ui.theme.LocalCustomColors

/* Copyright 2022 Simon Schubert
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

val bottomBarItems = listOf(
    Screen.Basics,
    Screen.Tips,
    Screen.Commands,
)

@Composable
fun BottomBar(
    navController: NavHostController,
    resetSearch: () -> Unit,
) {
    // TODO: read current route from navcontroller
    val selectedRoute = rememberSaveable { mutableStateOf(Screen.Basics.route) }

    BottomNavigation(
        backgroundColor = LocalCustomColors.current.navBarBackground,
        elevation = 0.dp,
    ) {
        bottomBarItems.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.drawableRes),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                label = { Text(stringResource(screen.resourceId)) },
                selected = screen.route == selectedRoute.value,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onSurface,
                onClick = {
                    while (navController.currentBackStackEntry?.destination?.route?.startsWith("command?") == true) {
                        navController.popBackStack()
                    }
                    navController.navigate(screen.route) {
                        selectedRoute.value = screen.route
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    resetSearch()
                },
            )
        }
    }
}
