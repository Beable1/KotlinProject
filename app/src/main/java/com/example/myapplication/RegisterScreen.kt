package com.example.myapplication

import android.content.Context
import android.graphics.Paint.Align
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.google.firebase.auth.FirebaseAuth
import javax.xml.transform.ErrorListener


@Composable
fun RegisterScreen(navController:NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var buttonClicked by remember { mutableStateOf(false) }



    val context= LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.onPrimaryContainer)
            .padding(25.dp, 90.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Kayıt Ol",
                    modifier = Modifier.padding(0.dp,0.dp,0.dp,30.dp),
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 28.sp
                )



                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Eposta:") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorScheme.primaryContainer,
                        focusedContainerColor = colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),



                    )
                if (buttonClicked){showEmailMessage(email = email)}

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
                if (buttonClicked){showPasswordMessage(password = password)}


                Button(
                    onClick = {
                        createUserWithEmailAndPassword(email,password, MainActivity(),navController, context)
                        buttonClicked=true
                    },
                    colors =  ButtonDefaults.buttonColors(containerColor = colorScheme.onPrimaryContainer),
                    modifier = Modifier
                        .width(150.dp)
                        .padding(8.dp)
                        .height(45.dp)
                ) {
                    Text(text = "Tamamla", color = colorScheme.primaryContainer, fontSize = 17.sp)
                }

            }
        }
    }
}
@Composable
@Preview
fun RegisterScreenPreview(){
    RegisterScreen(navController = rememberNavController())
}

fun createUserWithEmailAndPassword(email: String, password: String, activity: MainActivity,navController:NavController,context:Context) {
    val auth = FirebaseAuth.getInstance()
    if (isEmailValid(email)&& isPasswordValid(password)){
        Log.w("Kayıt", "valid")
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Başarılı kayıt
                Log.d("Kayıt", "Kayıt Başarılı")
                sendEmailVerification(activity)
                navController.navigate(route = Screen.Verification.route)


            } else {
                // Başarısız kayıt
                Log.w("Kayıt", "Kayıt Başarısız", task.exception)

                Toast.makeText(context,"Bu eposta halihazırda kullanılmakta!",Toast.LENGTH_LONG).show()

            }
        }
    }
    else{

        Log.w("Kayıt", "not valid")
    }
}


fun sendEmailVerification(activity: MainActivity) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    if (currentUser != null) {
        currentUser.sendEmailVerification()
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // E-posta doğrulama mesajı gönderildi
                    Log.d("E-posta Doğrulama", "E-posta Doğrulama Mesajı Gönderildi")
                    // E-posta doğrulama mesajı gönderildikten sonra yapılacak işlemleri buraya yazın
                } else {
                    // E-posta doğrulama mesajı gönderilemedi
                    Log.w("E-posta Doğrulama", "E-posta Doğrulama Mesajı Gönderilemedi", task.exception)
                    // Hatanın işlenmesi

                }
            }
    } else {
        // Geçerli bir kullanıcı yok

        Log.w("E-posta Doğrulama", "Geçerli Bir Kullanıcı Yok")

    }
}

fun isEmailValid(email: String): Boolean {

    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")

    if(email.isNotEmpty()&&emailRegex.matches(email))
    {return true }
    else
    {return false}

}

fun isPasswordValid(password: String): Boolean {

    val passwordRegex = Regex("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}")

    if(password.isNotEmpty()&&passwordRegex.matches(password))
    {return true }
    else
    {return false}

}


@Composable
fun showEmailMessage(email: String) {


    if (!isEmailValid(email)) {

        Text(
            text = "geçerli bir eposta kullanınız!",
            color = MaterialTheme.colorScheme.error,fontSize = 11.sp, textAlign =TextAlign.Start, modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 0.dp)
        )}




}

@Composable
fun showPasswordMessage(password: String) {


    if (!isPasswordValid(password)) {

        Text(
            text = " *parolanın en az 8 karakter uzunluğunda olması gerekir \n *en az bir büyük ve küçük harf olması gerekir \n" +
                    " *en az bir rakam olması gerekir",
            color = MaterialTheme.colorScheme.error, fontSize = 11.sp, modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 0.dp)
        )}




}





