package com.example.musicplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    // definindo atributos
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var startTime: Double = 0.0
    private var finalTime: Double = 0.0
    private var oneTimeOnly: Int = 0
    private var handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var time_text: TextView
    private lateinit var skBar: SeekBar
    private var forwardTime: Int = 10000
    private var backwardTime: Int = 10000

    @SuppressLint("MissingInflatedId", "SetTextI18n", "DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val nameMusic: TextView = findViewById(R.id.textView2)
        skBar= findViewById(R.id.seekBar)
        val musicText: TextView = findViewById(R.id.textView2)
        val leftBtn: Button = findViewById(R.id.btn2)
        val play: Button = findViewById(R.id.btn3)
        val pause: Button = findViewById(R.id.btn4)
        val rightBtn: Button = findViewById(R.id.btn5)
        time_text = findViewById(R.id.textView3)

        // criando um mediaPlayer atraves da classe MediaPlayer
        // passamos o create para iniciar nossa classe, passando como parametros o contexto e o uri do arquivo
        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.cannibalcorpse_srs,
        )

        skBar.isClickable = false

        // setando o nome da musica
        musicText.text = resources.getIdentifier(
            "cannibalcorpse_srs",
            "raw",
            packageName
        ).toString()


        // definindo funcoes do botao de play
        play.setOnClickListener(){
            // iniciando a musica assim que tocar o botao de start
            mediaPlayer.start()

            // utilizando o mediaPlayer para capturar a duracao do player e a posicao incial
            finalTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.currentPosition.toDouble()

            // fazendo uma estrutura condicional para definir o final do player
            if (oneTimeOnly == 0) {
                skBar.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            // setando o tempo da musica
            time_text.text = startTime.toString()
            // setando progresso com o seekaBar
            skBar.setProgress(startTime.toInt())

            // usando um handler para manipular atualizacoes de som e setar delay
            handler.postDelayed(UpdateSongTime, 100)
        }

        pause.setOnClickListener(){
            mediaPlayer.pause()
        }

        leftBtn.setOnClickListener(){
            val temp: Double = startTime
            if((temp - backwardTime) > 0) {
                startTime -= backwardTime
                mediaPlayer.seekTo(startTime.toInt())
            } else {
               Toast.makeText(
                   this,
                   "A musica ja esta no comeco",
                   Toast.LENGTH_LONG
               ).show()
            }
        }

        rightBtn.setOnClickListener(){
            val temp: Double = startTime
            if ((temp + forwardTime) <= finalTime) {
                startTime += forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            } else {
                Toast.makeText(
                    this,
                    "A musica esta acabando",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // criando interface runnable para verificar atualizacoes no player
    // lembrando que o runnable e uma acao assincrona
    val UpdateSongTime: Runnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            // utilizando o string format para formatar a string de tempo do player
            // passando modificando e transformando minutos e segundos
            time_text.text = "" +
                    String.format(
                        "%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()
                            - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    startTime.toLong()
                                )
                            )
                    ))

            // definindo o tempo de inicio da barra de progresso e o manipulador passando os parametros necessarios
            skBar.progress = startTime.toInt()
            handler.postDelayed(this, 100)
        }
    }
}
