package com.coding.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.os.Handler
import android.widget.Toast
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val title_txt : TextView = findViewById(R.id.song_title)
        var time_txt : TextView = findViewById(R.id.time)
        var seekBar: SeekBar = findViewById(R.id.seekbar)
        var back : Button = findViewById(R.id.backbutton)
        var forward : Button = findViewById(R.id.forward_icon)
        var pause  : Button = findViewById(R.id.pause_icon)
        var play : Button = findViewById(R.id.play_button)
        var oneTimeOnly : Int = 0
        val handler = Handler()
        val forwardTime : Long = 10000
        val backwardTime : Long = 10000

        //Media Player
        var mediaPlayer = MediaPlayer.create(this, R.raw.hummingbird)
        seekBar.isClickable = false

        var finalTime = mediaPlayer.duration.toDouble()
        var startTime = mediaPlayer.currentPosition.toDouble()


        //Adding Functionalities to the buttons

        //Creating the Runnable
        val UpdateSongTime: Runnable = object : Runnable {
            override fun run() {
                startTime = mediaPlayer.currentPosition.toDouble()
                time_txt.text = "" +
                        String.format(
                            "%d min : %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                            TimeUnit.MILLISECONDS.toSeconds(
                                startTime.toLong()
                                        - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(
                                        startTime.toLong()
                                    )
                                )
                            )
                        )

                seekBar.progress = startTime.toInt()
                handler.postDelayed(this, 100)
            }
        }


        play.setOnClickListener() {
            mediaPlayer.start()


            if (oneTimeOnly == 0) {
                seekBar.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            time_txt.text = startTime.toString()
            seekBar.setProgress(startTime.toInt())

            handler.postDelayed(UpdateSongTime, 100)

        }
        title_txt.text = ""+ resources.getResourceEntryName(R.raw.hummingbird)

        //Stop button
        pause.setOnClickListener(){
            mediaPlayer.pause()
        }


        //Setting the music Title

            //Forward Button
            forward.setOnClickListener(){
                var temp = startTime

                if((temp + forwardTime)<= finalTime){
                    startTime = startTime + forwardTime
                    mediaPlayer.seekTo(startTime.toInt())
                }
                else{
                    Toast.makeText(this,"Cannot Jump Forward",Toast.LENGTH_LONG).show()
                }
            }

            //Backward Button
            back.setOnClickListener(){
                var temp =  startTime.toInt()

                if((temp - backwardTime) > 0){
                    startTime = startTime - backwardTime
                    mediaPlayer.seekTo(startTime.toInt())
                }
                else{
                    Toast.makeText(this,"Cannot Jump Backward",Toast.LENGTH_LONG).show()
                }
            }

    }

}