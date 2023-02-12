package cristian.rojas.elparaiso

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaoFinca {
    @Insert
    suspend fun insertPerfil(vararg finca: entidadFinca)

    @Query("SELECT * FROM entidadFinca")
    suspend fun getFinca(): List<entidadFinca>

    @Query("update entidadFinca set propietario =:propietario, nombre =:nombre")
    suspend fun updateFinca(propietario: String,nombre: String)
}