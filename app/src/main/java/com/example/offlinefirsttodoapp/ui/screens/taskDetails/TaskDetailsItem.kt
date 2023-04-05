package com.example.offlinefirsttodoapp.ui.screens.taskDetails

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.offlinefirsttodoapp.ui.theme.LocalSpacing

@Composable
fun TaskDetailsItem(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    @DrawableRes icon: Int,
    content: @Composable() () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
    ) {
        Row(
            verticalAlignment = verticalAlignment,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = LocalSpacing.current.medium,
                    vertical = LocalSpacing.current.medium
                )
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Gray,
                modifier = iconModifier.alpha(.7f)
            )
            content()
        }
    }
}