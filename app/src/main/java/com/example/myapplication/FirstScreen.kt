package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.color1
import com.example.myapplication.ui.theme.color2
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.ui.res.painterResource


@Composable
fun FirstScreen(navController: NavController){
    var englishWord by remember { mutableStateOf("") }
    var turkishWord by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    val streak = 0
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var turkishWordExamples by remember { mutableStateOf(listOf("")) }
    var englishWordExamples by remember { mutableStateOf(listOf("")) }
    var correctCount=0
    var questionCount=0




    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.onPrimaryContainer),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(text = "Kelime Ekleme", color = colorScheme.onPrimaryContainer) },
            actions = {
                IconButton(onClick = {
                    navController.navigate(route = Screen.Home.route)
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Geri", tint = colorScheme.onPrimaryContainer)
                }
            },
            backgroundColor =  colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(10.dp))
        LazyColumn(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxSize()
                .background(colorScheme.onPrimaryContainer)
                .padding(15.dp,0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

                item {

                    Text(
                        text = "İngilizce Kelimeyi Ekleyiniz",
                        modifier = Modifier.padding(20.dp),
                        color = colorScheme.secondaryContainer,
                        fontSize = MaterialTheme.typography.h5.fontSize
                    )

                    TextField(
                        value = englishWord,
                        onValueChange = { englishWord = it },
                        label = { Text("Kelime Giriniz") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    Text(
                        text = "Türkçesini Ekleyiniz",
                        modifier = Modifier.padding(20.dp),
                        color = colorScheme.secondaryContainer,
                        fontSize = MaterialTheme.typography.h5.fontSize
                    )

                    TextField(
                        value = turkishWord,
                        onValueChange = { turkishWord = it },
                        label = { Text("Kelime Giriniz") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 8.dp, 8.dp, 10.dp)
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                }
            item {
                
                Column(modifier = Modifier.background(colorScheme.onPrimaryContainer),verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "İngilizce Cümle Ekleyiniz",
                        modifier = Modifier.padding(20.dp),
                        color =colorScheme.secondaryContainer,
                        fontSize = MaterialTheme.typography.h5.fontSize
                    )
                englishWordExamples.forEachIndexed { index, example ->


                        TextField(
                            value = example,
                            onValueChange = { newValue ->
                                englishWordExamples = englishWordExamples.toMutableList().also {
                                    it[index] = newValue
                                }
                            },
                            label = { Text(" ${index+1}. İngilizce Kelimeyi Ekleyiniz") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp, 8.dp, 8.dp, 10.dp)
                        )
                    }


                    Text(
                        text = "Türkçe Cümle Ekleyiniz",
                        modifier = Modifier.padding(20.dp),
                        color = colorScheme.secondaryContainer,
                        fontSize = MaterialTheme.typography.h5.fontSize
                    )
                turkishWordExamples.forEachIndexed { index, example ->


                        TextField(
                            value = example,
                            onValueChange = { newValue ->
                                turkishWordExamples = turkishWordExamples.toMutableList().also {
                                    it[index] = newValue
                                }
                            },
                            label = { Text("${index+1}. Türkçe Kelimenin Karşılığını Ekleyiniz") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp, 8.dp, 8.dp, 10.dp)
                        )


                }
                }



                Row(modifier = Modifier.padding(10.dp)) {
                    if(englishWordExamples.size<3){
                    GradientIconButton(
                        vector = Icons.Default.Add,
                        iconColor = Color.White,
                        gradient = Brush.horizontalGradient(
                            colors = listOf(color1, color2)
                        ),
                        onClick = {
                            englishWordExamples = englishWordExamples.toMutableList().apply {
                                if (englishWordExamples.size!=3){
                                    add("")

                                }else{ Toast.makeText(context,"Max 3 örnek cümle kullanılabilir!",Toast.LENGTH_SHORT).show()}


                            }
                            turkishWordExamples = turkishWordExamples.toMutableList().apply {
                                if (turkishWordExamples.size!=3){
                                    add("")

                                }else{ Toast.makeText(context,"Max 3 örnek cümle kullanılabilir!",Toast.LENGTH_SHORT).show()}

                            }
                        }
                    )}
                    if(englishWordExamples.size!=1){
                    GradientIconButton(
                        vector = Icons.Default.Close,
                        iconColor = Color.White,
                        gradient = Brush.horizontalGradient(
                            colors = listOf(color1, color2)
                        ),
                        onClick = {
                            englishWordExamples = englishWordExamples.toMutableList().apply {
                                if (isNotEmpty()) {
                                    removeAt(size - 1)
                                }
                            }
                            turkishWordExamples = turkishWordExamples.toMutableList().apply {
                                if (isNotEmpty()) {
                                    removeAt(size - 1)
                                }
                            }
                        }
                    )}
                }


            }
                item {

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(0.dp, 7.dp, 0.dp, 0.dp)
                        .clickable { launcher.launch("image/*") }
                        .background(colorScheme.primaryContainer),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        imageUri?.let {
                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap.value = MediaStore.Images
                                    .Media.getBitmap(context.contentResolver, it)
                            } else {
                                val source = ImageDecoder.createSource(context.contentResolver, it)
                                bitmap.value = ImageDecoder.decodeBitmap(source)
                            }

                            bitmap.value?.let { btm ->
                                val bitMap = btm.asImageBitmap()
                                Image(
                                    bitmap = bitMap,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(400.dp)
                                        .padding(20.dp)
                                )
                            }
                        }


                        Image(
                            painter = painterResource(id = R.drawable.baseline_image_24), // Resminizin adı burada
                            contentDescription = "My Image",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(0.dp, 0.dp, 0.dp, 10.dp)
                        )
                        Text(
                            text = "Resim Yükle",
                            modifier = Modifier.padding(4.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp
                        )


                    }



                    Spacer(modifier = Modifier.padding(5.dp))
                    Button(
                        onClick = {

                            val currentDate = Timestamp.now().toDate()
                            val calendar = Calendar.getInstance()
                            calendar.time = currentDate
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val formattedDate = dateFormat.format(calendar.time)
                            val data = mapOf(
                                "englishWord" to englishWord,
                                "turkishWord" to turkishWord,
                                "englishWordExamples" to englishWordExamples,
                                "turkishWordExamples" to turkishWordExamples,
                                "timeStamp" to formattedDate,
                                "streak" to streak,
                                "questionCount" to questionCount,
                                "correctCount" to correctCount
                            )

                            imageUri?.let { uri ->
                                uploadImageToFirebase(context, uri) { imageUrl ->
                                    // Resmin Firebase Storage'daki URL'sini Firestore'a ekle
                                    currentUser.let {
                                        db.collection("users").document(it).collection("words")
                                            .add(data.plus("imageUrl" to imageUrl))
                                            .addOnSuccessListener {
                                                Log.d("FirstScreen", "Kelime ekleme başarılı")
                                                Toast.makeText(context,"Kelime Başarıyla Eklendi",
                                                    Toast.LENGTH_LONG).show()
                                                turkishWord=""
                                                englishWord=""
                                                turkishWordExamples= listOf()
                                                englishWordExamples= listOf()
                                                imageUri=null

                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("FirstScreen", "Kelime ekleme başarısız: $e")
                                                Toast.makeText(context,"Kelime Eklenemedi $e",
                                                    Toast.LENGTH_LONG).show()
                                            }
                                    }
                                }
                            }


                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorScheme.secondaryContainer),
                        modifier = Modifier
                            .width(130.dp)
                            .padding(13.dp)
                            .height(45.dp)
                    ) {
                        Text(text = "Ekle", color = colorScheme.onSecondaryContainer, fontSize = 17.sp)
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                }




        }
    }
}

@Composable
@Preview
fun FirstScreenPreview(){


    FirstScreen(navController = rememberNavController())
}


private fun uploadImageToFirebase(context: Context,imageUri: Uri, callback: (String) -> Unit) {

    val storageRef = FirebaseStorage.getInstance().reference
    val imagesRef = storageRef.child("images")
    val fileName = "${UUID.randomUUID()}.jpg"
    val imageRef = imagesRef.child(fileName)

    try {
        val inputStream =  context.contentResolver.openInputStream(imageUri)
        inputStream?.buffered()?.use { input ->
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(4 * 1024)

            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }

            val data = outputStream.toByteArray()
            val uploadTask = imageRef.putBytes(data)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    downloadUri?.let {
                        callback(it.toString())
                    }
                } else {
                    Log.e("FirstScreen", "Error uploading image to Firebase Storage")
                }
            }
        }
    } catch (e: IOException) {
        Log.e("FirstScreen", "Error uploading image: ${e.message}")
    }
}
