package com.cyberfeedforward.gibblygoatgames.ui.compose.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyberfeedforward.gibblygoatgames.ui.compose.parts.Background

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState()
) {
    Column(modifier = modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
    ) {
        //Background()
    }
}