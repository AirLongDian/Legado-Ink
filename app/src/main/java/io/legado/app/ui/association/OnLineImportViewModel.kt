package io.legado.app.ui.association

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.legado.app.R
import io.legado.app.base.BaseViewModel
import io.legado.app.data.appDb
import io.legado.app.data.entities.TxtTocRule
import io.legado.app.help.http.newCallResponseBody
import io.legado.app.help.http.okHttpClient
import io.legado.app.help.http.text
import io.legado.app.model.NoStackTraceException
import io.legado.app.utils.GSON
import io.legado.app.utils.fromJsonArray
import io.legado.app.utils.fromJsonObject
import io.legado.app.utils.isJsonArray
import okhttp3.MediaType

class OnLineImportViewModel(app: Application) : BaseViewModel(app) {
    val successLive = MutableLiveData<Pair<String, String>>()
    val errorLive = MutableLiveData<String>()

    fun getText(url: String, success: (text: String) -> Unit) {
        execute {
            okHttpClient.newCallResponseBody {
                url(url)
            }.text("utf-8")
        }.onSuccess {
            success.invoke(it)
        }.onError {
            errorLive.postValue(
                it.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }
    }

    fun getBytes(url: String, success: (bytes: ByteArray) -> Unit) {
        execute {
            @Suppress("BlockingMethodInNonBlockingContext")
            okHttpClient.newCallResponseBody {
                url(url)
            }.bytes()
        }.onSuccess {
            success.invoke(it)
        }.onError {
            errorLive.postValue(
                it.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }
    }

    fun importTextTocRule(json: String, finally: (title: String, msg: String) -> Unit) {
        execute {
            if (json.isJsonArray()) {
                GSON.fromJsonArray<TxtTocRule>(json)?.let {
                    appDb.txtTocRuleDao.insert(*it.toTypedArray())
                } ?: throw NoStackTraceException("格式不对")
            } else {
                GSON.fromJsonObject<TxtTocRule>(json)?.let {
                    appDb.txtTocRuleDao.insert(it)
                } ?: throw NoStackTraceException("格式不对")
            }
        }.onSuccess {
            finally.invoke(context.getString(R.string.success), "导入Txt规则成功")
        }.onError {
            finally.invoke(
                context.getString(R.string.error),
                it.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }
    }

    fun determineType(url: String, finally: (title: String, msg: String) -> Unit) {
        execute {
            val rs = okHttpClient.newCallResponseBody {
                url(url)
            }
            when (rs.contentType()) {
//                "application/zip".toMediaType(),
                MediaType.parse("application/zip"),
//                "application/octet-stream".toMediaType() -> {
                MediaType.parse("application/octet-stream") -> {
                    errorLive.postValue("墨水屏版不导入阅读预设")
                }
                else -> {
                    val json = rs.text("utf-8")
                    when {
                        json.contains("bookSourceUrl") ->
                            successLive.postValue(Pair("bookSource", json))
                        json.contains("sourceUrl") ->
                            successLive.postValue(Pair("rssSource", json))
                        json.contains("replacement") ->
                            successLive.postValue(Pair("replaceRule", json))
                        json.contains("name") && json.contains("rule") ->
                            importTextTocRule(json, finally)
                        json.contains("name") && json.contains("url") ->
                            importTextTocRule(json, finally)
                        else -> errorLive.postValue("格式不对")
                    }
                }
            }
        }
    }

}
