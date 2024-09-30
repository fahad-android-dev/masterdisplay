package com.orbits.masterdisplay.helper

import android.os.Environment
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileConfig {

    var image_FileNames: Array<String?>? = null
    var image_FilePaths: Array<String?>? = null
    var video_FileNames: Array<String?>? = null
    var video_FilePaths: Array<String?>? = null

    fun createExcelFile(filePath: String) {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Sheet 1")

        // Example of setting data
        val rowOne = sheet.createRow(0)
        rowOne.createCell(0).setCellValue(Constants.SCROLL_TEXT)
        rowOne.createCell(1).setCellValue("Put your scrolling text in excel file value. Have a great day!!!")

        // Example of setting data
        val rowTwo = sheet.createRow(2)
        rowTwo.createCell(0).setCellValue(Constants.SCROLL_TEXT_COLOR)
        rowTwo.createCell(1).setCellValue("#F7B5CA")

        val rowThree = sheet.createRow(4)
        rowThree.createCell(0).setCellValue(Constants.SERVICES)
        rowThree.createCell(1).setCellValue("1")

        // Write the output to the file
        FileOutputStream(filePath).use { outputStream ->
            workbook.write(outputStream)
        }

        workbook.close()
    }

    fun readExcelFile(filePath: String): Map<String, String> {
        val colors = mutableMapOf<String, String>()

        FileInputStream(filePath).use { inputStream ->
            val workbook: Workbook = WorkbookFactory.create(inputStream)
            val sheet: Sheet = workbook.getSheetAt(0)

            // Use iterator to ensure all rows are handled, even if they have no physical data
            for (row in sheet) {
                // Ensure the row has at least two cells
                if (row.lastCellNum >= 2) {
                    val keyCell = row.getCell(0)
                    val valueCell = row.getCell(1)

                    // Check if both cells are not null and have string values
                    if (keyCell != null && valueCell != null) {
                        val key = keyCell.stringCellValue
                        val value = valueCell.stringCellValue
                        colors[key] = value
                    }
                }
            }

            workbook.close()
        }

        return colors
    }

    fun readImageFile(fileName:String) {
        try {
            val listFile: Array<File>
            image_FileNames = null
            image_FilePaths = null

            val file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + fileName
            )
            if (file.isDirectory) {
                listFile = file.listFiles()
                // List file path for Images
                image_FilePaths = arrayOfNulls(listFile.size)
                // List file path for Image file names
                image_FileNames = arrayOfNulls(listFile.size)

                for (i in listFile.indices) {
                    // Get the path
                    image_FilePaths!![i] = listFile[i].absolutePath
                    // Get the name
                    image_FileNames!![i] = listFile[i].name
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun readVideoFile() {
        try {
            val listFile: Array<File>
            video_FileNames = null
            video_FilePaths = null
            val vidDir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/MasterDisplay_Config/Advertise videos"
            )
            if (vidDir.isDirectory) {
                val vidFiles = vidDir.list()
                if (vidFiles?.size != 0) {
                    listFile = vidDir.listFiles() as Array<File>
                    // List file path for videos
                    video_FilePaths = arrayOfNulls(listFile.size)
                    // List file path for video file names
                    video_FileNames = arrayOfNulls(listFile.size)

                    for (i in listFile.indices) {
                        // Get the path
                        video_FilePaths!![i] = listFile[i].absolutePath
                        // Get the name
                        video_FileNames!![i] = listFile[i].name
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}