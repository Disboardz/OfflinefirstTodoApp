package com.example.offlinefirsttodoapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.offlinefirsttodoapp.R
import com.example.offlinefirsttodoapp.ui.screens.home.HomeSheetType

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    showSheet: (type: HomeSheetType) -> Unit
) {
    Surface(
        modifier = modifier,
        shadowElevation = 30.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .navigationBarsPadding()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(onClick = { showSheet(HomeSheetType.MENU) }) {
                    Icon(
                        Icons.Rounded.Menu,
                        contentDescription = stringResource(id = R.string.menu_icon)
                    )
                }
                IconButton(onClick = { showSheet(HomeSheetType.LIST_OPTIONS) }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(id = R.string.more_vert)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(end = 10.dp))
            FloatingActionButton(
                onClick = { showSheet(HomeSheetType.CREATE_TASK) },
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(id = R.string.add_icon)
                )
            }
            Spacer(modifier = Modifier.padding(end = 10.dp))
        }
    }
}