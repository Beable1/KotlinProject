package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomeScreen(navController: NavController) {
    var textFieldValue by remember { mutableIntStateOf(10) }
    val db = FirebaseFirestore.getInstance()
    val firebaseAuthGI=FirebaseAuth.getInstance()
    val currentUser = firebaseAuthGI.currentUser!!.uid
    var showDialog by remember { mutableStateOf(false) }
    var docRef = db.collection("users")
        .document(currentUser)
        .collection("userDefaults").document("userDefault")

    LaunchedEffect(Unit) {
        val userDefaultsRef = db.collection("users")
            .document(currentUser)
            .collection("userDefaults")
        docRef = userDefaultsRef.document("userDefault")

        userDefaultsRef.get().addOnSuccessListener { documents ->
            if (!documents.isEmpty) {


                docRef.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        val limit = document.getString("limit")
                        if (limit != null) {
                            textFieldValue = limit.toInt()
                            Log.d("HomeDocument", "limit: $textFieldValue")
                        }
                    } else {
                        Log.d("HomeDocument", "No such document")
                    }
                }.addOnFailureListener { exception ->
                    Log.d("HomeDocument", "get failed with ", exception)
                }
            } else {
                var newData: Map<String, Any> = hashMapOf(
                    "limit" to "10"
                )
                docRef.set(newData).addOnSuccessListener{

                }
            }
        }.addOnFailureListener { exception ->
            Log.d("HomeDocument", "get failed with ", exception)
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(text = "Ana Sayfa", color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer) },
            actions = {
                IconButton(onClick = {
                    firebaseAuthGI.signOut()
                    navController.navigate(route = Screen.Login.route)
                }) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Çıkış", tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
                }
            },
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
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
                Text(text = "İngilizce Kelime Ekleme", color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Button(
                onClick = { navController.navigate(route = Screen.Second.createRoute(textFieldValue)) },
                colors = ButtonDefaults.buttonColors(backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp)
                    .padding(5.dp)
            ) {
                Text(text = "İngilizce Kelime Egzersizi", color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Button(
                onClick = { navController.navigate(route = Screen.Third.route) },
                colors = ButtonDefaults.buttonColors(backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp)
                    .padding(5.dp)
            ) {
                Text(text = "İstatistikler", color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
            }

            Spacer(modifier = Modifier.padding(30.dp))
            Column(
                modifier = Modifier
                    .background(color = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_settings_24), // Resminizin adı burada
                    contentDescription = "Settings",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(10.dp)
                        .clickable {
                            docRef.get().addOnSuccessListener { document ->
                                if (document != null) {
                                    Log.d("HomeDocument", "limit: $textFieldValue")
                                    textFieldValue = document.getString("limit")!!.toInt()

                                }
                            }
                            showDialog = true
                        }
                )
            }
        }

        if (showDialog) {
            SettingsDialog(
                textFieldValue = textFieldValue,
                onDismiss = { showDialog = false },
                onSend = { newValue ->
                    textFieldValue = newValue
                    showDialog = false

                }
            )
        }
    }
}

@Composable
fun SettingsDialog(
    textFieldValue: Int,
    onDismiss: () -> Unit,
    onSend: (Int) -> Unit
) {
    var newValue by remember { mutableStateOf(textFieldValue.toString()) }
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    val docRef = db.collection("users")
        .document(currentUser)
        .collection("userDefaults").document("userDefault")

    var newData: Map<String, Any> = hashMapOf(
        "limit" to newValue
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Ayarlar") },
        text = {
            Column {
                Text("Yeni limit değeri girin:")
                TextField(
                    value = newValue,
                    onValueChange = { newValue = it },
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSend(newValue.toInt())
                docRef.get().addOnSuccessListener {document ->
                    document.reference.update(newData)
                }

            }) {
                Text("Gönder")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("İptal")
            }
        }
    )
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}