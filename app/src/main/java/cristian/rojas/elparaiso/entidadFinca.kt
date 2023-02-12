package cristian.rojas.elparaiso

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class entidadFinca (
    @PrimaryKey(autoGenerate = true) val id_finca: Int?,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "propietario") val propietario: String,
        )