package com.vince.appcontact

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.vince.appcontact.repository.ContactRepositoryImpl
import com.vince.appcontact.viewmodel.ContactViewModel


class MainActivity : ComponentActivity() {
    lateinit var appDatabase: AppDatabase
    lateinit var viewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (!::appDatabase.isInitialized) {
            appDatabase =
                Room.databaseBuilder(applicationContext, AppDatabase::class.java, "Contacts")
                    .build()
        }

        val repository = ContactRepositoryImpl(appDatabase.contactDao())
        viewModel = ContactViewModel(repository)

        setContent {
            NameEmailInputScreen(viewModel)
        }
    }
}

@Composable
fun NameEmailInputScreen(viewModel: ContactViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var id by remember { mutableLongStateOf(0) }
    val contactList by viewModel.contacts.collectAsState()
    val editingContact by viewModel.editingContact.collectAsState()

    LaunchedEffect(editingContact) {
        name = editingContact?.name ?: ""
        email = editingContact?.email ?: ""
        id = editingContact?.id ?: 0
    }

    Column(modifier = Modifier.padding(15.dp)) {
        // Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Add Button
        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank()) {
                    if (editingContact != null) {
                        editingContact?.let {
                            viewModel.updateContact(name, email)

                        }
                    } else {
                        viewModel.addUpdateContact(name, email)
                    }

                    name = ""
                    email = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            val btnLabel = if (editingContact == null) "Add" else "Update"
            Text(btnLabel)
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(contactList) { contact ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Text(text = "Name: ${contact.name}", fontWeight = FontWeight.Bold)
                        Text(text = "Email: ${contact.email}")

                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        IconButton(onClick = {
                            viewModel.startEditing(contact)
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Blue)
                        }

                        // Delete Button
                        IconButton(onClick = {
                            viewModel.deleteContact(contact)
                        }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}