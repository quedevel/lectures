package eu.tutorials.happyplaces.activities

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.tutorials.happyplaces.databinding.ActivityHappyPlaceDetailBinding
import eu.tutorials.happyplaces.models.HappyPlaceModel

class HappyPlaceDetailActivity : AppCompatActivity() {

  private lateinit var binding: ActivityHappyPlaceDetailBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
    setContentView(binding.root)

    var happyPlaceModel: HappyPlaceModel? = null

    if (intent.hasExtra("happyPlace")){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        happyPlaceModel = intent.getParcelableExtra("happyPlace", HappyPlaceModel::class.java)
      }
    }

    if (happyPlaceModel != null){
      setSupportActionBar(binding.toolbarHappyPlaceDetail)
      supportActionBar!!.setDisplayHomeAsUpEnabled(true)
      supportActionBar!!.title = happyPlaceModel.title

      binding.toolbarHappyPlaceDetail.setNavigationOnClickListener {
        onBackPressed()
      }

      binding.ivPlaceImage.setImageURI(Uri.parse(happyPlaceModel.image))
      binding.tvDescription.text = happyPlaceModel.description
      binding.tvLocation.text = happyPlaceModel.location
    }

  }
}