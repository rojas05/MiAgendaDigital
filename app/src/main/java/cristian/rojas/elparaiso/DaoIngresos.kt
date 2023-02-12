package cristian.rojas.elparaiso

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaoIngresos {
    @Insert
    suspend fun insertIngresos(vararg Egreso: entidadIngresos)

    @Query("SELECT * FROM entidadIngresos ORDER BY id_ingreso DESC")
    suspend fun getIngresos(): List<entidadIngresos>

    @Query("update entidadIngresos set descripcion =:descripcion, fecha =:fecha, precio =:precio, producto =:producto where id_ingreso=:id")
    suspend fun updateIngresos(descripcion: String,fecha: String,precio: Int,producto: String,id: Int)

    @Query("delete from entidadIngresos WHERE id_ingreso=:id")
    suspend fun deleteIngreso(id: Int)

    @Query("delete from entidadIngresos WHERE fecha LIKE :year")
    suspend fun delete(year: String)


    @Query("SELECT sum(precio) FROM entidadIngresos WHERE fecha LIKE :year ")
    suspend fun getTotalIngresos(year: String): List<Int>


    @Query("SELECT * FROM entidadIngresos WHERE fecha LIKE :year")
    suspend fun getIngresosCierre(year: String): List<entidadIngresos>
}