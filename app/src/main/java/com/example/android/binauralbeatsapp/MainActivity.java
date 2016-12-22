package com.example.android.binauralbeatsapp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private final static int SAMPLE_RATE = 8000; // in Hz
    private final static int TONE_DURATION = 1; // in seconds

    private AudioTrack audioTrack;

    /** Initialize the AudioTrack object and volume control
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioTrack =  new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                SAMPLE_RATE * TONE_DURATION * 4, AudioTrack.MODE_STATIC);
        setupVolumeControl();
    }

    // Links volume bar to audio track
    private void setupVolumeControl() {
        SeekBar volControl = (SeekBar)findViewById(R.id.volumeBar);
        volControl.setMax(100);
        volControl.setProgress(100);
        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {}
            @Override
            public void onStartTrackingTouch(SeekBar arg0) {}
            @Override
            public void onProgressChanged(SeekBar arg0, int volume, boolean arg2) {
                audioTrack.setVolume(((float)volume)/100);
            }
        });
    }

    public void playTheta(View view) {
        playTones(300,304);
    }
    public void playGamma(View view) {
        playTones(400,410);
    }
    public void playDelta(View view) {
        playTones(600,601);
    }

    // Stops the audio and clears the buffer
    public void stop(View view) {
        audioTrack.pause();
        audioTrack.flush();
    }

    /**
     * Creates and plays a sine wave tone with different frequencies in each channel.
     * @param frequencyL The frequency (in Hz) of the tone to be played in the left ear
     * @param frequencyR The frequency (in Hz) of the tone to be played in the right ear
     */
    private void playTones(double frequencyL, double frequencyR) {
        int numSamples = TONE_DURATION * SAMPLE_RATE;
        double sampleL[] = new double[numSamples];
        double sampleR[] = new double[numSamples];
        byte generatedStereoSamples[] = new byte[4 * numSamples];
        for (int i = 0; i < numSamples; ++i) {      // Fill the sample array
            sampleR[i] = Math.sin(frequencyR * 2 * Math.PI * i / (SAMPLE_RATE));
            sampleL[i] = Math.sin(frequencyL * 2 * Math.PI * i / (SAMPLE_RATE));
        }
        // Converts to 16 bit pcm sound array
        // assumes the sample buffer is normalized.
        for (int idx = 0, i = 0; i < numSamples; ++i) {
            // Max amplitude for the samples
            final short valL = (short) (sampleR[i] * Short.MAX_VALUE);
            final short valR = (short) (sampleL[i] * Short.MAX_VALUE);
            // in 16 bit wav PCM, first byte is the low order byte
            generatedStereoSamples[idx++] = (byte) (valL & 0x00ff);
            generatedStereoSamples[idx++] = (byte) ((valL & 0xff00) >>> 8);
            generatedStereoSamples[idx++] = (byte) (valR & 0x00ff);
            generatedStereoSamples[idx++] = (byte) ((valR & 0xff00) >>> 8);
        }
        audioTrack.flush(); // Clear AudioTrack
        audioTrack.write(generatedStereoSamples, 0, generatedStereoSamples.length); // Send samples
        audioTrack.reloadStaticData(); // Added to use Static Mode, needed for looping
        audioTrack.setLoopPoints(0,audioTrack.getBufferSizeInFrames(),-1); // Infinite loop of the sample
        audioTrack.play();
    }
}
