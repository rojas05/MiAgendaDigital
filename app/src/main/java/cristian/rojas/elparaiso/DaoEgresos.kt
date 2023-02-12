package cristian.rojas.elparaiso

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaoEgresos {
    @Insert
    suspend fun insertEgresos(vararg Egreso: entidadEgresos)

    @Query("SELECT * FROM entidadEgresos WHERE tipo = 'Insumo' ORDER BY id_egreso DESC")
    suspend fun getInsumos(): List<entidadEgresos>

    @Query("SELECT * FROM entidadEgresos WHERE tipo = 'Trabajo' ORDER BY id_egreso DESC")
    suspend fun getTrabajos(): List<entidadEgresos>

    @Query("SELECT * FROM entidadEgresos WHERE tipo = 'Otro' ORDER BY id_egreso DESC")
    suspend fun getOtros(): List<entidadEgresos>

    @Query("update entidadEgresos set descripcion =:descripcion, fecha =:fecha, costo =:precio where id_egreso=:id")
    suspend fun updateEgreso(descripcion: String,fecha: String,precio: Int,id: Int)

    @Query("delete from entidadEgresos where id_egreso=:id")
    suspend fun deleteEgreso(id: Int)

    @Query("delete from entidadEgresos WHERE fecha LIKE :year")
    suspend fun delete(year: String)

    @Query("SELECT sum(costo) FROM entidadEgresos WHERE tipo = 'Insumo' AND fecha LIKE :year ")
    suspend fun getTotalInsumo(year: String): List<Int>

    @Query("SELECT sum(costo) FROM entidadEgresos WHERE tipo = 'Trabajo' AND fecha LIKE :year")
    suspend fun getTotalTrabajo(year: String): List<Int>

    @Query("SELECT sum(costo) FROM entidadEgresos WHERE tipo = 'Otro' AND fecha LIKE :year")
    suspend fun getTotalOtro(year: String): List<Int>

    @Query("SELECT sum(costo) FROM entidadEgresos WHERE fecha LIKE :year")
    suspend fun getTotalEgresos(year: String): List<Int>

    //<----------------------------- cierre anual-------------------------------->

    @Query("SELECT * FROM entidadEgresos WHERE tipo = 'Insumo' AND fecha LIKE :year ORDER BY id_egreso DESC")
    suspend fun getInsumosCierre(year: String): List<entidadEgresos>

    @Query("SELECT * FROM entidadEgresos WHERE tipo = 'Trabajo' AND fecha LIKE :year ORDER BY id_egreso DESC")
    suspend fun getTrabajosCierre(year: String): List<entidadEgresos>

    @Query("SELECT * FROM entidadEgresos WHERE tipo = 'Otro' AND fecha LIKE :year ORDER BY id_egreso DESC")
    suspend fun getOtrosCierre(year: String): List<entidadEgresos>
}