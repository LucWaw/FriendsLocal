package com.lucwaw.friendsLocal.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DetailPage(personId: Int?, modifier: Modifier = Modifier) {
    Text(text = "Detail Page - PersonId = ${personId ?: "None"}", modifier = modifier)
}
