@file:Suppress("DEPRECATION")

package com.prayatna.storyapp.ui.user.story

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.prayatna.storyapp.R
import com.prayatna.storyapp.databinding.ActivityStoryUploadBinding
import com.prayatna.storyapp.helper.Result
import com.prayatna.storyapp.helper.reduceFileImage
import com.prayatna.storyapp.helper.uriToFile
import com.prayatna.storyapp.ui.UserViewModelFactory
import com.prayatna.storyapp.ui.user.UserViewModel
import com.prayatna.storyapp.ui.user.story.CameraActivity.Companion.CAMERAX_RESULT
import jp.wasabeef.glide.transformations.BlurTransformation

class StoryUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryUploadBinding
    private var userLatitude: Double? = null
    private var userLongitude: Double? = null

    private val viewModel by viewModels<UserViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    private val requestPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermission.launch(REQUIRED_PERMISSION)
        }

        setupAction()
        setupUI()
        setupViewModel()
        setupGalleryFromCameraResult()
    }

    private fun setupGalleryFromCameraResult() {
        intent.getStringExtra(CameraActivity.RESULT_FROM_CAMERA_GALLERY)?.let { uri ->
            viewModel.currentImageUri = uri.toUri()
            showImage()
        }
    }

    private fun setupViewModel() {
        viewModel.story.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                        Log.e("okhttp", "switch: ${result.error}")
                    }

                    is Result.Success -> {
                        showLoading(false)
                        showToast(result.data.message!!)
                        finish()
                    }
                }
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupAction() {
        binding.btnUpload.setOnClickListener { upload() }
        binding.openCamera.setOnClickListener { openCamera() }
        binding.openGallery.setOnClickListener { openGallery() }
        binding.btnSwitch.setOnCheckedChangeListener { _, isChecked ->
            setupSwitch(isChecked)
            Log.d("okhttp", "onSwitch: $userLatitude and $userLongitude")
        }
    }

    private fun setupSwitch(isChecked: Boolean) {
        if (isChecked) {
            setLocation()
        } else {
            userLatitude = null
            userLongitude = null
        }
    }

    private fun setLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(
                this,
                "Location permission is required to fetch your location.",
                Toast.LENGTH_SHORT
            ).show()
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fetchLocation()
        } else {
            Toast.makeText(
                this,
                "Location permission is required to fetch your location.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            showImage()
        }
    }

    private fun showImage() {
        val currentImageUri = viewModel.currentImageUri
        if (currentImageUri != null) {
            Glide.with(binding.cameraResultImage.context).load(currentImageUri)
                .into(binding.cameraResultImage)
            Glide.with(binding.cameraResultImage.context).load(currentImageUri)
                .transform(BlurTransformation(25, 25)).into(binding.blurredBackground)
        } else {
            binding.cameraResultImage.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_gallery
                )
            )
        }
    }



    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        if (!isLocationEnabled()) {
            Toast.makeText(this, "Please enable location services.", Toast.LENGTH_SHORT).show()
            return
        }

        val fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : com.google.android.gms.location.LocationCallback() {
            @SuppressLint("StringFormatMatches")
            override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                locationResult.lastLocation?.let { location ->
                    userLatitude = location.latitude
                    userLongitude = location.longitude
                } ?: run {
                    Toast.makeText(this@StoryUploadActivity, "Unable to fetch location. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as android.location.LocationManager
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
    }

    private fun openCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherCamera.launch(intent)
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            viewModel.currentImageUri =
                it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        } else {
            setupGalleryFromCameraResult()
        }
    }

    private fun upload() {
        viewModel.currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.editTextDescription.text
            viewModel.addStory(imageFile, description.toString(), userLatitude!!.toFloat() , userLongitude!!.toFloat())
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}