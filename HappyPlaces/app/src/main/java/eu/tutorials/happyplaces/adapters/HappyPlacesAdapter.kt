package eu.tutorials.happyplaces.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import eu.tutorials.happyplaces.activities.AddHappyPlaceActivity
import eu.tutorials.happyplaces.database.DatabaseHandler
import eu.tutorials.happyplaces.databinding.ItemHappyPlaceBinding
import eu.tutorials.happyplaces.models.HappyPlaceModel

class HappyPlacesAdapter(
  private val context: Context,
  private var list: ArrayList<HappyPlaceModel>
) : RecyclerView.Adapter<HappyPlacesAdapter.MyViewHolder>() {

  private var onClickListener: OnClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
    val binding = ItemHappyPlaceBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )
    return MyViewHolder(binding)
  }

  fun setOnClickListener(onClickListener: OnClickListener) {
    this.onClickListener = onClickListener
  }

  override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    val model = list[position]
    holder.bind(model, position)
  }

  fun notifyEditItem(position: Int, updateHappyPlaces: ActivityResultLauncher<Intent>){
    val intent = Intent(context, AddHappyPlaceActivity::class.java)
    intent.putExtra("happyPlace", list[position])

    updateHappyPlaces.launch(intent)

    notifyItemChanged(position)
  }

  fun removeAt(position: Int) {
    val dbHandler = DatabaseHandler(context)
    val isDelete = dbHandler.deleteHappyPlace(list[position])
    if (isDelete > 0){
      list.removeAt(position)
      notifyItemRemoved(position)
    }
  }

  override fun getItemCount(): Int {
    return list.size
  }

  interface OnClickListener {
    fun onClick(position: Int, model: HappyPlaceModel)
  }

  inner class MyViewHolder(private val binding: ItemHappyPlaceBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: HappyPlaceModel, position: Int) {
      binding.ivPlaceImage.setImageURI(Uri.parse(model.image))
      binding.tvTitle.text = model.title
      binding.tvDescription.text = model.description

      binding.llHappyPlace.setOnClickListener {
        if (onClickListener != null){
          onClickListener!!.onClick(position, model)
        }
      }
    }
  }
}

