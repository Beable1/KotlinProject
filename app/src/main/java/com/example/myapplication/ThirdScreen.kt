package com.example.myapplication

import Word
import WordViewModel
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.PieChart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.checkerframework.checker.units.qual.Angle

@Composable
fun ThirdScreen(navController: NavController, viewModel: WordViewModel = viewModel()){
    val words by viewModel.words.collectAsState()




        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TopAppBar(
                title = { Text(text = "İstatistikler", color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(route = Screen.Home.route)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Çıkış", tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ){

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ){

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(words) { word ->
                            WordItem(word)
                            Spacer(modifier = Modifier.padding(10.dp))
                        }
                    }
                }

            }

        }

}

@Composable
@Preview
fun ThirdScreenPreview(){
    ThirdScreen(navController = rememberNavController())
}

@Composable
fun WordItem(word: Word) {
    
    Row(modifier = Modifier
        .background(color = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer)
        .padding(10.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier
            .padding(30.dp)
            .size(80.dp)){

            PieChart(data = mapOf(
            Pair("Yanlış Sayısı", word.questionCount-word.correctCount),
            Pair("Doğru Sayısı", word.correctCount)
        ), radiusOuter = 50.dp, chartBarWidth = 10.dp
        )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Text(text = "Kelime: ${word.englishWord}",color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "Doğru Sayısı: ${word.correctCount}",color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "Yanlış Sayısı: ${word.questionCount-word.correctCount}",color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "Art Arda Bilme Sayısı: ${word.streak}",color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)


        }
    }
    
}
