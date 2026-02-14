package com.usil.aprendeya.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.google.firebase.auth.FirebaseUser
import com.usil.aprendeya.data.model.Curso
import com.usil.aprendeya.ui.navigation.Routes.Home
import com.usil.aprendeya.ui.navigation.Routes.Login
import com.usil.aprendeya.ui.navigation.Routes.Profile
import com.usil.aprendeya.ui.screens.alumno.CursoScreen
import com.usil.aprendeya.ui.screens.alumno.HomeScreen
import com.usil.aprendeya.ui.screens.alumno.ProfileScreen
import com.usil.aprendeya.ui.screens.alumno.TutoriaScreen
import com.usil.aprendeya.ui.screens.components.MainScaffold
import com.usil.aprendeya.ui.screens.components.NavItem
import com.usil.aprendeya.ui.screens.components.NavigationEvent
import com.usil.aprendeya.ui.screens.login.LoginScreen
import com.usil.aprendeya.viewModel.alumno.CursoViewModel
import com.usil.aprendeya.viewModel.alumno.HomeViewModel
import com.usil.aprendeya.viewModel.alumno.ProfileViewModel
import com.usil.aprendeya.viewModel.alumno.TutoriaViewModel
import com.usil.aprendeya.viewModel.login.LoginViewModel
import kotlinx.coroutines.flow.merge

@Composable
fun NavigationWrapper(
    currentUser: FirebaseUser?,
    loginViewModel: LoginViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    cursoViewModel: CursoViewModel = hiltViewModel(),
    tutoriaViewModel: TutoriaViewModel = hiltViewModel()
) {
    val startDestination = if (currentUser != null) Home else Login
    val backStack = rememberNavBackStack(startDestination)

    val navItems = listOf(
        NavItem("Inicio", Icons.Default.Home),
        NavItem("Perfil", Icons.Default.Person)
    )
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    val bottomRoutes = listOf(Home, Profile, Routes.Curso, Routes.Tutoria)
    val topAppRoutes = listOf(Routes.Curso, Routes.Tutoria)
    val showBottomBar = backStack.lastOrNull() in bottomRoutes
    val showTopBar = backStack.lastOrNull() in topAppRoutes

    var currentCurso by remember { mutableStateOf<Curso?>(null) }

    val onBottomItemSelected: (Int) -> Unit = { index ->
        selectedIndex = index
        backStack.clear()
        backStack.add(bottomRoutes[index])
    }

    val handleBack: () -> Unit = {
        if (backStack.lastOrNull() is Routes.Curso) {
            currentCurso = null
        }
        backStack.removeLastOrNull()
    }

    LaunchedEffect(Unit) {
        merge(
            loginViewModel.event,
            profileViewModel.event,
            homeViewModel.event,
            cursoViewModel.event
        ).collect { event ->
            when (event) {
                NavigationEvent.ToHome -> {
                    backStack.clear()
                    backStack.add(Home)
                }

                NavigationEvent.ToLogin -> {
                    selectedIndex = 0
                    backStack.clear()
                    backStack.add(Login)
                }

                is NavigationEvent.ToCurso -> {
                    currentCurso = event.curso
                    backStack.add(Routes.Curso)
                }

                is NavigationEvent.ToTutoria -> {
                    backStack.add(Routes.Tutoria)
                }
            }
        }
    }

    BackHandler(currentCurso != null) {
        handleBack()
    }

    MainScaffold(
        topTitle = currentCurso?.nombre ?: "",
        onBackButtonPressed = handleBack,
        navItems = navItems,
        selectedIndex = selectedIndex,
        onItemSelected = onBottomItemSelected,
        showTopBar = showTopBar,
        showBottomBar = showBottomBar
    ) { paddingValues ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Login> {
                    LoginScreen(loginViewModel)
                }
                entry<Home> {
                    HomeScreen(
                        homeViewModel,
                        paddingValues = paddingValues
                    )
                }
                entry<Profile> {
                    ProfileScreen(
                        profileViewModel,
                        paddingValues = paddingValues
                    )
                }
                entry<Routes.Curso> {
                    CursoScreen(
                        cursoViewModel,
                        paddingValues = paddingValues,
                    )
                }
                entry<Routes.Tutoria> {
                    TutoriaScreen(
                        tutoriaViewModel,
                        paddingValues,
                        currentCurso?.id ?: ""
                    )
                }
                entry<Routes.Error> {
                    Text("Error")
                }
            },
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(500)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(500)
                )
            },
            popTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(500)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(500)
                )
            },
            predictivePopTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(500)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(500)
                )
            }
        )
    }
}