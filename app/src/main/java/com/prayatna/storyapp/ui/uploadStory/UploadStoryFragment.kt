package com.prayatna.storyapp.ui.uploadStory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.prayatna.storyapp.R

class UploadStoryFragment : Fragment() {

    companion object {
        fun newInstance() = UploadStoryFragment()
    }

    private val viewModel: UploadStoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_upload_story, container, false)
    }
}