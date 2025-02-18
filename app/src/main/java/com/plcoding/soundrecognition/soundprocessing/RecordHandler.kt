package com.plcoding.soundrecognition.soundprocessing

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import android.widget.TextView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

import com.jjoe64.graphview.series.DataPoint
import com.plcoding.soundrecognition.MainActivity
import com.plcoding.soundrecognition.data.models.DataSound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

const val SAMPLE_RATE = 10100
const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

class RecordHandler(
    val graphHandler: GraphHandler
) {

    private var mAudioRecord: AudioRecord? = null
    private var recorder: MediaRecorder? = null
    private var mMediaPlayer: MediaPlayer? = null


    fun startPlaying(textTest: TextView, animalNameText: TextView, animalTypeText: TextView, fileName:String): Boolean {
        graphHandler.mAmplitudeSeries?.resetData(arrayOf<DataPoint>())
        graphHandler.mTimeSeries?.resetData(arrayOf<DataPoint>())
        graphHandler.mFullFreqSeries?.resetData(arrayOf<DataPoint>())

        val sound = graphHandler.createDataSound(true, animalNameText, animalTypeText)
        val stringBuilder = sound.toString()
        GlobalScope.launch( Dispatchers.Main ){
            textTest.text = stringBuilder
        }

        mMediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("", "prepare() failed")
            }
        }
        mAudioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, mMinBufferSize)
        mAudioRecord!!.startRecording()

        val playedOut = graphHandler.replayGraphView(mAudioRecord!!)

        return playedOut
    }

    fun stopPlaying() {
        mMediaPlayer?.release()
        mAudioRecord?.stop()
        mAudioRecord?.release()
    }

    fun startRecording(fileName:String) {
        graphHandler.dataGraphs.currentRecordTimeDomain.clear()
        mAudioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT,
            mMinBufferSize
        )
        mAudioRecord!!.startRecording()

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("tag", "prepare() failed")
            }

            start()
        }

        graphHandler.updateGraphView(mAudioRecord!!)
        //mRecordThread!!.start()
    }

    fun stopRecording() {
        if (mAudioRecord != null) {
            if (isRecording) {
                mAudioRecord?.stop()
                // tutaj cos zmienic z tym sleep?
            }
            mAudioRecord?.release()
        }

        recorder?.apply {
            stop()
            release()
        }
    }


    companion object {
        val mMinBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
        var isPlaying = false
        var isRecording = false
    }
}