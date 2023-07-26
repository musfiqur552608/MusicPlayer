package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button forward, backward, play, pause;
    TextView time, title;
    SeekBar seekBar;

    MediaPlayer mediaPlayer;

    Handler handler = new Handler();

    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int onetime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forward = findViewById(R.id.forwordbtn);
        backward = findViewById(R.id.backword);
        play = findViewById(R.id.playbtn);
        pause = findViewById(R.id.pausebtn);
        seekBar = findViewById(R.id.seekBar);
        time = findViewById(R.id.time_left);
        title = findViewById(R.id.songtitle);


        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        seekBar.setClickable(false);

        title.setText(getResources().getIdentifier("music","raw",getPackageName()));

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if((temp+forwardTime) <= finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else {
                    Toast.makeText(MainActivity.this, "Can't the jump forward!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if((temp-backwardTime) > 0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else {
                    Toast.makeText(MainActivity.this, "Can't go back!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void PlayMusic() {
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if(onetime == 0){
            seekBar.setMax((int) finalTime);
            onetime = 1;
        }

        time.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)-
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))

        ));

        seekBar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime, 100);
    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            time.setText(String.format("%d min, %d sec",

                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime)-
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
                    ));

            seekBar.setProgress((int)startTime);
            handler.postDelayed(this,100);
        }
    };
}