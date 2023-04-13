package vault.gallery.loc.os

import android.app.Application
import com.onesignal.OneSignal

class OneSignalTouchWrapper(private val application: Application) {

     fun init(externalId: String) {

        OneSignal.setExternalUserId(externalId)
        OneSignal.setAppId("33a5d832-be50-4383-8d86-3acd262ae741")
        OneSignal.initWithContext(application)
    }
}