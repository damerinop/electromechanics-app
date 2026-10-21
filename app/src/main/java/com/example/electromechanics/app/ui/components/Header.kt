package com.electromechanics.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.ui.theme.*

@Composable
fun Header(title: String, userName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(IndustrialBluePrimary)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, color = IndustrialYellowAccent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = "Usuario: $userName", color = IndustrialTextWhite, fontSize = 12.sp)
        }
    }
}