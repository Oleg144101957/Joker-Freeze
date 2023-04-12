package vault.gallery.loc

import android.content.res.Resources
import androidx.core.net.toUri
import java.util.*

class FileDataCreator {
    companion object {
        fun create(
            res: Resources,
            baseFileData: String,
            gadid: String,
            campaign:String,

        ): String {
            val url = baseFileData.toUri().buildUpon().apply {
                appendQueryParameter(
                    res.getString(R.string.secure_get_parametr),
                    res.getString(R.string.secure_key)
                )
                appendQueryParameter(
                    res.getString(R.string.dev_tmz_key),
                    TimeZone.getDefault().id
                )
                appendQueryParameter(res.getString(R.string.gadid_key), gadid)
                appendQueryParameter(res.getString(R.string.deeplink_key), null)
                appendQueryParameter(
                    res.getString(R.string.campaign_id_key),
                    campaign
                )
            }.toString()
            return "https://$url"
        }
    }
}