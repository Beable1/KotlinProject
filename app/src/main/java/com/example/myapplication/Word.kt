import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Word(
    val id: String = "",
    val englishWord: String = "",
    val turkishWord: String = "",
    val streak: Int = 0,
    val questionCount: Int = 0,
    val correctCount: Int = 0
)

class WordViewModel : ViewModel() {
    private val _words = MutableStateFlow<List<Word>>(emptyList())
    val words: StateFlow<List<Word>> get() = _words.asStateFlow()



    init {
        fetchWords()
    }

    private fun fetchWords() {
        val documentsList: MutableList<DocumentSnapshot> = mutableListOf()
        val currentUser = FirebaseAuth.getInstance().currentUser


        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("users").document(currentUser.uid).collection("words")
                .orderBy("timeStamp", Query.Direction.ASCENDING)


            query.get()
                .addOnSuccessListener { documents ->

                    documentsList.clear()
                    documentsList.addAll(documents.documents)
                    val wordList = mutableListOf<Word>()
                    for (document in documents) {

                        var questionCount = document.getLong("questionCount")!!.toInt()
                        if (questionCount!=0) {
                            val word = Word(
                                id = document.id,
                                englishWord = document.getString("englishWord") ?: "",
                                turkishWord = document.getString("turkishWord") ?: "",
                                streak = document.getLong("streak")?.toInt() ?: 0,
                                questionCount = document.getLong("questionCount")?.toInt() ?: 0,
                                correctCount = document.getLong("correctCount")?.toInt() ?: 0
                            )
                            wordList.add(word)
                        }

                    }
                    _words.value = wordList
                }
                .addOnFailureListener { exception ->
                    Log.d("WordViewModel", "get failed with ", exception)
                }
        }
    }
}
