package vip.inode.demo.webrtc

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    var isStop = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private val audioRes by lazy {
        resources.openRawResource(R.raw.recorder)
    }

    fun onClick(view: View) {
        when (view) {
            start_btn -> {
                isStop = false
                thread {
                    val enabledNsAgc = enable_ns_agc_switch.isChecked
                    val audioManager: AudioManager =
                        getSystemService(Context.AUDIO_SERVICE) as AudioManager
                    val bufferSize: Int =
                        AudioTrack.getMinBufferSize(
                            16000,
                            AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_16BIT
                        )
                    val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build()
                    val audioFormat: AudioFormat = AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(16000)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                    val sessionId = audioManager.generateAudioSessionId()
                    val audioTrack =
                        AudioTrack(
                            audioAttributes, audioFormat, bufferSize, AudioTrack.MODE_STREAM,
                            sessionId
                        )
                    var nsUtils: NoiseSuppressorUtils? = null
                    var nsxId = 0L
                    var agcUtils: AutomaticGainControlUtils? = null
                    var agcId = 0L
                    if (enabledNsAgc) {
                        nsUtils = NoiseSuppressorUtils()
                        nsxId = nsUtils.nsxCreate()
                        val nsxInit = nsUtils.nsxInit(nsxId, 16000)
                        val nexSetPolicy = nsUtils.nsxSetPolicy(nsxId, 2)
                        Log.i(tag, "nsxId : $nsxId  nsxInit: $nsxInit nexSetPolicy: $nexSetPolicy")

                        agcUtils = AutomaticGainControlUtils()
                        agcId = agcUtils.agcCreate()
                        val agcInitResult = agcUtils.agcInit(agcId, 0, 255, 3, 16000)
                        val agcSetConfigResult = agcUtils.agcSetConfig(agcId, 9, 9, true)
                        Log.e(
                            tag,
                            "agcId : $agcId  agcInit: $agcInitResult agcSetConfig: $agcSetConfigResult"
                        )
                    }
                    kotlin.run {
                        audioTrack.play()
                        audioRes.reset()
                        val audioData = audioRes.readBytes()
                        if (isStop) {
                            return@run
                        }
                        audioData.asSequence().chunked(320).filter { it.size == 320 }.forEach {
                            val byteArray = it.toByteArray()
                            if (enabledNsAgc) {
                                val inputData = ShortArray(160)
                                val outNsData = ShortArray(160)
                                val outAgcData = ShortArray(160)
                                ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN)
                                    .asShortBuffer()
                                    .get(inputData)
                                nsUtils!!.nsxProcess(nsxId, inputData, 1, outNsData)
                                agcUtils!!.agcProcess(
                                    agcId, outNsData, 1, 160, outAgcData,
                                    0, 0, 0, false
                                )
                                if (isStop) {
                                    return@run
                                }
                                audioTrack.write(outAgcData, 0, 160)
                            } else {
                                audioTrack.write(byteArray, 0, byteArray.size)
                            }
                            if (isStop) {
                                return@run
                            }
                        }
                    }
                    nsUtils?.nsxFree(nsxId)
                    agcUtils?.agcFree(agcId)
                    audioTrack.stop()
                    audioTrack.release()
                }
            }
            stop_btn -> {
                isStop = true
            }
            else -> {
            }
        }
    }

    override fun onStop() {
        isStop = true
        super.onStop()
    }

}
