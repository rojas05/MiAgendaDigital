package cristian.rojas.elparaiso

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(  foreignKeys = [
    ForeignKey(entity = entidadFinca::class, parentColumns = ["id_finca"], childColumns = ["finca"])
])

class entidadIngresos (
    @PrimaryKey(autoGenerate = true) val id_ingreso: Int?,
    @ColumnInfo(name = "producto") val producto: String?,
    @ColumnInfo(name = "fecha") val fecha: String?,
    @ColumnInfo(name = "precio") val precio: Int?,
    @ColumnInfo(name = "descripcion") val descripcion: String,
    @ColumnInfo(name = "finca") val finca: Int
)