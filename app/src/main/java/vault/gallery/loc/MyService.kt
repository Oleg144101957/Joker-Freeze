package vault.gallery.loc

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.JsonObject
import com.ionspin.kotlin.crypto.LibsodiumInitializer
import com.ionspin.kotlin.crypto.secretbox.SecretBox
import com.ionspin.kotlin.crypto.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import vault.gallery.loc.api.APIInterface
import java.io.File


class MyService : Service() {

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("MyService", "onStartCommand")

        val referrerData = intent.getStringExtra("referrerData")
        val gadid = intent.getStringExtra("gadid")

        val fileName = "test.txt"
        val file = File(applicationContext.filesDir.toString(), fileName)
        file.deleteOnExit()
        file.createNewFile()

        var text = ""

        CoroutineScope(Dispatchers.IO).launch {

            LibsodiumInitializer.initialize()
            val test =
                "utm_source=apps.facebook.com&utm_campaign=fb4a&utm_content={\"app\": 1627114414375163,\"t\": 1676487969,\"source\": {\"data\": \"2f3a3a54c23f62f64a9784a0fb6931648aa792f296deb742d2332c260244a11d14a1823a32467defd0a824712f12329efd14d7179e12e0cb6dacc923649f191cfc3556d1c23df83f8d7c9516c9855190d669ce6eff08f533ce29eb8b7b25a4561d87aa2f6e7051c65c0bc004042cbb4f3ea159a5669c310f84d93517332965bbf2946aeddd02f299bd2e5d07ab4f8b00cef10c1457ae4d96f381c15b0cffcacd37b081025e3b0727fa233e0d161adf1d78158f2dd6605a87273f67dc11d527310b14257b81f77762ea6cc460681b3cff8f22d025e25d3f4ea80f68070d9c3b31664b0110ed7a0231f2c14b0236ccd56ffcf32e51e89fe8bdaf98a2e5730491c9646671c25e772e14738313832c32c84353d7d09e0b44fb0f184403e177e8ebeb1335d6ed10058a45877a2d626eee26c0a1aac305ae42b7eb34176f99e7d626be2ef6087a4e02edb48eb57cf06c0ee10caecfe14099b75ca9abf016cf0d0ae8e747621db4b409120264987f7ad9386f4fafdd055c89cbaa2f2b5c4ee2866cf50497d33fd8fd4213a55c1b25cc12a5d0aa45466a3e051417ed34b5b50df096ba0aaf8ff4db30b2ce5d5f25f0d6b104881ecd9792d92fe969800f7ab2d378c0ec5e7e9e5854ae3ecdc8eda26a679a0b727719d2b0c93a05\",\"nonce\": \"0f04e0702c63c49186660dba\"}}"
            val message = test.encodeToUByteArray()

            val key: UByteArray = LibsodiumRandom.buf(32)
            val nonce: UByteArray = LibsodiumRandom.buf(24)
            val encrypted = SecretBox.easy(message!!, nonce, key)
            val fileValue =
                "${encrypted.toHexString()},${nonce.toHexString()},${key.toHexString()},$gadid"
            Log.e("Sevrice", "fileValue  = $fileValue")
            file.writeText(fileValue)

            val client = provideHttpClient(file)
            val api = initApiService(client)
            val requestBody = MultipartBody.Part.createFormData(
                "aswq",
                "test.txt",
                file.asRequestBody("text/plain".toMediaTypeOrNull())
            )

            val response = api.referrer(requestBody)
            Log.e("Resp", "response = ${response.body()}")
            LibsodiumUtil.fromHex(response.body()!!)
            val q = LibsodiumUtil.fromHex(response.body()!!)
            val d: UByteArray = SecretBox.openEasy(q, nonce, key)

            val input = """{"campaign":"\u043a\u0430\u043c\u043f\u0430\u043d\u0456\u044f - Copy"}"""

//            var str: String = input.split(" ").get(0)
//            str = str.replace("\\", "")
//            val arr = str.split("u".toRegex()).toTypedArray()
//            for (i in 1 until arr.size) {
//                val hexVal = arr[i].toInt(16)
//                text += hexVal.toChar()
//            }
            val value = String(d.toByteArray(), Charsets.UTF_8)
            val jsonObject = JSONObject(value)
            val result = jsonObject.get("campaign")


            Log.e("qweqeqweqe", "output = $value")
            Log.e("qweqeqweqe", "campaign = ${result.toString()}")
            sendMessageToActivity(result.toString())

        }
        return START_NOT_STICKY
    }

    private fun provideHttpClient(file: File): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)


        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val requestBuilder: Request.Builder = chain.request().newBuilder()
                requestBuilder.header("Content-Type", "multipart/form-data")
                chain.proceed(requestBuilder.build())
            })
            .addInterceptor(interceptor)
            .build()
    }

    private fun initApiService(client: OkHttpClient): APIInterface =
        Retrofit.Builder()
            .baseUrl("https://jokerfreeze.live")
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(APIInterface::class.java)


    private fun sendMessageToActivity(msg: String) {
        val intent = Intent("GPSLocationUpdates")
        intent.putExtra("Status", msg)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

}