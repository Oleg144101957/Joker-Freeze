package vault.gallery.loc.ui.loading

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import leagueofmonkeys.torqueburnou.data.Repo
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val repository: Repo,
    private val app: Application
) : AndroidViewModel(application = app) {

    val stringToSend = MutableStateFlow<String>("")
    private lateinit var referrerClient: InstallReferrerClient

    suspend fun createInstallReferrerRequest(): String? = suspendCoroutine { cont ->

        referrerClient = InstallReferrerClient.newBuilder(app).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {

                        val response: ReferrerDetails = referrerClient.installReferrer
                        Log.e("Ref", "${referrerClient.installReferrer}")
                        viewModelScope.launch {
                            stringToSend.emit(response.toString())
                            Log.e(
                                "InstallReferrerClient",
                                "referrerClient.installReferrer to string === $response "
                            )
                        }
                        cont.resume(response.toString())
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        Log.e(
                            "InstallReferrerClient",
                            "InstallReferrerResponse.FEATURE_NOT_SUPPORTED "
                        )
                        cont.resume(null)
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        Log.e(
                            "InstallReferrerClient",
                            "InstallReferrerResponse.SERVICE_UNAVAILABLE "
                        )
                        cont.resume(null)
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                Log.e(
                    "InstallReferrerClient",
                    "onInstallReferrerServiceDisconnected"
                )
                cont.resume(null)
            }
        })
    }

}