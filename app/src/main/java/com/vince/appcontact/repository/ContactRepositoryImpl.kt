package com.vince.appcontact.repository

import com.vince.appcontact.model.Contact
import com.vince.appcontact.model.dao.ContactDao
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ContactRepositoryImpl(private val contactDao: ContactDao): ContactRepository {

    override suspend fun addUpdateContact(contact: Contact) {
        withContext(IO){
            contactDao.addUpdateUser(contact)
        }
    }

    override suspend fun deleteContact(id: Long) {
        withContext(IO){
            contactDao.deleteContactById(id)
        }
    }

    override fun getContacts(): Flow<List<Contact>> = contactDao.getAllContacts()


}