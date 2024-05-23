package com.example.myapplication
import android.Manifest
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Paint.Align
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.color1
import com.example.myapplication.ui.theme.color2
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.properties.Delegates


@Composable
fun SecondScreen(navController: NavController, textFieldValue: Int){

    var insufficientDocument by remember{mutableStateOf(false)}
    var timestamp by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var answer1 by remember { mutableStateOf(List(3){""}) }
    var formattedDate by remember { mutableStateOf("") }
    var englishWord by remember { mutableStateOf("") }
    var turkishWord by remember { mutableStateOf("") }
    var streak by remember { mutableIntStateOf(0) }
    var last by remember { mutableStateOf(false) }
    var load by remember { mutableStateOf(true) }
    var documentId by remember { mutableStateOf("") }
    var documentId1 by remember { mutableStateOf("") }
    var turkishWordExamples by remember { mutableStateOf(listOf("")) }
    var englishWordExamples by remember { mutableStateOf(listOf("")) }
    var questionCount by remember { mutableIntStateOf(0) }
    var correctCount by remember { mutableIntStateOf(0) }
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    var questionId by remember { mutableLongStateOf(1) }
    val currentTime = System.currentTimeMillis()
    var expirationTime = System.currentTimeMillis() + (1 * 24 * 60 * 60 * 1000) // 1 gün
    val documentsList: MutableList<DocumentSnapshot> = mutableListOf()
    val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val storage = Firebase.storage

    var imageUrl by remember { mutableStateOf<String?>(null) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    questionId = sharedPreferences.getLong("lastQuestionId", 1)
    val expirationTimeLast = sharedPreferences.getLong("lastQuestionExpiration", expirationTime)

    val currentDate = Timestamp.now().toDate()

    val calendar = Calendar.getInstance()
    calendar.time = currentDate
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    formattedDate = dateFormat.format(calendar.time)

    Timer().schedule(1000) {
        load = false
    }



    fun timeStampController(){

        Log.d("CONTROLLERT", "TİME: $timestamp")
        Log.d("CONTROLLERT", "CURRENT TİME: $formattedDate")
        if((streak==0)&&(timestamp<=formattedDate)){
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            formattedDate = dateFormat.format(calendar.time)


        } else if((streak==1)&&(timestamp<=formattedDate)){
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            formattedDate = dateFormat.format(calendar.time)

        } else if ((streak==2)&&(timestamp<=formattedDate)){
            calendar.add(Calendar.DAY_OF_MONTH, 7)
            formattedDate = dateFormat.format(calendar.time)

        }else if ((streak==3)&&(timestamp<=formattedDate)){
            calendar.add(Calendar.DAY_OF_MONTH, 30)
            formattedDate = dateFormat.format(calendar.time)
        }else if ((streak==4)&&(timestamp<=formattedDate)){
            calendar.add(Calendar.DAY_OF_MONTH, 90)
            formattedDate = dateFormat.format(calendar.time)
        }else if ((streak==5)&&(timestamp<=formattedDate)){
            calendar.add(Calendar.DAY_OF_MONTH, 180)
            formattedDate = dateFormat.format(calendar.time)
        }else if ((streak==6)&&(timestamp<=formattedDate)){
            calendar.add(Calendar.DAY_OF_MONTH, 360)
            formattedDate = dateFormat.format(calendar.time)
        }else{
            calendar.add(Calendar.DAY_OF_MONTH, 9000)
            formattedDate = dateFormat.format(calendar.time)
        }
    }

    //Document Sıralama
    fun processDocument() {
        var currentDocumentIndex=0

        for(document in documentsList) {

            var timestamp1= document.getString("timeStamp").toString()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val deFormattedDate = dateFormat.parse(timestamp1)
            val calendar1 = Calendar.getInstance()
            calendar1.time = deFormattedDate!!
            var timestamp2 = Timestamp(calendar1.time)




            if (timestamp2<=Timestamp.now()) {

                documentId = document.id
                documentId1 = document.id
                timestamp= document.getString("timeStamp").toString()
                englishWord = document.getString("englishWord").toString()
                englishWordExamples = (document["englishWordExamples"] as List<String>?)!!
                streak = document.getLong("streak")!!.toInt()
                turkishWord = document.getString("turkishWord").toString().lowercase(Locale.getDefault())
                turkishWordExamples = (document["turkishWordExamples"] as List<String>?)!!
                questionCount = document.getLong("questionCount")!!.toInt()
                correctCount = document.getLong("correctCount")!!.toInt()

                Log.d("second", "id: $documentId")

                imageUrl = document.getString("imageUrl")
                imageUrl?.let { url ->
                    val storageReference = storage.getReferenceFromUrl(url)
                    storageReference.getBytes(Long.MAX_VALUE)
                        .addOnSuccessListener { bytes ->
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            imageBitmap = bitmap?.asImageBitmap()
                        }
                        .addOnFailureListener { exception ->
                            Log.e("ProcessDocument", "Error fetching image", exception)
                        }
                }

            }else{
                insufficientDocument=true
            }
            if (documentId==""){
                Log.d("second", "null")
                insufficientDocument=true
            }


            currentDocumentIndex++
            Log.d("second", "$currentDocumentIndex")

        }


    }

    if (currentTime > expirationTimeLast) {
        editor.remove("lastQuestionId")
        editor.remove("lastQuestionExpiration")
        editor.apply()
        questionId=0
    }


    currentUser.let { userId ->
        val query = db.collection("users").document(userId).collection("words")
            .orderBy("timeStamp", Query.Direction.ASCENDING)
            .limit(1)

        query.get()
            .addOnSuccessListener { documents ->
                documentsList.clear()
                documentsList.addAll(documents.documents)
                // İlk belgeyi işleme al

                    processDocument()

            }
            .addOnFailureListener { exception ->
                Log.d("second", "get failed with ", exception)
            }
    }




    //EKRANLAR

    if (load){
        LoadingScreen()
    }
    else{

        Log.d("AAAAAAAA","soru id:$questionId")
        Log.d("AAAAAAAA","value:$textFieldValue")
    if ((!insufficientDocument)&&(questionId<=textFieldValue)) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)

        ){
            TopAppBar(
                title = { Text(text = "Egzersiz", color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(route = Screen.Home.route)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri", tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                backgroundColor =  androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
                .padding(25.dp, 70.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {


                LazyColumn(
                    modifier = Modifier
                        .padding(11.dp)
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(text = " ${questionId}.Soru :", color = Color.Black, fontSize = 18.sp)
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(300.dp)
                                    .padding(30.dp)
                            ) {
                                imageBitmap?.let {
                                    Image(
                                        bitmap = it,
                                        contentDescription = "Firestore Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }

                            Text(text = englishWord)

                            OutlinedTextField(
                                value = answer,
                                onValueChange = { answer = it },
                                label = { Text("Cevap:") },
                                modifier = Modifier
                                    .padding(0.dp, 20.dp)
                            )
                            englishWordExamples.forEachIndexed { index, _ ->
                                var textFieldValue by remember { mutableStateOf("") }
                                Text(text = englishWordExamples[index])

                                OutlinedTextField(
                                    value = textFieldValue,
                                    onValueChange = { newValue ->
                                        textFieldValue = newValue
                                        answer1 = answer1.toMutableList().also {
                                            it[index] = newValue
                                        }
                                    },
                                    label = { Text("Cevap:") },
                                    modifier = Modifier
                                        .padding(0.dp, 20.dp)
                                )
                            }

                            GradientIconButton(
                                vector = Icons.Default.Check,
                                iconColor = Color.White,
                                gradient = Brush.horizontalGradient(
                                    colors = listOf(
                                        color1, color2
                                    )
                                )
                            ) {
                                answer1 = answer1.map { it.lowercase() }.toMutableList()
                                turkishWordExamples =
                                    turkishWordExamples.map { it.lowercase() }.toMutableList()

                                if (answer.lowercase(Locale.getDefault()) == turkishWord && answer1 == turkishWordExamples) {
                                    Toast.makeText(
                                        context, "Doğru Cevap !!!",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    streak++
                                    correctCount++

                                } else {
                                    Toast.makeText(
                                        context, "Yanlış Cevap !!!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.d("second", "Yanlış")
                                    Log.d("second", answer1.toString())

                                    streak = 0
                                }
                                questionId++
                                questionCount++
                                editor.putLong("lastQuestionId", questionId)
                                editor.putLong("lastQuestionExpiration", expirationTime)
                                editor.apply()



                                timeStampController()



                                Log.d("AGALAR", "formatted $formattedDate")
                                Log.d("AGALAR", "TimeStamp $timestamp")
                                var newData: Map<String, Any> = hashMapOf(
                                    "streak" to streak, // Yeni değeri belirtin
                                    "timeStamp" to formattedDate,
                                    "correctCount" to correctCount,
                                    "questionCount" to questionCount
                                )


                                db.collection("users")
                                    .document(currentUser)
                                    .collection("words")
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) {
                                            if (documentId == document.id) {

                                                document.reference.update(newData)
                                                    .addOnSuccessListener {
                                                        Log.d(
                                                            "TAG",
                                                            "Document updated successfully"
                                                        )
                                                    }
                                                    .addOnFailureListener { exception ->
                                                        Log.d(
                                                            "TAG",
                                                            "Error updating document: $exception"
                                                        )
                                                    }
                                            }
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.d("TAG", "Error getting documents: $exception")
                                    }

                                calendar.time = currentDate
                                formattedDate = dateFormat.format(calendar.time)
                                load = true
                                last = true
                                correctCount = 0
                                questionCount = 0

                            }
                        }
                    }

                }
            }
        }
    }
    }
    if ((insufficientDocument)&&(questionId<= textFieldValue)){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
                .padding(25.dp, 90.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = " LÜTFEN KELİME EKLEYİNİZ ", color =androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 24.sp)
                    Spacer(modifier = Modifier.padding(10.dp))
                    GradientIconButton(vector = Icons.Rounded.ArrowForward, iconColor = Color.White, gradient = Brush.horizontalGradient(
                        colors = listOf(color1,color2)
                    ),
                        onClick = { navController.navigate(route = Screen.First.route)})


                }
            }
        }
    }
        if(questionId>textFieldValue){
        QuestionLimitScreen()
    }
    }
}



fun deleteDocument(db:FirebaseFirestore,currentUser:String,documentId: String){
    db.collection("users")
        .document(currentUser)
        .collection("words").document(documentId).delete().addOnSuccessListener {
            Log.d("Firestore", "Belge başarıyla silindi.")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Belge silinirken hata oluştu.", e)
        }
}


@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer)
    }
}

@Composable
fun QuestionLimitScreen() {
    Box(modifier = Modifier.background(color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer),
        contentAlignment = Alignment.Center
    ) {
       Text(text = "Bugünkü soru limitine ulaştınız. Eğer soru limitini değiştirmek istiyorsanız, ana sayfadaki ayarlar menüsünden değiştirebilirsiniz.", color = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer, fontSize = 18.sp,modifier = Modifier.align(Alignment.Center) )
    }}
}