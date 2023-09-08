package eu.tutorials.happyplaces.activities

import android.util.Log
import android.os.Bundle
import android.view.View
import android.content.Intent
import com.infinum.dbinspector.DbInspector
import eu.tutorials.happyplaces.models.HappyPlaceModel
import eu.tutorials.happyplaces.database.DatabaseHandler
import eu.tutorials.happyplaces.utils.SwipeToEditCallback
import eu.tutorials.happyplaces.utils.SwipeToDeleteCallback
import eu.tutorials.happyplaces.adapters.HappyPlacesAdapter
import eu.tutorials.happyplaces.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val fabAddHappyPlace: FloatingActionButton = binding.fabAddHappyPlace
    val dbInspector: FloatingActionButton = binding.dbInspector

    dbInspector.setOnClickListener {
      DbInspector.show()
    }

    fabAddHappyPlace.setOnClickListener {
      val intent = Intent(this, AddHappyPlaceActivity::class.java)
      updateHappyPlaces.launch(intent)
    }

    getHappyPlacesFromLocalDB()
  }

  private val updateHappyPlaces =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      Log.e("updateHappyPlaces", result.resultCode.toString())
      when (result.resultCode) {
        RESULT_OK, RESULT_CANCELED -> {
          getHappyPlacesFromLocalDB()
        }
      }
    }

  private fun setupHappyPlacesRecyclerView(happyPlaces: ArrayList<HappyPlaceModel>) {
    binding.rvHappyPlacesList.layoutManager = LinearLayoutManager(this)
    binding.rvHappyPlacesList.setHasFixedSize(true)

    val placesAdapter = HappyPlacesAdapter(this, happyPlaces)
    binding.rvHappyPlacesList.adapter = placesAdapter

    placesAdapter.setOnClickListener(object : HappyPlacesAdapter.OnClickListener{
      override fun onClick(position: Int, model: HappyPlaceModel) {
        val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
        intent.putExtra("happyPlace", model)
        startActivity(intent)
      }
    })

    val editSwipeHandler = object: SwipeToEditCallback(this){
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val adapter = binding.rvHappyPlacesList.adapter as HappyPlacesAdapter
        adapter.notifyEditItem(viewHolder.adapterPosition, updateHappyPlaces)
      }
    }

    val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
    editItemTouchHelper.attachToRecyclerView(binding.rvHappyPlacesList)

    val deleteSwipeHandler = object: SwipeToDeleteCallback(this){
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val adapter = binding.rvHappyPlacesList.adapter as HappyPlacesAdapter
        adapter.removeAt(viewHolder.adapterPosition)
        getHappyPlacesFromLocalDB()
      }
    }

    val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
    deleteItemTouchHelper.attachToRecyclerView(binding.rvHappyPlacesList)
  }

  private fun getHappyPlacesFromLocalDB() {
    val dbHandler = DatabaseHandler(this)
    val happyPlaces: ArrayList<HappyPlaceModel> = dbHandler.getHappyPlaces()
    if (happyPlaces.size > 0) {
      binding.rvHappyPlacesList.visibility = View.VISIBLE
      binding.tvNoRecordsAvailable.visibility = View.GONE
      setupHappyPlacesRecyclerView(happyPlaces)
    } else {
      binding.rvHappyPlacesList.visibility = View.GONE
      binding.tvNoRecordsAvailable.visibility = View.VISIBLE
    }
  }
}