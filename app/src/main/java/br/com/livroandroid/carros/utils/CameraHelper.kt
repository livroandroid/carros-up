package br.com.livroandroid.carros.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class CameraHelper {
    companion object {
        private const val TAG = "camera"
    }

    // Arquivo pode ser nulo
    var file: File? = null
        private set

    // Se girou a tela recupera o estado
    fun init(icicle: Bundle?) {
        if (icicle != null) {
            file = icicle.getSerializable("file") as File
        }
    }

    // Salva o estado
    fun onSaveInstanceState(outState: Bundle) {
        if (file != null) {
            outState.putSerializable("file", file)
        }
    }

    // Intent para abrir a câmera
    fun open(context: Context, fileName: String): Intent {
        file = getSdCardFile(context, fileName)
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file?.apply {
            Log.d("camera", file.toString())
            val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", this)
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        return i
    }

    // Cria o arquivo da foto no diretório privado do app
    private fun getSdCardFile(context: Context, fileName: String): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ?: throw RuntimeException("Não foi possível criar a o arquivo da foto.")
        if (!dir.exists()) {
            dir.mkdir()
        }
        return File(dir, fileName)
    }

    // Lê o bitmap no tamanho desejado
    fun getBitmap(w: Int, h: Int): Bitmap? {
        file?.apply {
            if (exists()) {
                Log.d(TAG, absolutePath)
                // Resize
                val bitmap = ImageUtils.resize(this, w, h)
                Log.d(TAG, "getBitmap w/h: " + bitmap.width + "/" + bitmap.height)
                return bitmap
            }
        }
        return null
    }

    // Salva o bitmap reduzido no arquivo (para upload)
    fun save(bitmap: Bitmap) {
        file?.apply {
            val out = FileOutputStream(this)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            Log.d(TAG, "Foto salva: $absolutePath")
        }
    }
}
