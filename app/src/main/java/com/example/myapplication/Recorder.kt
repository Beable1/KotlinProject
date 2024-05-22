package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private val REQUEST_CODE = 1000

@SuppressLint("QueryPermissionsNeeded")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun Recorder() {
    var isRecording by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var audioRecorder: AudioRecord? by remember { mutableStateOf(null) }
    var buttonClicked by remember { mutableStateOf(false) }
    var outputFile: File? by remember { mutableStateOf(null) }
    val mediaPlayer = remember { MediaPlayer() }
    val mediaRecorder = MediaRecorder()

    val bufferSize = AudioRecord.getMinBufferSize(
        44100,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    val audioRecordListener = Runnable {
        val audioData = ByteArray(bufferSize)
        val outputDir = File(context.filesDir, "recordings")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val audioFile = File(outputDir, "recorded_audio.wav")
        val outputStream = FileOutputStream(audioFile)
        audioRecorder?.startRecording()
        while (isRecording) {
            val bytesRead = audioRecorder?.read(audioData, 0, bufferSize) ?: 0
            outputStream.write(audioData, 0, bytesRead)
        }
        audioRecorder?.stop()
        outputStream.close()
        outputFile = audioFile
    }

    Column {
        Button(
            onClick = {
                if (!isRecording) {
                    stopRecording(mediaRecorder)

                } else {
                    recordAudio(context,outputFile,mediaRecorder)
                }
                buttonClicked = true
            }) {
            Text(if (isRecording) "Stop Recording" else "Start Recording")
        }

        Button(onClick = {
            outputFile?.let {
                playAudio(context, it, mediaPlayer)
            } ?: Log.e("Recorder", "Kaydedilen ses dosyası bulunamadı.")
        }) {
            Text(text = "dinle")
        }
    }
}

@Composable
@Preview
fun RecorderPreview() {
    Recorder()
}

fun playAudio(context: Context, file: File?, mediaPlayer: MediaPlayer) {
    file?.let {
        if (it.exists()) {
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(it.path)
                mediaPlayer.prepare()
                mediaPlayer.start()
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer.release()
                    Log.d("Recorder", "başarılı.")
                }
            } catch (e: IOException) {
                Log.e("Recorder", "Ses dosyası oynatılırken hata oluştu: ${e.message}")
            } catch (e: IllegalStateException) {
                Log.e("Recorder", "MediaPlayer durumu hatalı: ${e.message}")
            }
        } else {
            Log.d("Recorder", "Kaydedilen ses dosyası bulunamadı.")
        }
    } ?: Log.e("Recorder", "Kaydedilen ses dosyası bulunamadı.")
}

fun recordAudio(context:Context,file: File?,mediaRecorder: MediaRecorder) {
    // Gerekli izinleri kontrol et
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
        // MediaRecorder oluştur

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder.setOutputFile(file)

        try {
            // Ses kaydını başlat
            mediaRecorder.prepare()
            mediaRecorder.start()
        } catch (e: IOException) {
            Log.e(TAG, "Ses kaydı başlatılamadı: ${e.message}")
        }
    } else {
        // İzin verilmediğinde kullanıcıya bilgi ver
        Toast.makeText(context, "Ses kaydı için izin verilmedi", Toast.LENGTH_SHORT).show()
    }
}

fun stopRecording(mediaRecorder: MediaRecorder) {
    if (mediaRecorder != null) {
        mediaRecorder.stop()
        mediaRecorder.release()
    }
}