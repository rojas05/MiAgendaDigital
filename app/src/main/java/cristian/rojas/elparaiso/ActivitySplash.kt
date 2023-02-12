package cristian.rojas.elparaiso

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import cristian.rojas.elparaiso.databinding.ActivityMainBinding
import java.util.*

class ActivitySplash : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageView.setAnimation(R.raw.animation)
        binding.imageView.playAnimation()

        val preferencias = getSharedPreferences( "registro", Context.MODE_PRIVATE)
        val clave = preferencias.getString("clave","")

        if (clave == ""){
            startActivity(Intent(this,Activity_datos_finca::class.java))
            finish()
        }else{
            starTimer()
        }
    }

    fun starTimer() {
        object: CountDownTimer(5000,1){
            override fun onTick(p0: Long) {
            }
            override fun onFinish() {
                val intent = Intent(applicationContext, ActivityPrincipal::class.java).apply { }
                startActivity(intent)
                finish()
            }
        }.start()
    }
}



