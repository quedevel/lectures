package eu.tutorials.happyplaces.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import eu.tutorials.happyplaces.BuildConfig
import eu.tutorials.happyplaces.R
import eu.tutorials.happyplaces.database.DatabaseHandler
import eu.tutorials.happyplaces.databinding.ActivityAddHappyPlaceBinding
import eu.tutorials.happyplaces.models.HappyPlaceModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID


class AddHappyPlaceActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAddHappyPlaceBinding

  private var cal = Calendar.getInstance()
  private lateinit var dateSetListener: OnDateSetListener

  private var path: Uri? = null
  private var mLatitude: Double = 0.0
  private var mLongitude: Double = 0.0

  private var mHappyPlaceDetails: HappyPlaceModel? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
    setContentView(binding.root)


    setSupportActionBar(binding.toolbarAddPlace)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    binding.toolbarAddPlace.setNavigationOnClickListener {
      onBackPressed()
    }

    if (!Places.isInitialized()) {
      Places.initialize(this@AddHappyPlaceActivity, BuildConfig.GOOGLE_MAPS_API_KEY)
    }

    if (intent.hasExtra("happyPlace")) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        mHappyPlaceDetails = intent.getParcelableExtra("happyPlace", HappyPlaceModel::class.java)
      }
    }

    dateSetListener = OnDateSetListener { view, year, month, dayOfMonth ->
      run {
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateDateInView("dd.MM.yyyy")
      }
    }

    updateDateInView("dd.MM.yyyy")

    if (mHappyPlaceDetails != null) {
      supportActionBar?.title = "Edit Happy Place"

      binding.etTitle.setText(mHappyPlaceDetails!!.title)
      binding.etDescription.setText(mHappyPlaceDetails!!.description)
      binding.etDate.setText(mHappyPlaceDetails!!.date)
      binding.etLocation.setText(mHappyPlaceDetails!!.location)
      mLatitude = mHappyPlaceDetails!!.latitude
      mLongitude = mHappyPlaceDetails!!.longitude

      path = Uri.parse(mHappyPlaceDetails!!.image)

      binding.ivPlaceImage.setImageURI(path)
      binding.btnSave.text = "UPDATE"
    }

    binding.etDate.setOnClickListener { onClickProcess(it) }
    binding.tvAddImage.setOnClickListener { onClickProcess(it) }
    binding.btnSave.setOnClickListener { onClickProcess(it) }
    binding.etLocation.setOnClickListener { onClickProcess(it) }
  }

  private fun onClickProcess(v: View) {
    when (v.id) {
      R.id.et_date -> showDatePickerDialog(this)
      R.id.tv_add_image -> {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from Gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { _, which ->
          when (which) {
            0 -> choosePhotoFromGallery()
            1 -> takePhotoFromCamera()
          }
        }.show()
      }
      R.id.btn_save -> {
        when {
          binding.etTitle.text.isNullOrEmpty() -> {
            Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show()
          }

          binding.etDescription.text.isNullOrEmpty() -> {
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show()
          }

          binding.etDate.text.isNullOrEmpty() -> {
            Toast.makeText(this, "Please enter date", Toast.LENGTH_SHORT).show()
          }

          path == null -> {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
          }

          else -> {
            val happyPlaceModel = HappyPlaceModel(
              if (mHappyPlaceDetails == null) 0 else mHappyPlaceDetails!!.id,
              binding.etTitle.text.toString(),
              path.toString(),
              binding.etDescription.text.toString(),
              binding.etDate.text.toString(),
              binding.etLocation.text.toString(),
              mLatitude,
              mLongitude
            )
            val dbHandler = DatabaseHandler(this)

            if (mHappyPlaceDetails == null) {
              val addHappyPlace = dbHandler.addHappyPlace(happyPlaceModel)
              if (addHappyPlace > 0) {
                Toast.makeText(this, "The happy place details are inserted successfully!", Toast.LENGTH_LONG).show()
                finish()
              }
            } else {
              val updateHappyPlace = dbHandler.updateHappyPlace(happyPlaceModel)
              if (updateHappyPlace > 0) {
                finish()
              }
            }

          }
        }
      }
      R.id.et_location -> {
        try {
          val fields = listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS
          )

          val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this@AddHappyPlaceActivity)

          startAutocomplete.launch(intent)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
    }
  }

  private val startAutocomplete =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
      if (result.resultCode == Activity.RESULT_OK) {
        val intent = result.data
        if (intent != null) {
          val place = Autocomplete.getPlaceFromIntent(intent)
          Log.i(
            "google map", "Place: ${place.name}, ${place.id}"
          )
        }
      } else if (result.resultCode == Activity.RESULT_CANCELED) {
        // The user canceled the operation.
        Log.i("google map", "User canceled autocomplete")
      }
    }

  private val cameraForResult =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      when (result.resultCode) {
        RESULT_OK -> {
          val thumbnail = result.data?.extras?.get("data") as Bitmap
          path = saveImageToInternalStorage(thumbnail)
          binding.ivPlaceImage.setImageBitmap(thumbnail)
        }
      }
    }

  private fun takePhotoFromCamera() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      Dexter.withContext(this)
        .withPermissions(
          Manifest.permission.POST_NOTIFICATIONS,
          Manifest.permission.READ_MEDIA_IMAGES,
          Manifest.permission.READ_MEDIA_VIDEO,
          Manifest.permission.READ_MEDIA_AUDIO,
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {
          override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraForResult.launch(cameraIntent)
          }

          override fun onPermissionRationaleShouldBeShown(
            permissions: List<PermissionRequest>,
            token: PermissionToken
          ) {
            showRationalDialogForPermissions()
          }
        }).onSameThread().check()
    }
  }

  private val galleryForResult =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      when (result.resultCode) {
        RESULT_OK -> {
          val contentURI = result.data?.data
          try {
            val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
            path = saveImageToInternalStorage(selectedImageBitmap)
            binding.ivPlaceImage.setImageBitmap(selectedImageBitmap)
          } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this@AddHappyPlaceActivity, "Failed..", Toast.LENGTH_LONG).show()
          }
        }
      }
    }

  private fun choosePhotoFromGallery() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      Dexter.withContext(this)
        .withPermissions(
          Manifest.permission.POST_NOTIFICATIONS,
          Manifest.permission.READ_MEDIA_IMAGES,
          Manifest.permission.READ_MEDIA_VIDEO,
          Manifest.permission.READ_MEDIA_AUDIO,
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {
          override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryForResult.launch(galleryIntent)
          }

          override fun onPermissionRationaleShouldBeShown(
            permissions: List<PermissionRequest>,
            token: PermissionToken
          ) {
            showRationalDialogForPermissions()
          }
        }).onSameThread().check()
    }
  }

  private fun showRationalDialogForPermissions() {
    AlertDialog.Builder(this)
      .setMessage("It looks like you have turned off permission required for this feature. It can be enabled under the Applications Settings")
      .setPositiveButton("GO TO SETTINGS") { _, _ ->
        try {
          val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
          val uri = Uri.fromParts("package", packageName, null)
          intent.data = uri
          startActivity(intent)
        } catch (e: ActivityNotFoundException) {
          e.printStackTrace()
        }
      }.setNegativeButton("CANCEL") { dialog, _ ->
        dialog.dismiss()
      }.show()
  }

  private fun showDatePickerDialog(context: Context) {
    DatePickerDialog(
      context,
      dateSetListener,
      cal.get(Calendar.YEAR),
      cal.get(Calendar.MONTH),
      cal.get(Calendar.DAY_OF_MONTH)
    ).show()
  }

  private fun updateDateInView(format: String) {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    binding.etDate.setText(sdf.format(cal.time).toString())
  }

  private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
    val wrapper = ContextWrapper(applicationContext)
    var file = wrapper.getDir("happyplace", Context.MODE_PRIVATE)
    file = File(file, "${UUID.randomUUID()}.jpg")
    FileOutputStream(file).use { stream ->
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
      stream.flush()
    }
    return Uri.parse(file.absolutePath)
  }
}