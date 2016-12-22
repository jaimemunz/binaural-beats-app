package com.example.android.binauralbeatsapp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private final static int SAMPLE_RATE = 8000;
    private final static int TONE_DURATION = 1;

    AudioTrack audioTrack;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioTrack =  new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                SAMPLE_RATE * TONE_DURATION * 4, AudioTrack.MODE_STATIC);
        int maxVolume = 100;
        int curVolume = 100;
        SeekBar volControl = (SeekBar)findViewById(R.id.volumeBar);
        volControl.setMax(maxVolume);
        volControl.setProgress(curVolume);
        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int volume, boolean arg2) {
                audioTrack.setVolume(((float)volume)/100);
            }
        });
    }



    public void playTheta(View view) {
        genTone(300,304,true);
    }
    public void playGamma(View view) {
        genTone(400,410,true);
    }
    public void playDelta(View view) {
        genTone(600,601,true);
    }


    public void stop(View view) {
        audioTrack.pause();
        audioTrack.flush();
    }


    private void genTone(double frequencyL, double frequencyR, boolean play) {
        int sampleRate = SAMPLE_RATE;              // a number
        int duration = TONE_DURATION;
        double dnumSamples = duration * sampleRate;
        dnumSamples = Math.ceil(dnumSamples);
        int numSamples = (int) dnumSamples;
        double sampleL[] = new double[numSamples];
        double sampleR[] = new double[numSamples];
        byte generatedSnd[] = new byte[2 * numSamples];
        byte generatedStereo[] = new byte[4 * numSamples];

        audioTrack.flush();

        for (int i = 0; i < numSamples; ++i) {      // Fill the sample array
            sampleL[i] = Math.sin(frequencyR * 2 * Math.PI * i / (sampleRate));
            sampleR[i] = Math.sin(frequencyL * 2 * Math.PI * i / (sampleRate));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalized.
        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        int i = 0 ;

        int ramp = numSamples * 0 / 100 ;                                    // Amplitude ramp as a percent of sample count


        for (i = 0; i< ramp; ++i) {                                     // Ramp amplitude up (to avoid clicks)
            // Ramp up to maximum
            final short valL = (short) ((sampleL[i] * Short.MAX_VALUE * i/ramp));
            final short valR = (short) ((sampleR[i] * Short.MAX_VALUE * i/ramp));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedStereo[idx++] = (byte) (valL & 0x00ff);
            generatedStereo[idx++] = (byte) ((valL & 0xff00) >>> 8);
            generatedStereo[idx++] = (byte) (valR & 0x00ff);
            generatedStereo[idx++] = (byte) ((valR & 0xff00) >>> 8);
        }


        for (i = i; i< numSamples - ramp; ++i) {                        // Max amplitude for most of the samples
            final short valL = (short) ((sampleL[i] * Short.MAX_VALUE));
            final short valR = (short) ((sampleR[i] * Short.MAX_VALUE));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedStereo[idx++] = (byte) (valL & 0x00ff);
            generatedStereo[idx++] = (byte) ((valL & 0xff00) >>> 8);
            generatedStereo[idx++] = (byte) (valR & 0x00ff);
            generatedStereo[idx++] = (byte) ((valR & 0xff00) >>> 8);
        }

        for (i = i; i< numSamples; ++i) {                               // Ramp amplitude down
            // Ramp down to zero
            final short valL = (short) ((sampleL[i] * Short.MAX_VALUE * (numSamples-i)/ramp));
            final short valR = (short) ((sampleR[i] * Short.MAX_VALUE * (numSamples-i)/ramp));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedStereo[idx++] = (byte) (valL & 0x00ff);
            generatedStereo[idx++] = (byte) ((valL & 0xff00) >>> 8);
            generatedStereo[idx++] = (byte) (valR & 0x00ff);
            generatedStereo[idx++] = (byte) ((valR & 0xff00) >>> 8);
        }
        audioTrack.write(generatedStereo, 0, generatedStereo.length);     // Load the track
        audioTrack.reloadStaticData();
        audioTrack.setLoopPoints(0,audioTrack.getBufferSizeInFrames(),-1);

        audioTrack.play();

    }
}
