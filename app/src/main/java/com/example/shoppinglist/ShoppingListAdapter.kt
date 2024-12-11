package com.example.shoppinglist

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ShoppingListAdapter (
    private val items: MutableList<ShoppingItem>,
    private val context: Context
) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>() {

    private var addItemPlayer: MediaPlayer? = null
    private var deleteItemPlayer: MediaPlayer? = null
    private var itemBoughtPlayer: MediaPlayer? = null

    init {
        addItemPlayer = MediaPlayer.create(context, R.raw.drip)
        deleteItemPlayer = MediaPlayer.create(context, R.raw.zoom_out)
        itemBoughtPlayer = MediaPlayer.create(context, R.raw.zoom_in)
    }

    class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemStatus: TextView = itemView.findViewById(R.id.itemStatus)
        val itemLayout: LinearLayout = itemView.findViewById(R.id.item_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false)
        return ShoppingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val currentItem = items[position]
        holder.itemName.text = currentItem.name


        if (currentItem.isBought) {
            holder.itemStatus.text = holder.itemView.context.getString(R.string.item_status_bought)
            holder.itemStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.text_color_bought))
            holder.itemLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.item_background_color_selected))
        } else {
            holder.itemStatus.text = holder.itemView.context.getString(R.string.item_status_not_bought)
            holder.itemStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.text_color_not_bought))
            holder.itemLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.item_background_color_not_bought))
        }
        holder.itemView.setOnClickListener{
            currentItem.isBought = !currentItem.isBought
            notifyItemChanged(position)

            if (currentItem.isBought) {
                itemBoughtPlayer?.start()
                val message = context.getString(R.string.toast_items_marked_as_bought, currentItem.name)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            if (items.all { it.isBought }) {
                Toast.makeText(context, context.getString(R.string.toast_all_items_bought), Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: ShoppingItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)

        addItemPlayer?.start()
    }

    fun removeItem(position: Int) {
        val removedItem = items[position]
        items.removeAt(position)
        notifyItemRemoved(position)

        deleteItemPlayer?.start()

        val message = context.getString(R.string.toast_item_removed, removedItem.name)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        if (items.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.toast_no_more_items), Toast.LENGTH_SHORT).show()
        }
    }

    fun markAsBought(position: Int) {
        items[position].isBought = true
        notifyItemChanged(position)

        itemBoughtPlayer?.start()

        val message = context.getString(R.string.toast_items_marked_as_bought, items[position].name)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        if (items.all { it.isBought }) {
            Toast.makeText(context, context.getString(R.string.toast_all_items_bought), Toast.LENGTH_SHORT).show()
        }
    }

    fun releasePlayers() {
        addItemPlayer?.release()
        deleteItemPlayer?.release()
        itemBoughtPlayer?.release()
    }
}