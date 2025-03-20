package com.vince.appcontact.model.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vince.appcontact.model.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Upsert
    suspend fun addUpdateUser(contact: Contact)

    @Query("SELECT * FROM Contact")
    fun getAllContacts(): Flow<List<Contact>>

    @Query("DELETE FROM Contact WHERE id = :id")
    suspend fun deleteContactById(id: Long)
}