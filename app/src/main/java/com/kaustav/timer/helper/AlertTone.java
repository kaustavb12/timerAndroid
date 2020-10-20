package com.kaustav.timer.helper;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Vibrator;
import android.widget.TextView;

public class AlertTone {
    private final double duration = 0.5; // seconds
    private final int sampleRate = 12000;
    private final int numSamples = (int)(duration * sampleRate);
    private final double sample[] = new double[numSamples];
    private final double freqOfTone = 540; // hz

    private final byte generatedSnd[] = new byte[6 * numSamples];

    public AlertTone(){
        this.genTone();
    }

    private void genTone(){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }

        for (int i=0; i<idx; i++){
            generatedSnd[idx+i] = (byte)0;
        }

        for(int i=0; i<idx; i++){
            generatedSnd[(2*idx)+i] = generatedSnd[i];
        }
    }

    public void playSound(){
        final AudioTrack audioTrack = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                .setAudioFormat(new AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(sampleRate).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                .setBufferSizeInBytes(generatedSnd.length)
                .build();
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }

}
