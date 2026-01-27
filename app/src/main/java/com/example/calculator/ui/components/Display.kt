package com.example.calculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.calculator.ui.theme.DarkBackground
import com.example.calculator.ui.theme.TextColor

@Composable
fun Display(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = modifier
            .background(DarkBackground)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = text,
            color = TextColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}
