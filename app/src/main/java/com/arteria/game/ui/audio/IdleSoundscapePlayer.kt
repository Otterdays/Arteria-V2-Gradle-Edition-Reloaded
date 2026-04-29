package com.arteria.game.ui.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.sin

/**
 * Generates a soft ambient drone via PCM synthesis — two detuned sine waves with a slow
 * breathing envelope. No audio assets required. Lifecycle: call [start], then [stop] when done.
 */
class IdleSoundscapePlayer {

    private val sampleRate = 44100
    // 1-second chunk: enough to keep the buffer fed without eating memory
    private val chunkSize = sampleRate

    @Volatile private var running = false
    @Volatile private var audioTrack: AudioTrack? = null
    private var playThread: Thread? = null

    fun start() {
        if (running) return
        running = true

        val minBuf = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
        )

        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .build(),
            )
            .setBufferSizeInBytes(maxOf(minBuf, chunkSize * 2 * Short.SIZE_BYTES))
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        track.setVolume(0.45f)
        track.play()
        audioTrack = track

        playThread = Thread {
            val buffer = ShortArray(chunkSize)
            var sampleIndex = 0L

            while (running) {
                fillBuffer(buffer, sampleIndex)
                val written = audioTrack?.write(buffer, 0, buffer.size) ?: break
                if (written <= 0) break
                sampleIndex += chunkSize
            }
        }.also {
            it.isDaemon = true
            it.name = "arteria-soundscape"
            it.start()
        }
    }

    fun stop() {
        running = false
        runCatching { audioTrack?.stop() }
        runCatching { audioTrack?.release() }
        audioTrack = null
        // Thread exits naturally after the current buffer write returns
    }

    private fun fillBuffer(buffer: ShortArray, startSample: Long) {
        for (i in buffer.indices) {
            val t = (startSample + i).toDouble() / sampleRate

            // Two detuned drones — 3.5 Hz beat creates a slow, organic pulse
            val drone1 = sin(2.0 * PI * 55.0 * t) * 0.40
            val drone2 = sin(2.0 * PI * 58.5 * t) * 0.28

            // Faint harmonic shimmer (space / ethereal feel)
            val shimmer = sin(2.0 * PI * 220.0 * t) * 0.06

            // Slow breathing envelope — 0.2 Hz = 5-second breath cycle
            val breath = sin(2.0 * PI * 0.2 * t) * 0.12 + 0.88

            val raw = ((drone1 + drone2 + shimmer) * breath * Short.MAX_VALUE).toInt()
            buffer[i] = raw.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
    }
}
