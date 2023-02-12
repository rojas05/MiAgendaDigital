package cristian.rojas.elparaiso

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [entidadEgresos::class,entidadFinca::class,entidadIngresos::class], version = 1)
abstract class DB: RoomDatabase(){
    abstract fun DaoEgresos():DaoEgresos
    abstract fun DaoFinca():DaoFinca
    abstract fun DaoIngresos():DaoIngresos
    companion object{
        var instancia: DB?=null
        fun getInstancia(context: Context):DB {
            if (instancia == null){
                instancia = Room.databaseBuilder(
                    context.applicationContext,
                    DB::class.java,"ElParaiso"
                ).build()
            }
            return instancia!!
        }
    }
}