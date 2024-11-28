package com.prayatna.storyapp.ui.settings

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.prayatna.storyapp.R
import com.prayatna.storyapp.databinding.FragmentSettingsBinding
import com.prayatna.storyapp.ui.AuthViewModelFactory

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels {
        AuthViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupUser()
        setupView()
    }

    private fun setupView() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.settings)
    }

    private fun setupUser() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.tvUserName.text = user.userName
            binding.tvEmail.text = user.userId
        }
    }

    private fun setupAction() {
        binding.btnLogout.setOnClickListener {
            logout()
        }
        binding.settingsLocale.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun logout() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.logout))
            setMessage(getString(R.string.confirmation_message))
        }.setPositiveButton(getString(R.string.yes)) { _, _ ->
            viewModel.logout()
        }.setNegativeButton(getString(R.string.no)) { _, _ -> }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}