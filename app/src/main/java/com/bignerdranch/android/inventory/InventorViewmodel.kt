package com.bignerdranch.android.inventory

import androidx.lifecycle.*
import com.bignerdranch.android.inventory.data.Item
import com.bignerdranch.android.inventory.data.ItemDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class InventorViewModel(private val dao: ItemDao) : ViewModel() {
    val allItems: LiveData<List<Item>> = dao.getItems().asLiveData()

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            dao.insert(item)
        }
    }

    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantity = itemCount.toInt()
        )
    }

    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    fun retrieveItem(id: Int): LiveData<Item> {
        return dao.getItem(id).asLiveData()
    }

    private fun updateItem(item: Item) {
        viewModelScope.launch {
            dao.update(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch { dao.delete(item) }
    }

    fun sell(item: Item) {
        if (item.quantity > 0) {
            val newItem = item.copy(quantity = item.quantity - 1)
            updateItem(newItem)
        }
    }

    fun isStockAvailable(item: Item): Boolean {
        return (item.quantity > 0)
    }

    private fun getUpdatedItemEntry(
        itemId: Int,
        ItemName: String,
        itemPrice: String,
        itemCount: String
    ): Item {
        return Item(
            id = itemId,
            itemName = ItemName,
            itemPrice = itemPrice.toDouble(),
            quantity = itemCount.toInt()
        )
    }

    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }


}

class InventorViewModelFactory(private val dao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventorViewModel::class.java)) {
            return InventorViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }

}