package com.prayatna.storyapp.ui.user.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.prayatna.storyapp.databinding.FragmentHomeBinding
import com.prayatna.storyapp.ui.UserViewModelFactory
import com.prayatna.storyapp.ui.adapter.HomeAdapter
import com.prayatna.storyapp.ui.adapter.LoadingStateAdapter
import com.prayatna.storyapp.ui.maps.MapsActivity
import com.prayatna.storyapp.ui.user.UserViewModel

class HomeFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels {
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

        setupAction()
        getData()
    }

    private fun getData() {
        adapter = HomeAdapter()
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.adapter = adapter?.withLoadStateFooter(
            footer = LoadingStateAdapter {
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        viewModel.stories.observe(viewLifecycleOwner) {
            adapter?.submitData(lifecycle, it)
        }
    }

    private fun setupAction() {
        mapsIntent()
    }

    private fun mapsIntent() {
        binding.btnMaps.setOnClickListener {
            val intent = Intent(requireActivity(), MapsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        adapter?.refresh()
    }
}