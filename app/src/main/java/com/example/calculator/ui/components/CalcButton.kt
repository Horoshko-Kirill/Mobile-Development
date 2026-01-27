package com.example.calculator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.calculator.ui.theme.FunctionColor
import com.example.calculator.ui.theme.NumberColor
import com.example.calculator.ui.theme.OperatorColor
import com.example.calculator.ui.theme.TextColor

@Composable
fun CalcButton(
    label: String,
    modifier: Modifier = Modifier,
    shape: Boolean = false, // новый флаг
    onClick: () -> Unit
) {
    val backgroundColor = when(label) {
        "+","-","*","/","^","=","C" -> OperatorColor
        "√","!","sin","cos","tan","ln","log","exp","±","%" -> FunctionColor
        else -> NumberColor
    }

    Surface(
        modifier = modifier
            .padding(2.dp)
            .clickable { onClick() },
        shape = if (shape) RoundedCornerShape(50) else RoundedCornerShape(12.dp),
        color = backgroundColor,
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = TextColor,
                fontSize = 20.sp
            )
        }
    }
}
