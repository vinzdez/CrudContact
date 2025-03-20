package com.vince.appcontact.repository

import com.vince.appcontact.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun addUpdateContact(contact: Contact)
    suspend fun deleteContact(id: Long)
    fun getContacts(): Flow<List<Contact>>
}