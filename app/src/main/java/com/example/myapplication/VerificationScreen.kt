package com.example.myapplication
import androidx.compose.material3.MaterialTheme.colorScheme
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.fido.fido2.api.common.ErrorCode
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*


@Composable
fun VerificationScreen(navController:NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.onPrimaryContainer)
            .padding(25.dp, 180.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Eposta Doğrulama",
                    modifier = Modifier.padding(20.dp),
                    color = colorScheme.onPrimaryContainer,
                    fontSize = MaterialTheme.typography.h3.fontSize,textAlign = TextAlign.Center
                )
                Text(
                    text = "Epostanıza Gelen Doğrulama Linkini Onaylayınız.",
                    modifier = Modifier.padding(30.dp),
                    color = colorScheme.onPrimaryContainer,
                    fontSize = MaterialTheme.typography.h5.fontSize, textAlign = TextAlign.Center
                )
                Button(
                    onClick = {
                        navController.navigate(route = Screen.Login.route)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorScheme.onPrimaryContainer),
                    modifier = Modifier
                        .width(230.dp)
                        .padding(8.dp)
                        .height(45.dp)
                ) {
                    Text(text = "Giriş Ekranına Dön", color = colorScheme.primaryContainer, fontSize = 17.sp)
                }





            }
        }
    }
}
@Composable
@Preview
fun VerificationScreenPreview(){
    VerificationScreen(navController = rememberNavController())
}


