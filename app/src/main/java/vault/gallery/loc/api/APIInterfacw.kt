package vault.gallery.loc.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface APIInterface {

    @Multipart
    @POST("/f4Eyo5QFPS5e.php")
     suspend fun referrer(@Part filePart: MultipartBody.Part): Response<String>

}

