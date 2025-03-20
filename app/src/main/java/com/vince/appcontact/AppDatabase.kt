package com.vince.appcontact

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vince.appcontact.model.Contact
import com.vince.appcontact.model.dao.ContactDao

@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}