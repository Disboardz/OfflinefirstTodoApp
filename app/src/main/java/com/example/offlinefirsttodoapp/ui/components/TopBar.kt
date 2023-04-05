package com.example.offlinefirsttodoapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.offlinefirsttodoapp.R
import com.example.offlinefirsttodoapp.ui.theme.OfflinefirstTodoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(id = R.string.home_title))
        },
        modifier = modifier
    )
}

@Preview()
@Composable
fun TopBarPreview() {
    OfflinefirstTodoAppTheme {
        TopBar()
    }
}