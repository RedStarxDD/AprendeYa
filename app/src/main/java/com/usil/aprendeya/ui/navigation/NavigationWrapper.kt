package com.usil.aprendeya.ui.navigation

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.google.firebase.auth.FirebaseUser
import com.usil.aprendeya.ui.navigation.Routes.HomeRoute
import com.usil.aprendeya.ui.navigation.Routes.LoginRoute
import com.usil.aprendeya.ui.navigation.Routes.ProfileRoute
import com.usil.aprendeya.ui.navigation.Routes.CursoRoute
import com.usil.aprendeya.ui.navigation.Routes.TemaRoute
import com.usil.aprendeya.ui.navigation.Routes.PreguntaRoute
import com.usil.aprendeya.ui.screens.alumno.TemaScreen
import com.usil.aprendeya.ui.screens.alumno.HomeScreen
import com.usil.aprendeya.ui.screens.alumno.ProfileScreen
import com.usil.aprendeya.ui.screens.alumno.CursoScreen
import com.usil.aprendeya.ui.screens.alumno.PreguntaScreen
import com.usil.aprendeya.ui.screens.components.AppEvent
import com.usil.aprendeya.ui.screens.components.MainScaffold
import com.usil.aprendeya.ui.screens.components.NavItem
import com.usil.aprendeya.ui.screens.login.LoginScreen
import com.usil.aprendeya.viewModel.alumno.CursoViewModel
import com.usil.aprendeya.viewModel.alumno.HomeViewModel
import com.usil.aprendeya.viewModel.alumno.PreguntaViewModel
import com.usil.aprendeya.viewModel.alumno.ProfileViewModel
import com.usil.aprendeya.viewModel.alumno.TemaViewModel
import com.usil.aprendeya.viewModel.login.LoginViewModel
import kotlinx.coroutines.flow.merge

@Composable
fun NavigationWrapper(
    currentUser: FirebaseUser?,
    loginViewModel: LoginViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    cursoViewModel: CursoViewModel = hiltViewModel(),
    temaViewModel: TemaViewModel = hiltViewModel(),
    preguntaViewModel: PreguntaViewModel = hiltViewModel()
) {
    val startDestination = if (currentUser != null) HomeRoute else LoginRoute
    val backStack = rememberNavBackStack(startDestination)

    val navItems = listOf(
        NavItem("Inicio", Icons.Default.Home),
        NavItem("Perfil", Icons.Default.Person)
    )

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    var previousIndex by rememberSaveable { mutableIntStateOf(0) }
    var currentCurso by rememberSaveable { mutableStateOf<String?>(null) }

    val currentRoute = backStack.lastOrNull()
    val bottomRoutes = listOf(HomeRoute, ProfileRoute)
    val showTopBar = currentRoute is CursoRoute || currentRoute is TemaRoute || currentRoute is PreguntaRoute
    val showBottomBar = currentRoute in bottomRoutes || showTopBar
    val context = LocalContext.current

    val onBottomItemSelected: (Int) -> Unit = { index ->
        if (index != selectedIndex) {
            previousIndex = selectedIndex
            selectedIndex = index
            backStack.clear()
            backStack.add(bottomRoutes[index])
        }
    }

    val handleBack: () -> Unit = {
        if (backStack.lastOrNull() is CursoRoute) {
            currentCurso = null
        }
        backStack.removeLastOrNull()
    }

    val screenTransition = NavDisplay.transitionSpec {
        val isForward = selectedIndex > previousIndex
        val isBackward = selectedIndex < previousIndex

        when {
            isForward -> {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                )
            }

            isBackward -> {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }

            else -> EnterTransition.None togetherWith ExitTransition.None
        }
    }

    LaunchedEffect(Unit) {
        merge(
            loginViewModel.event,
            profileViewModel.event,
            homeViewModel.event,
            cursoViewModel.event,
            temaViewModel.event
        ).collect { event ->
            when (event) {
                AppEvent.ToHome -> {
                    backStack.clear()
                    backStack.add(HomeRoute)
                }

                AppEvent.ToLogin -> {
                    selectedIndex = 0
                    backStack.clear()
                    backStack.add(LoginRoute)
                }

                is AppEvent.ToCurso -> {
                    currentCurso = event.cursoNombre
                    backStack.add(CursoRoute(event.cursoId))
                }

                is AppEvent.ToTema -> {
                    backStack.add(TemaRoute(event.cursoId, event.temaId))
                }

                is AppEvent.OpenLink -> {
                    val intent = Intent(Intent.ACTION_VIEW, event.url.toUri())
                    context.startActivity(intent)
                }

                is AppEvent.ToPregunta -> {
                    backStack.add(PreguntaRoute(event.cursoId, event.temaId, event.cuestionarioId))
                }
            }
        }
    }

    BackHandler(currentCurso != null) {
        handleBack()
    }

    MainScaffold(
        topTitle = currentCurso ?: "",
        onBackButtonPressed = handleBack,
        navItems = navItems,
        selectedIndex = selectedIndex,
        onItemSelected = onBottomItemSelected,
        showTopBar = showTopBar,
        showBottomBar = showBottomBar,
    ) { paddingValues ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<LoginRoute> {
                    LoginScreen(loginViewModel)
                }
                entry<HomeRoute>(metadata = screenTransition) {
                    HomeScreen(
                        homeViewModel,
                        paddingValues
                    )
                }
                entry<ProfileRoute>(metadata = screenTransition) {
                    ProfileScreen(
                        profileViewModel,
                        paddingValues
                    )
                }
                entry<CursoRoute> { key ->
                    CursoScreen(
                        cursoViewModel,
                        paddingValues,
                        key.cursoId
                    )
                }
                entry<TemaRoute> { key ->
                    TemaScreen(
                        temaViewModel,
                        paddingValues,
                        key.cursoId,
                        key.temaId
                    )
                }
                entry<PreguntaRoute> {key ->
                    PreguntaScreen(
                        preguntaViewModel,
                        paddingValues,
                        key.cursoId,
                        key.temaId,
                        key.cuestionarioId
                    )
                }
                entry<Routes.ErrorRoute> {
                    Text("Error")
                }
            }
        )
    }
}