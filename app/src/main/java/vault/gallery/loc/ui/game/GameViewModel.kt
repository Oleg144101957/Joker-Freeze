package vault.gallery.loc.ui.game

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import vault.gallery.loc.R
import java.util.*
import kotlin.collections.ArrayList

class GameViewModel : ViewModel() {

    val count = MutableSharedFlow<Int>()

    val images = arrayListOf<Images>(
        Images(id = 1, res = R.drawable.bt),
        Images(id = 2, res = R.drawable.vr),
        Images(id = 3, res = R.drawable.ce),
        Images(id = 4, res = R.drawable.xw),
        Images(id = 5, res = R.drawable.zq),
        Images(id = 6, res = R.drawable.ny),
    )

    fun spinSlot(): ArrayList<Int> {
        val random = Random()
        val image1Index = random.nextInt(images.size)
        val image2Index = random.nextInt(images.size)
        val image3Index = random.nextInt(images.size)

        return arrayListOf(image1Index, image2Index, image3Index)
    }
}

data class Images(
    val id: Int,
    val res: Int
)