package cristian.rojas.elparaiso


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.os.StrictMode.VmPolicy
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.itextpdf.text.*
import cristian.rojas.elparaiso.databinding.ActivityPrincipalBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


@Suppress("UNUSED_EXPRESSION")
class ActivityPrincipal : AppCompatActivity() {

    lateinit var binding: ActivityPrincipalBinding

    private lateinit var Datosfinca:List<entidadFinca>

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        CoroutineScope(Dispatchers.IO).launch {Datosfinca = DB.getInstancia(this@ActivityPrincipal).DaoFinca().getFinca()}
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpTabBar()

        if(checkPermission()) {
            Toast.makeText(this, getString(R.string.bienvenido), Toast.LENGTH_SHORT).show()
        } else {
            requestPermissions()
        }

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    private fun setUpTabBar() {
        val tabLayout = binding.tabLayout
        val adapter = tabClassAdapter(this,tabLayout.tabCount)
        binding.ViewPager.adapter = adapter

        binding.ViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.ViewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.informe -> startActivity(Intent(this,ActivityInformeRapido::class.java))
            R.id.cierre -> cierre()
            R.id.datos -> datos()
            R.id.delete -> deleteNotas()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteNotas() {
        DialogDeleteNotas(
            delete =
            {
                delete(it.toString())
            }
        ).show(supportFragmentManager, "dialog")
    }

    private fun datos() {
        DialogUpdateDatosIniciales(
            Datosfinca,
            update =
            {
                updateDatosFinca(it)
            }
        ).show(supportFragmentManager, "dialog")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun cierre() {
        if(checkPermission()) {
            DialogCierre(
                Pdfyear = { itYear ->
                    Pdf(
                        this@ActivityPrincipal,
                        "${itYear}%"
                    ) {
                        openFolder(
                            it
                        )
                    }.crearPdf()
                }
            ).show(supportFragmentManager, "dialog")
        } else {
            requestPermissions()
        }
    }

    private fun updateDatosFinca(it: entidadFinca) {
        CoroutineScope(Dispatchers.IO).launch{
            if (it.nombre != null && it.propietario != null) {
                DB.getInstancia(this@ActivityPrincipal).DaoFinca().updateFinca(
                    it.propietario,it.nombre
                )
            }
            mensaje("datos actualizados correctamente")
        }
        preferences(it.nombre)
    }

    private fun preferences (finca : String){
        val preferencias = getSharedPreferences( "registro", Context.MODE_PRIVATE)
        val editor = preferencias.edit()

        editor.putString("finca",finca)
        editor.apply()
    }

    private fun mensaje(text: String) {
        Snackbar.make(
            findViewById(R.id.principal),
            text,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun checkPermission(): Boolean {
        val permission1 =
            ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val permission2 =
            ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            200
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 200) {
            if (grantResults.isNotEmpty()) {
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_LONG).show()
                } else {
                    DialogAlert().show(supportFragmentManager, "dialog")
                }
            }
        }
    }

    fun delete (year: String){
        val yearDelete = year.toInt() - 1
        val yearString = "$yearDelete%"
        CoroutineScope(Dispatchers.IO).launch{
            DB.getInstancia(this@ActivityPrincipal).DaoEgresos().delete(yearString)
            DB.getInstancia(this@ActivityPrincipal).DaoIngresos().delete(yearString)
            startActivity(Intent(this@ActivityPrincipal, ActivityPrincipal::class.java))
        }
    }

    fun openFolder(folder: String ) {
        val selectedUri =
            Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/$folder")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(selectedUri, "application/pdf")
        startActivity(Intent.createChooser(intent, "Abrir $folder en"))
    }
}