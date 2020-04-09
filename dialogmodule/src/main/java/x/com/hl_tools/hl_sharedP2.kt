package x.com.hl_tools

import android.content.Context
import android.text.TextUtils
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.io.IOException

/**
 * @author luoqqsh
 * 需要限制最低安卓6.0
 */
object hl_sharedP2 {

    /**
     * 保存在手机里面的文件名
     */
    private const val FILE_NAME = "share_date"

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context 上下文
     * @param key     key
     * @param object  类型
     */
    fun setParamCommit(context: Context, key: String?, `object`: Any?): Boolean {
        val sp = EncryptedSharedPreferences.create(FILE_NAME, MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        val editor = sp.edit()
        try {
            editor.putString(key, hl_stream.objectToStream(`object`))
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return editor.commit()
    }

    fun setParam(context: Context, key: String?, `object`: Any?) {
        val sp = EncryptedSharedPreferences.create(FILE_NAME, MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        val editor = sp.edit()
        try {
            editor.putString(key, hl_stream.objectToStream(`object`))
            editor.apply()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * 注意：默认值和你返回的object类型要同一类型
     *
     * @param context       上下文
     * @param key           key
     * @param defaultObject 默认
     * @return object数据
     */
    fun <T> getParam(context: Context, key: String?, defaultObject: T): T {
        val sp = EncryptedSharedPreferences.create(FILE_NAME, MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        val o = sp.getString(key, "")
        return try {
            if (!TextUtils.isEmpty(o)) {
                hl_stream.streamToObject(o)
            } else {
                defaultObject
            }
        } catch (e: Exception) {
            e.printStackTrace()
            defaultObject
        }
    }

    fun clear(context: Context): Boolean {
        val sp = EncryptedSharedPreferences.create(FILE_NAME, MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        return sp.edit().clear().commit()
    }

    fun remove(context: Context, key: String?): Boolean {
        val sp = EncryptedSharedPreferences.create(FILE_NAME, MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        return if (TextUtils.isEmpty(key)) {
            false
        } else sp.edit().remove(key).commit()
    }
}