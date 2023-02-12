package cristian.rojas.elparaiso

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(entity = entidadFinca::class, parentColumns = ["id_finca"], childColumns = ["finca"])
    ])
class entidadEgresos (
    @PrimaryKey(autoGenerate = true) val id_egreso: Int?,
    @ColumnInfo(name = "fecha") val fecha: String?,
    @ColumnInfo(name = "costo") val costo: Int?,
    @ColumnInfo(name = "descripcion") val descripcion: String,
    @ColumnInfo(name = "tipo") val tipo: String,
    @ColumnInfo(name = "finca") val finca: Int
    )