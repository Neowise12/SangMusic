package com.anu.sangmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySongs extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();;
        mediaPlayer.release();
        updateseek.interrupt();
    }

    private TextView textView;
    private ImageView play;
    private ImageView previous;
    private ImageView next;
    private SeekBar seekBar;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String name;
    int position;
    Thread updateseek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_songs);
        textView = findViewById(R.id.textView);
        seekBar = findViewById(R.id.seekBar);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs =(ArrayList)bundle.getParcelableArrayList("Songs list");
        name = intent.getStringExtra("Current Songs");
        textView.setText(name);
        textView.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer =MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        updateseek = new Thread(){
            @Override
            public void run() {
                int currentpositon =0;
                try {

                    while(currentpositon<mediaPlayer.getDuration()){
                        currentpositon= mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentpositon);
                        sleep(800);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                super.run();
            }
        };
        updateseek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();

                }
                else {
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }


            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position = position-1;
                }
                else {
                    position = songs.size()-1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer =MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                name = songs.get(position).getName();
                textView.setText(name);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                play.setImageResource(R.drawable.pause);
                if (position!=songs.size()-1){
                    position = position+1;
                }
                else {
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer =MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                name = intent.getStringExtra("Current Songs");
                textView.setText(name);
                name = songs.get(position).getName();
                textView.setText(name);
            }
        });




        
    }
}