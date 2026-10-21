package com.electromechanics.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.ui.theme.*

@Composable
fun Footer(
    onNavigateHome: (() -> Unit)? = null,
    onLogout: (() -> Unit)? = null,
    copyrightText: String = "© 2026 Electromechanics SpA. Todos los derechos reservados.",
    backgroundColor: Color = IndustrialBlack,
    textColor: Color = Color.Gray
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onNavigateHome != null) {
                Button(
                    onClick = onNavigateHome,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IndustrialYellowAccent,
                        contentColor = IndustrialBlack
                    ),
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Text("VOLVER AL MENÚ", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            if (onLogout != null) {
                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IndustrialErrorRed,
                        contentColor = IndustrialTextWhite
                    ),
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Text("CERRAR SESIÓN", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = copyrightText,
            color = textColor,
            fontSize = 11.sp
        )
    }
}