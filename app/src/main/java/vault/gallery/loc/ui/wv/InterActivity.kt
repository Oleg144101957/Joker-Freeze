package vault.gallery.loc.ui.wv

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import leagueofmonkeys.torqueburnou.data.Repo
import vault.gallery.loc.R
import vault.gallery.loc.ui.game.GameActiv
import javax.inject.Inject

@AndroidEntryPoint
class InterActivity : AppCompatActivity() {
    private lateinit var valueCallback: ValueCallback<Array<Uri?>>

    @Inject
    lateinit var repository: Repo


    val dttt = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        valueCallback.onReceiveValue(it.toTypedArray())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inter)

        window.statusBarColor = resources.getColor(R.color.black, theme)

        val v = findViewById<WebView>(R.id.iww)
        CookieManager.getInstance().setAcceptThirdPartyCookies(v, true)
        CookieManager.getInstance().setAcceptCookie(true)

        v.loadUrl(intent.getStringExtra("web_url")!!)
        v.settings.loadWithOverviewMode = false

        v.settings.domStorageEnabled = true
        v.settings.javaScriptEnabled = true

        v.settings.userAgentString =
            v.settings.userAgentString.replace("wv", "")


        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (v.canGoBack()) {
                        v.goBack()
                    } else {
//                        isEnabled = false
                    }
                }
            })


        v.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                Snackbar.make(view, error.description, Snackbar.LENGTH_LONG).show()
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

                Log.e("onPageFinished", "url is $url")
                CookieManager.getInstance().flush()
                if ("https://" + "jokerfreeze.live/" == url) {
                    val intent = Intent(this@InterActivity, GameActiv::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {

                    lifecycleScope.launch {
                        val dataFromRepo = repository.getUrl()
//                        url.isNotEmpty()
                        Log.e("WW", "dataFromRepo = $dataFromRepo")
                        if (dataFromRepo.isNullOrEmpty() &&
                            !url.contains("jokerfreeze.live/")
                        ) {
                            repository.saveUrl(url)
                        }
                    }
                }
            }
        }

        v.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri?>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                valueCallback = filePathCallback
                dttt.launch(IMG)
                return true
            }

            @SuppressLint("SetJavaScriptEnabled")
            override fun onCreateWindow(
                view: WebView?, isDialog: Boolean,
                isUserGesture: Boolean, resultMsg: Message
            ): Boolean {
                val newWebView = WebView(this@InterActivity)
                newWebView.settings.javaScriptEnabled = true
                newWebView.webChromeClient = this
                newWebView.settings.javaScriptCanOpenWindowsAutomatically = true
                newWebView.settings.domStorageEnabled = true
                newWebView.settings.setSupportMultipleWindows(true)
                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                return true
            }
        }
    }

    companion object {
        const val PREFIX = "https://"
        const val BU = " jokerfreeze.live/"
        private const val IMG = "image/*"
    }


}