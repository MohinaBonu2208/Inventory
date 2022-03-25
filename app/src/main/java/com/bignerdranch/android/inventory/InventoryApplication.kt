package com.bignerdranch.android.inventory



import android.app.Application
import com.bignerdranch.android.inventory.data.ItemRoomDatabase

class InventoryApplication : Application() {
    val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this) }
}