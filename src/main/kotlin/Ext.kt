package cn.csnbgsh.herbarium

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.Toast
import com.cylee.androidlib.base.BaseApplication
import com.cylee.lib.BuildConfig
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cylee on 16/4/2.
 */
inline fun <T> Activity.bind(id:Int):T {
    return this.findViewById(id) as T
}

inline  fun <T> View.bind(id:Int):T {
    return this.findViewById(id) as T
}

inline fun toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(EX.context, message, length).show()
}

inline fun Number.dp2px():Int {
    return Math.round(EX.context.resources.displayMetrics.density * this.toInt())
}

object EX {
    lateinit var context:Context
}


//测试包或者非release包将日志输出到文件中，方便查问题
fun Context.redirectLog() {
    if (BuildConfig.DEBUG) {
        val defHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, ex ->
            var osw: OutputStreamWriter? = null
            try {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd/HH:mm:ss", Locale.CHINA)
                val log = File(Environment.getExternalStorageDirectory(), "scan_crash.log")
                if (!log.exists()) {
                    log.createNewFile()
                }

                osw = OutputStreamWriter(FileOutputStream(log, false))
                var baos = ByteArrayOutputStream()
                val pw = PrintWriter(baos)
                pw.println("\n============================================================\n")
                pw.println(simpleDateFormat.format(Date()))
                ex.printStackTrace(pw)
                pw.flush()
                var content = baos.toString("utf-8")

                var clipboard = getSystemService(BaseApplication.CLIPBOARD_SERVICE) as ClipboardManager;
                var textCd = ClipData.newPlainText("crash_log",content)
                clipboard.setPrimaryClip(textCd);

                osw!!.write(content)
                osw.flush()
            } catch (e: Exception) {
            } finally {
                if (osw != null) {
                    try {
                        osw.close()
                    } catch (e: Exception) {
                    }

                }
            }
            defHandler?.uncaughtException(thread, ex)
        }
    }
}