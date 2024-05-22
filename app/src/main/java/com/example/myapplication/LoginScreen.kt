package com.example.myapplication
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource


@Composable
fun LoginScreen(navController:NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var buttonClicked by remember { mutableStateOf(false) }
    val context= LocalContext.current



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.onPrimaryContainer)
            .padding(15.dp, 40.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {




        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.onPrimaryContainer)
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .border(1.5.dp, color = colorScheme.primaryContainer, RoundedCornerShape(16.dp))
        ) {


            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.aaa), // Resminizin adı burada
                    contentDescription = "My Image",
                    modifier = Modifier
                        .size(260.dp)
                        .padding(0.dp, 0.dp, 0.dp, 10.dp)
                )

                Text(
                    text = "Giriş Yap",
                    modifier = Modifier.padding(20.dp),
                    color = colorScheme.primaryContainer,
                    fontSize = 28.sp
                )

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Kullanıcı Adı:") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorScheme.primaryContainer,
                        focusedContainerColor = colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),



                )
                if (buttonClicked){showEmailMessage(email = email)}

                Spacer(modifier = Modifier.padding(5.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Şifre:") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 8.dp, 8.dp, 10.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorScheme.primaryContainer,
                        focusedContainerColor = colorScheme.primaryContainer
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Row {
                    Text(
                        text = "Kayıt Ol",
                        modifier = Modifier
                            .padding(26.dp, 7.dp)
                            .clickable { navController.navigate(route = Screen.Register.route) },
                        color = colorScheme.primaryContainer,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Şifremi Unuttum",
                        modifier = Modifier
                            .padding(10.dp, 7.dp)
                            .clickable { navController.navigate(route = Screen.Forgot.route) },
                        color = colorScheme.primaryContainer,
                        fontSize = 13.sp
                    )

                }
                Spacer(modifier = Modifier.padding(5.dp))
                Button(
                    onClick = {
                        signInWithEmailAndPassword(email,password,MainActivity(),navController,context)
                        buttonClicked=true
                         },
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primaryContainer),
                    modifier = Modifier
                        .width(150.dp)
                        .padding(8.dp)
                        .height(45.dp)
                ) {
                    Text(text = "Giriş Yap", color = colorScheme.onPrimaryContainer, fontSize = 13.sp)
                }
            }
        }

    }
}
@Composable
@Preview
fun LoginScreenPreview(){
    LoginScreen(navController = rememberNavController())
}


fun signInWithEmailAndPassword(email: String, password: String, activity: MainActivity,navController:NavController,context: Context) {
    val auth = FirebaseAuth.getInstance()

    if (isEmailValid(email)&& isPasswordValid(password)){
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Başarılı giriş
                Log.d("Giriş", "Giriş Başarılı")
                val currentUser = auth.currentUser

                if (currentUser != null && currentUser.isEmailVerified) {
                    // E-posta adresi doğrulanmış, giriş başarılı
                    Log.d("Giriş", "Giriş Başarılı")
                    navController.navigate(route = Screen.Home.route)


                }
                else {
                    Log.d("E-posta Doğrulama", "E-posta Adresi Doğrulanmamış")
                    Toast.makeText(context,"E-posta Adresi Doğrulanmamış", Toast.LENGTH_LONG).show()
                }


            } else {
                // Başarısız giriş
                Log.w("Giriş", "Giriş Başarısız", task.exception)
                Toast.makeText(context,"E-posta veya şifre yanlış", Toast.LENGTH_LONG).show()




            }
        }
    }
    else{
        Toast.makeText(context,"E-posta veya şifre yanlış", Toast.LENGTH_LONG).show()
    }
}




