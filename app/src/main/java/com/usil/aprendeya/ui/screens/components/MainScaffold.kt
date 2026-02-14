package com.usil.aprendeya.ui.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.usil.aprendeya.ui.theme.NavBar

@Preview(showBackground = true)
@Composable
private fun MainScaffoldPreview() {
    val navItems = listOf(
        NavItem("Inicio", Icons.Default.Home),
        NavItem("Perfil", Icons.Default.Person)
    )

    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    MainScaffold(
        topTitle = "Matemática",
        onBackButtonPressed = {},
        navItems = navItems,
        selectedIndex = selectedIndex,
        onItemSelected = { selectedIndex = it },
        showTopBar = true,
        showBottomBar = true
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Contenido de prueba")
        }
    }
}

@Composable
fun MainScaffold(
    topTitle: String,
    onBackButtonPressed: () -> Unit,
    navItems: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    showTopBar: Boolean,
    showBottomBar: Boolean,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            if (showTopBar) TopBar(
                title = topTitle,
                onBackButtonPressed = onBackButtonPressed
            )
        },
        bottomBar = {
            if (showBottomBar) {
                BottomBar(
                    items = navItems,
                    selectedIndex = selectedIndex,
                    onItemSelected = onItemSelected
                )
            }
        }
    ) { padding ->
        content(padding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onBackButtonPressed: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(
                onClick = onBackButtonPressed
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Atrás"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = NavBar,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun BottomBar(
    items: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = NavBar
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(item.icon, contentDescription = item.label)
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,
                    indicatorColor = Color.Blue
                )
            )
        }
    }
}
