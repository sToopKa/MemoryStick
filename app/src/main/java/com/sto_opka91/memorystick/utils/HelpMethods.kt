package com.sto_opka91.memorystick.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import java.io.ByteArrayOutputStream
import java.util.Date
import java.util.Locale

object HelpMethods {

      fun getDateBool(date: String):Boolean{
          val dateString = date
          val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
          val dateDate = dateFormat.parse(dateString)
          val currentDate = Date()
          return dateDate.after(currentDate)
      }
    fun getCurrentDate(): String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDate = Date() // Получаем текущую дату
        return dateFormat.format(currentDate)
    }

    fun getByteArrayFromBitmap(bitmap: Bitmap?): ByteArray?{
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        outputStream.close()
        return byteArray

    }
    fun getBitmapFromByteArray(byteArray: ByteArray): Bitmap{
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
    fun compressBitmap(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        val aspectRatio: Float = width.toFloat() / height.toFloat()

        // Устанавливаем максимальные размеры
        val maxWidth = 800
        val maxHeight = 800

        // Уменьшаем размер изображения, если он превышает максимальные размеры
        if (width > maxWidth || height > maxHeight) {
            if (aspectRatio > 1) {
                width = maxWidth
                height = (width / aspectRatio).toInt()
            } else {
                height = maxHeight
                width = (height * aspectRatio).toInt()
            }
        }

        // Создаем новое изображение с измененными размерами
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

}