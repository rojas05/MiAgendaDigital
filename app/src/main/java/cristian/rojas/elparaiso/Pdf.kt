package cristian.rojas.elparaiso

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.stream.Stream


class Pdf(
    var context: Context,
    var year: String,
    var location: (String) -> Unit
){


    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.N)
    fun crearPdf() {

        CoroutineScope(Dispatchers.IO).launch {

            val insumos = DB.getInstancia(context).DaoEgresos().getInsumosCierre(year)
            val totalInsumos = DB.getInstancia(context).DaoEgresos().getTotalInsumo(year)

            val trabajos = DB.getInstancia(context).DaoEgresos().getTrabajosCierre(year)
            val totalTrabajos = DB.getInstancia(context).DaoEgresos().getTotalTrabajo(year)

            val otros = DB.getInstancia(context).DaoEgresos().getOtrosCierre(year)
            val totalOtros = DB.getInstancia(context).DaoEgresos().getTotalOtro(year)

            val totalEgresos = DB.getInstancia(context).DaoEgresos().getTotalEgresos(year)

            val ingresos = DB.getInstancia(context).DaoIngresos().getIngresosCierre(year)
            val totalIngresos = DB.getInstancia(context).DaoIngresos().getTotalIngresos(year)

            val Datosfinca = DB.getInstancia(context).DaoFinca().getFinca()

            var finca = ""

            for (item in Datosfinca) {
                finca = item.nombre
            }

            val folder = "/Informes_Empresa_" + finca.replace(" ","_")

            val path =
                Environment.getExternalStorageDirectory().absolutePath + folder

            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val newYear = year.replace("%","")

            val fileName = "Cierre${newYear}.pdf"
            val file = File(dir, fileName )
            val fileOutputStream = FileOutputStream(file)
            val documento = Document()
            PdfWriter.getInstance(documento, fileOutputStream)
            documento.open()

            val titulo = Paragraph(
                "Cierre Año ${newYear}. Sistema De Contabilidad Mi Agenda Digital \n\n\n",
                FontFactory.getFont("arial", 22f, Font.BOLD, BaseColor.BLACK)
            )
            titulo.alignment = Element.ALIGN_CENTER

            documento.add(titulo)

            val tituloTablaDatos = PdfPTable(1)
            Stream.of("Datos Empresa").forEach {
                val header = PdfPCell()
                val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.WHITE)
                header.backgroundColor = BaseColor.BLUE
                header.horizontalAlignment = Element.ALIGN_CENTER
                header.borderWidth = 2f
                header.phrase = Phrase(it, headerFont)
                tituloTablaDatos.addCell(header)
            }
            documento.add(tituloTablaDatos)

            val tablaDatos = PdfPTable(2)

            for (item in Datosfinca) {
                Stream.of(
                    "Propietario",
                    item.propietario,
                    "Empresa",
                    item.nombre
                ).forEach {
                    val header = PdfPCell()
                    val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
                    header.phrase = Phrase(it, headerFont)
                    tablaDatos.addCell(header)
                }
                documento.add(tablaDatos)
            }

            val space = Paragraph(
                "\n"
            )

            documento.add(space)
            documento.add(space)

            if (totalInsumos[0] != null){
                val tituloTablaInsumos = PdfPTable(1)
                Stream.of("Insumos").forEach {
                    val header = PdfPCell()
                    val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.WHITE)
                    header.backgroundColor = BaseColor.BLUE
                    header.horizontalAlignment = Element.ALIGN_CENTER
                    header.borderWidth = 2f
                    header.phrase = Phrase(it, headerFont)
                    tituloTablaInsumos.addCell(header)
                }
                documento.add(tituloTablaInsumos)

                val tablaInsumos = PdfPTable(3)
                Stream.of(
                    "DESCRIPCION",
                    "FECHA",
                    "COSTO"
                ).forEach {
                    val header = PdfPCell()
                    val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
                    header.backgroundColor = BaseColor.LIGHT_GRAY
                    header.phrase = Phrase(it, headerFont)
                    tablaInsumos.addCell(header)
                }

                for (item in insumos) {
                    tablaInsumos.addCell(item.descripcion)
                    tablaInsumos.addCell(item.fecha)
                    Price().priceSplit(
                        item.costo.toString().toInt(),
                    )
                    { tablaInsumos.addCell(it) }
                }
                documento.add(tablaInsumos)

                val totalTablaInsumos = PdfPTable(2)
                Price().priceSplit(totalInsumos[0].toString().toInt()) { itPrice ->
                    Stream.of(
                        "total",
                        itPrice
                    ).forEach {
                        val header = PdfPCell()
                        val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
                        header.backgroundColor = BaseColor.GREEN
                        header.phrase = Phrase(it, headerFont)
                        totalTablaInsumos.addCell(header)
                    }
                    documento.add(totalTablaInsumos)
                }
            }

            documento.add(space)


            if (totalTrabajos[0] != null){
                val tituloTablaTrabajos = PdfPTable(1)
                Stream.of("Tabajos").forEach {
                    val header = PdfPCell()
                    val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.WHITE)
                    header.backgroundColor = BaseColor.BLUE
                    header.horizontalAlignment = Element.ALIGN_CENTER
                    header.borderWidth = 2f
                    header.phrase = Phrase(it, headerFont)
                    tituloTablaTrabajos.addCell(header)
                }
                documento.add(space)

                documento.add(tituloTablaTrabajos)

                val tablaTrabajos = PdfPTable(3)
                Stream.of(
                    "DESCRIPCION",
                    "FECHA",
                    "COSTO"
                ).forEach {
                    val header = PdfPCell()
                    val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
                    header.backgroundColor = BaseColor.LIGHT_GRAY
                    header.phrase = Phrase(it, headerFont)
                    tablaTrabajos.addCell(header)
                }

                for (item in trabajos) {
                    tablaTrabajos.addCell(item.descripcion)
                    tablaTrabajos.addCell(item.fecha)
                    Price().priceSplit(
                        item.costo.toString().toInt(),
                    )
                    { tablaTrabajos.addCell(it) }
                }
                documento.add(tablaTrabajos)

                val totalTablaTrabajos = PdfPTable(2)
                Price().priceSplit(totalTrabajos[0].toString().toInt()) { itPrice ->
                    Stream.of(
                        "total",
                        itPrice
                    ).forEach {
                        val header = PdfPCell()
                        val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
                        header.backgroundColor = BaseColor.GREEN
                        header.phrase = Phrase(it, headerFont)
                        totalTablaTrabajos.addCell(header)
                    }
                    documento.add(totalTablaTrabajos)
                }
            }

            if (totalOtros[0] != null){

                val tituloTablaOtros = PdfPTable(1)
                Stream.of("Otros").forEach {
                    val header = PdfPCell()
                    val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.WHITE)
                    header.backgroundColor = BaseColor.BLUE
                    header.horizontalAlignment = Element.ALIGN_CENTER
                    header.borderWidth = 2f
                    header.phrase = Phrase(it, headerFont)
                    tituloTablaOtros.addCell(header)
                }
                documento.add(space)

                documento.add(tituloTablaOtros)

                val tablaOtros = PdfPTable(3)
                Stream.of(
                    "DESCRIPCION",
                    "FECHA",
                    "COSTO"
                ).forEach {
                    val header = PdfPCell()
                    val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
                    header.backgroundColor = BaseColor.LIGHT_GRAY
                    header.phrase = Phrase(it, headerFont)
                    tablaOtros.addCell(header)
                }

                for (item in otros) {
                    tablaOtros.addCell(item.descripcion)
                    tablaOtros.addCell(item.fecha)
                    Price().priceSplit(
                        item.costo.toString().toInt(),
                    )
                    { tablaOtros.addCell(it) }
                }
                documento.add(tablaOtros)

                val totalTablaOtros = PdfPTable(2)
                Price().priceSplit(totalOtros[0].toString().toInt()) { itPrice ->
                    Stream.of(
                        "total",
                        itPrice
                    ).forEach {
                        val header = PdfPCell()
                        val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
                        header.backgroundColor = BaseColor.GREEN
                        header.phrase = Phrase(it, headerFont)
                        totalTablaOtros.addCell(header)
                    }
                    documento.add(totalTablaOtros)
                }
            }

            documento.add(space)

            if (totalEgresos[0] != null) {

                val totalTablaEgresos = PdfPTable(2)

                documento.add(space)


                Price().priceSplit(totalEgresos[0].toString().toInt()) { itPrice ->
                    Stream.of(
                        "total egresos",
                        itPrice
                    ).forEach {
                        val header = PdfPCell()
                        val headerFont =
                            FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
                        header.backgroundColor = BaseColor.GREEN
                        header.phrase = Phrase(it, headerFont)
                        totalTablaEgresos.addCell(header)
                    }
                    documento.add(totalTablaEgresos)
                }
            }

            documento.add(space)
            documento.add(space)

            //ingresos

            documento.add(space)

            if (totalIngresos[0] != null) {

                val tituloTablaIngresos = PdfPTable(1)
                Stream.of("Ingresos").forEach {
                    val header = PdfPCell()
                    val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.WHITE)
                    header.backgroundColor = BaseColor.BLUE
                    header.horizontalAlignment = Element.ALIGN_CENTER
                    header.borderWidth = 2f
                    header.phrase = Phrase(it, headerFont)
                    tituloTablaIngresos.addCell(header)
                }
                documento.add(tituloTablaIngresos)

                val tablaIngresos = PdfPTable(4)
                Stream.of(
                    "DESCRIPCION",
                    "FECHA",
                    "TIPO",
                    "PRECIO"
                ).forEach {
                    val header = PdfPCell()
                    val headerFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
                    header.backgroundColor = BaseColor.LIGHT_GRAY
                    header.phrase = Phrase(it, headerFont)
                    tablaIngresos.addCell(header)
                }

                for (item in ingresos) {
                    tablaIngresos.addCell(item.descripcion)
                    tablaIngresos.addCell(item.fecha)
                    tablaIngresos.addCell(item.producto)
                    Price().priceSplit(
                        item.precio.toString().toInt(),
                    )
                    { tablaIngresos.addCell(it) }
                }
                documento.add(tablaIngresos)

                val totalTablaIngresos = PdfPTable(2)
                Price().priceSplit(totalIngresos[0].toString().toInt()) { itPrice ->
                    Stream.of(
                        "total ingresos",
                        itPrice
                    ).forEach {
                        val header = PdfPCell()
                        val headerFont =
                            FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
                        header.backgroundColor = BaseColor.GREEN
                        header.phrase = Phrase(it, headerFont)
                        totalTablaIngresos.addCell(header)
                    }
                    documento.add(totalTablaIngresos)
                }

                documento.add(space)
                documento.add(space)
            }
            //ingreso libre

            documento.add(space)

            if (totalEgresos[0] != null && totalIngresos[0] != null){
                val tablaIngresoLibre = PdfPTable(1)
                val ingresoLibre = totalIngresos[0] - totalEgresos[0]
                Price().priceSplit(ingresoLibre) { itPrice ->
                    Stream.of(
                        "total ingreso libre",
                        itPrice
                    ).forEach {
                        val header = PdfPCell()
                        val headerFont = FontFactory.getFont("arial", 20f, Font.BOLD, BaseColor.BLACK)
                        header.backgroundColor = BaseColor.GREEN
                        header.phrase = Phrase(it, headerFont)
                        header.horizontalAlignment = Element.ALIGN_CENTER
                        tablaIngresoLibre.addCell(header)
                    }
                    documento.add(tablaIngresoLibre)
                }
            }else{
                val error = Paragraph(
                    ":( DATOS INSUFICIENTES ):",
                    FontFactory.getFont("arial", 25f, Font.BOLD, BaseColor.RED)
                )
                error.alignment = Element.ALIGN_CENTER

                documento.add(error)
            }

            documento.add(space)

            val frace = Paragraph(
                "La innovación distingue a los líderes de los seguidores" + "\n" + "Steve Jobs" + "\n\n\n",
                FontFactory.getFont("arial", 15f, Font.BOLD, BaseColor.LIGHT_GRAY)
            )
            frace.alignment = Element.ALIGN_CENTER

            documento.add(frace)

            documento.close()

            location("$folder/$fileName")

        }
    }

}