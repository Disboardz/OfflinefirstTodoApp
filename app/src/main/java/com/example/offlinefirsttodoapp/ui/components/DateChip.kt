package com.example.offlinefirsttodoapp.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.offlinefirsttodoapp.ui.theme.LocalSpacing


@Composable
fun DateChip(
    modifier: Modifier = Modifier,
    text: String,
    onTrailingIconClick: () -> Unit = {
        Log.v("date_chip", "Add function!!")
    }
) {
    val interactionSource by remember { mutableStateOf(MutableInteractionSource()) }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .border(.8.dp, MaterialTheme.colorScheme.onBackground, MaterialTheme.shapes.large)
    ) {
        Row(
            modifier = Modifier.padding(
                vertical = LocalSpacing.current.extraSmall,
                horizontal = LocalSpacing.current.small
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = LocalSpacing.current.extraSmall)
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) {
                        onTrailingIconClick()
                    }
                    .padding(start = LocalSpacing.current.extraSmall)
            )
        }
    }
}