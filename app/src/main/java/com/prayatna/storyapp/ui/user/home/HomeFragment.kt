package com.prayatna.storyapp.ui.user.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.prayatna.storyapp.databinding.FragmentHomeBinding
import com.prayatna.storyapp.helper.Result
import com.prayatna.storyapp.ui.UserViewModelFactory
import com.prayatna.storyapp.ui.adapter.HomeAdapter
import com.prayatna.storyapp.ui.user.UserViewModel

class HomeFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels{
        UserViewModelFactory.getInstance(requireContext())
    }

    private var adapter: HomeAdapter? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupAction()
    }

    private fun setupAction() {
        viewModel.getStories("0").observe(viewLifecycleOwner) { stories ->
            if (stories != null) {
                when (stories) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        Log.d("HomeFragment", "Loading")
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.e("HomeFragment", stories.error)
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val data = stories.data.listStory
                        Log.d("HomeFragment", data.toString())
                        adapter?.submitList(data)
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = HomeAdapter()
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}