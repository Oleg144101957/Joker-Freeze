package vault.gallery.loc.os

import android.app.Application
import com.onesignal.OneSignal

class OneSignalTouchWrapper(private val application: Application) {

     fun init(externalId: String) {

        OneSignal.setExternalUserId(externalId)
        OneSignal.setAppId("ed5ab42a-5598-4ac0-8b64-3ba201b6e6fd")
        OneSignal.initWithContext(application)
    }
}