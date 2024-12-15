package com.prayatna.storyapp.ui.user.home

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.prayatna.storyapp.databinding.ActivityDetailBinding
import com.prayatna.storyapp.helper.Result
import com.prayatna.storyapp.ui.UserViewModelFactory
import com.prayatna.storyapp.ui.user.UserViewModel
import jp.wasabeef.glide.transformations.BlurTransformation

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()


    }

    private fun setupAction() {
        val id = intent.getStringExtra(EXTRA_ID)
        if (id != null) {
            viewModel.getDetailStoryById(id).observe(this) { result ->
                when (result) {
                    is Result.Error -> {
                        Log.e("DetailActivity", result.error)
                    }

                    is Result.Loading -> {
                        Log.d("DetailActivity", "Loading")
                    }

                    is Result.Success -> {
                        val data = result.data.story!!
                        binding.tvTitle.text = data.name
                        binding.tvDescription.text = data.description
                        Glide.with(binding.storyImage.context).load(data.photoUrl)
                            .into(binding.storyImage)
                        Glide.with(binding.blurredBackground.context).load(data.photoUrl)
                            .transform(BlurTransformation(25, 25))
                            .into(binding.blurredBackground)
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}