package com.example.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class MainActivity : AppCompatActivity() {

    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private val shoppingList = mutableListOf<ShoppingItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Recycler
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        shoppingListAdapter = ShoppingListAdapter(shoppingList, this)
        recyclerView.adapter = shoppingListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Új tétel hozzáadás
        val addButton = findViewById<Button>(R.id.add_button)
        val itemEditText = findViewById<EditText>(R.id.edit_text)

        addButton.setOnClickListener {
            val itemName = itemEditText.text.toString()
            if (itemName.isNotBlank()) {
                val newItem = ShoppingItem(itemName)
                shoppingListAdapter.addItem(newItem)
                itemEditText.text.clear()
            }
        }

        //Swipe to dismiss cucc
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) : Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.RIGHT) {
                    shoppingListAdapter.markAsBought(position)
                } else if (direction == ItemTouchHelper.LEFT) {
                    shoppingListAdapter.removeItem(position)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onDestroy() {
        super.onDestroy()
        shoppingListAdapter.releasePlayers()
    }
}