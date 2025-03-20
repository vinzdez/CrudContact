package com.vince.appcontact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vince.appcontact.model.Contact
import com.vince.appcontact.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) :
    ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

    private val _editingContact = MutableStateFlow<Contact?>(null)
    val editingContact: StateFlow<Contact?> = _editingContact.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getContacts().collect { contactsList ->
                _contacts.value = contactsList
            }
        }
    }

    fun addUpdateContact(name: String, email: String) {
        if (name.isBlank() || email.isBlank()) return

        val contact = Contact(name = name, email = email)
        viewModelScope.launch {
            repository.addUpdateContact(contact)
        }
    }

    fun updateContact(name: String, email: String) {
        viewModelScope.launch {
            val currentContact = _editingContact.value
            if (currentContact != null) {
                val updatedContact = currentContact.copy(name = name, email = email)
                viewModelScope.launch {
                    repository.addUpdateContact(updatedContact)
                    resetEditing()
                }
            }
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.deleteContact(contact.id)
        }
    }

    fun startEditing(contact: Contact) {
        _editingContact.value = contact
    }

    private fun resetEditing() {
        _editingContact.value = null
    }

}