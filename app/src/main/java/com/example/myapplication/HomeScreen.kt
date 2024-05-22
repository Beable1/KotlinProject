package com.example.myapplication
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

public var limit = 10

@Composable
fun HomeScreen(navController:NavController){



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(text = "Ana Sayfa", color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer) },
            actions = {
                IconButton(onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(route = Screen.Login.route)
                }) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Çıkış", tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
                }
            },
            backgroundColor =  androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth()
        )



        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Spacer(modifier = Modifier.padding(15.dp))
            Image(
                painter = painterResource(id = R.drawable.aaa), // Resminizin adı burada
                contentDescription = "My Image",
                modifier = Modifier
                    .size(270.dp)
                    .padding(0.dp, 0.dp, 0.dp, 10.dp)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                onClick = { navController.navigate(route = Screen.First.route) },
                colors = ButtonDefaults.buttonColors(backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp)
                    .padding(5.dp)
            ) {
                Text(text = "İngilizce Kelime Ekleme", color =  androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Button(
                onClick = { navController.navigate(route = Screen.Second.route) },
                colors = ButtonDefaults.buttonColors(backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp)
                    .padding(5.dp)
            ) {
                Text(text = "İngilizce Kelime Egzersizi", color =  androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Button(
                onClick = { navController.navigate(route = Screen.Third.route) },
                colors = ButtonDefaults.buttonColors(backgroundColor =  androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp)
                    .padding(5.dp)
            ) {
                Text(text = "İstatistikler", color =  androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
            }

            Spacer(modifier = Modifier.padding(30.dp))
            Column(modifier= Modifier
                .background(color = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer)
                .clip(
                    RoundedCornerShape(16.dp)
                )
            ){
                Image(
                    painter = painterResource(id = R.drawable.baseline_settings_24), // Resminizin adı burada
                    contentDescription = "My Image",
                    modifier = Modifier
                        .size(100.dp).padding(10.dp)

                )
            }

        }
    }
}

@Composable
@Preview
fun HomeScreenPreview(){
    HomeScreen(navController = rememberNavController())
}