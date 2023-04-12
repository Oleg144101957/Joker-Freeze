package vault.gallery.loc.ui.spl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import vault.gallery.loc.R
import vault.gallery.loc.ui.game.GameActiv
import vault.gallery.loc.ui.loading.LoadingActiv

class SplActiv : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        simpleWork()
    }

    private fun simpleWork() {

        val mReques: WorkRequest = OneTimeWorkRequestBuilder<MyWorkManager>().build()
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(mReques.id)
            .observe(this, Observer { info ->
                if (info != null && info.state.isFinished) {
                    val isNot = info.outputData.getBoolean(
                        MyWorkManager.KEY_RESULT,
                        false
                    )
                    if (isNot) {
                        Log.e("IF", "start Loading Activity")
                        val intet = Intent(this@SplActiv, LoadingActiv::class.java)
                        startActivity(intet)
                        finish()
                    } else {
                        Log.e("IF", "start Game Activity")
                        val intet = Intent(this@SplActiv, GameActiv::class.java)
                        startActivity(intet)
                        finish()
                    }
                }
            })
        WorkManager.getInstance(this).enqueue(mReques)
    }
}