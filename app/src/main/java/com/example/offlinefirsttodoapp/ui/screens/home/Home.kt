package com.example.offlinefirsttodoapp.ui.screens.home


import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.offlinefirsttodoapp.R
import com.example.offlinefirsttodoapp.ui.components.BottomBar
import com.example.offlinefirsttodoapp.ui.components.TabList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class HomeSheetType {
    MENU,
    LIST_OPTIONS,
    CREATE_TASK,
    NONE
}

@OptIn(
    ExperimentalPagerApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
)
@Composable
fun Home(
    navigateToTaskDetails: (taskId: Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    val systemBarColor = MaterialTheme.colorScheme.surface
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(0)
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    var sheetType: HomeSheetType by rememberSaveable { mutableStateOf(HomeSheetType.NONE) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusRequester = remember { FocusRequester() }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = systemBarColor,
            darkIcons = useDarkIcons
        )
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        if (pagerState.currentPage != state.currentPage) {
            viewModel.changePage(pagerState.currentPage)
        }
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    if (state.currentPage != pagerState.currentPage) {
                        coroutineScope.launch { pagerState.animateScrollToPage(state.currentPage) }
                    }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    fun changePage(page: Int, dismissSheet: Boolean = false) {
        coroutineScope.launch {
            launch { pagerState.animateScrollToPage(page) }
            launch {
                if (dismissSheet) {
                    bottomSheetState.hide()
                    sheetType = HomeSheetType.NONE
                }
            }
        }
    }

    fun showSheet(type: HomeSheetType) {
        sheetType = type
        coroutineScope.launch {
            launch { bottomSheetState.show() }
            delay(100)
            if (type == HomeSheetType.CREATE_TASK) {
                focusRequester.requestFocus()
            }
        }
    }

    fun onNavigateToCreateList(fromSheet: Boolean = false) {
        if (fromSheet) {
            coroutineScope.launch {
                bottomSheetState.hide()
                sheetType = HomeSheetType.NONE
                viewModel.toggleModal()
            }
        } else {
            viewModel.toggleModal()
        }
    }

    fun createList(title: String) {
        viewModel.createTaskList(title) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(state.taskList.size)
            }
        }
    }

    fun deleteList() {
        viewModel.deleteTaskList(state.taskList[pagerState.currentPage]) {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        }
    }

    BackHandler(enabled = bottomSheetState.isVisible) {
        coroutineScope.launch {
            bottomSheetState.hide()
        }
    }

    Crossfade(targetState = state.showModal) {
        if (!it) {
            ModalBottomSheetLayout(
                sheetBackgroundColor = MaterialTheme.colorScheme.background,
                sheetState = bottomSheetState,
                sheetContent = {
                    when (sheetType) {
                        HomeSheetType.MENU -> {
                            Menu(
                                taskList = state.taskList,
                                page = pagerState.currentPage,
                                changePage = { page -> changePage(page, true) },
                                onNavigateToCreateList = { onNavigateToCreateList(true) }
                            )
                        }
                        HomeSheetType.LIST_OPTIONS -> {
                            ListOptions(
                                taskList = state.taskList,
                                page = pagerState.currentPage,
                                deleteList = ::deleteList
                            )
                        }
                        HomeSheetType.CREATE_TASK -> {
                            CreateTaskSheet(createTask = viewModel::createTask, focusRequester, toggleSheet = {
                                coroutineScope.launch {
                                    bottomSheetState.hide()
                                }
                            })
                        }
                        HomeSheetType.NONE -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                            ) {

                            }
                        }
                    }
                },
                sheetShape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
            ) {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(text = stringResource(id = R.string.home_title))
                            },
                            scrollBehavior = scrollBehavior
                        )
                    },
                    bottomBar = { BottomBar(showSheet = { type ->
                        showSheet(type)
                    }) },
                    content = { contentPadding ->
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(contentPadding)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                TabList(
                                    state.taskList,
                                    selectedTabIndex = pagerState.currentPage,
                                    changePage = { page -> changePage(page) },
                                    openCreateList = viewModel::toggleModal
                                )
                                HorizontalPager(
                                    count = state.taskList.size,
                                    state = pagerState,
                                    modifier = Modifier.weight(1f)
                                ) { page ->
                                    Page(
                                        data = state.taskList[page].tasks,
                                        navigateToTaskDetails = navigateToTaskDetails,
                                        updateTask = viewModel::updateTask
                                    )
                                }
                            }
                        }
                    }
                )
            }
        } else {
            CreateTaskList(
                dismiss = viewModel::toggleModal,
                createTaskList = ::createList
            )
        }
    }
}