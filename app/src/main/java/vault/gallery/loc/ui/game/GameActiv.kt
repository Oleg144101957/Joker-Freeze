package vault.gallery.loc.ui.game

import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vault.gallery.loc.R
import vault.gallery.loc.databinding.ActivityGameBinding
import kotlin.collections.indexOf as indexOf1

class GameActiv : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: ActivityGameBinding
    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSpin.setOnClickListener {
            lifecycleScope.launch {

                val array1 = viewModel.spinSlot()
                val array2 = viewModel.spinSlot()
                val array3 = viewModel.spinSlot()

                val animation: Animation =
                    AnimationUtils.loadAnimation(this@GameActiv, R.anim.slide_down)

                binding.image1.startAnimation(animation)
                binding.image1.setImageResource(viewModel.images[array1[0]].res)
                delay(500)

                val animation2: Animation =
                    AnimationUtils.loadAnimation(this@GameActiv, R.anim.slide_down)
                binding.image2.startAnimation(animation2)
                binding.image2.setImageResource(viewModel.images[array1[1]].res)
                delay(500)

                val animation3: Animation =
                    AnimationUtils.loadAnimation(this@GameActiv, R.anim.slide_down)
                binding.image3.startAnimation(animation3)
                binding.image3.setImageResource(viewModel.images[array1[2]].res)
                delay(500)

                val animation4: Animation =
                    AnimationUtils.loadAnimation(this@GameActiv, R.anim.slide_down)
                binding.image4.startAnimation(animation4)
                binding.image4.setImageResource(viewModel.images[array2[0]].res)
                delay(500)

                val animation5: Animation =
                    AnimationUtils.loadAnimation(this@GameActiv, R.anim.slide_down)
                binding.image5.startAnimation(animation5)
                binding.image5.setImageResource(viewModel.images[array2[1]].res)
                delay(500)

                val animation6: Animation =
                    AnimationUtils.loadAnimation(this@GameActiv, R.anim.slide_down)
                binding.image6.startAnimation(animation6)
                binding.image6.setImageResource(viewModel.images[array2[2]].res)
                delay(500)

                val animation7: Animation =
                    AnimationUtils.loadAnimation(this@GameActiv, R.anim.slide_down)
                binding.image7.startAnimation(animation7)
                binding.image7.setImageResource(viewModel.images[array3[0]].res)
                delay(500)

                val animation8: Animation =
                    AnimationUtils.loadAnimation(this@GameActiv, R.anim.slide_down)
                binding.image8.startAnimation(animation8)
                binding.image8.setImageResource(viewModel.images[array3[1]].res)
                delay(500)

                val animation9: Animation =
                    AnimationUtils.loadAnimation(this@GameActiv, R.anim.slide_down)
                binding.image9.startAnimation(animation9)
                binding.image9.setImageResource(viewModel.images[array3[2]].res)
                delay(500)

                checkWin(array1 = array1, array2 = array2, array3 = array3)
            }
        }
    }

    private fun checkWin(array1: ArrayList<Int>, array2: ArrayList<Int>, array3: ArrayList<Int>) {
        if (array1[0] == array1[1] || array1[1] == array1[2]) {
            count = count.plus(10)
            Log.e("Count", "Count = $count")
            binding.textCount.text = "Your credits: $count"
            Log.e("Win", "line 1 win")
        } else {
            binding.textCount.text = "Your credits: $count"
        }

        if (array2[0] == array2[1] || array2[1] == array2[2]) {
            count = count.plus(10)
            Log.e("Count", "Count = $count")
            binding.textCount.text = "Your credits: $count"
            Log.e("Win", "line 2 win")

        } else {
            binding.textCount.text = "Your credits: $count"
        }

        if (array3[0] == array3[1] || array3[1] == array3[2]) {
            count = count.plus(10)
            Log.e("Count", "Count = $count")
            binding.textCount.text = "Your credits: $count"
            Log.e("Win", "line 3 win")

        } else {
            binding.textCount.text = "Your credits: $count"
        }

        if (array1[0] == array2[1] || array2[1] == array3[2]) {
            count = count.plus(10)
            Log.e("Count", "Count = $count")
            binding.textCount.text = "Your credits: $count"
            Log.e("Win", "line 1.1 win")

        } else {
            binding.textCount.text = "Your credits: $count"
        }

        if (array1[2] == array2[1] || array2[1] == array3[0]) {
            count = count.plus(10)
            Log.e("Count", "Count = $count")
            binding.textCount.text = "Your credits: $count"
            Log.e("Win", "line 2.2 win")
        } else {
            binding.textCount.text = "Your credits: $count"

        }
    }
}