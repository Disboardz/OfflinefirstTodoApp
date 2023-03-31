package com.example.offlinefirsttodoapp.ui.screens.taskDetails

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.offlinefirsttodoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsTopBar(
    navigateBack: () -> Unit,
    starred: Boolean,
    delete: () -> Unit,
    update: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_icon),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            IconButton(onClick = update) {
                Icon(
                    painter = painterResource(id = if (starred) R.drawable.ic_baseline_star_24 else R.drawable.ic_outline_star_outline_24),
                    contentDescription = stringResource(
                        id = R.string.starred
                    ),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = delete) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = stringResource(id = R.string.delete_icon),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
    )
}