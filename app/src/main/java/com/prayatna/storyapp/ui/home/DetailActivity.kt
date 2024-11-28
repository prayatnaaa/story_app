package com.prayatna.storyapp.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.prayatna.storyapp.databinding.ActivityDetailBinding
import com.prayatna.storyapp.helper.Result
import com.prayatna.storyapp.ui.UserViewModelFactory
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
                        showLoading(false)
                        showToast(result.error)
                    }

                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showLoading(false)
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}