package fr.antoine.maintenancecesi

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import fr.antoine.maintenancecesi.TicketRepository.Singleton.databaseRef
import fr.antoine.maintenancecesi.TicketRepository.Singleton.downloadUri
import fr.antoine.maintenancecesi.TicketRepository.Singleton.storageReference
import fr.antoine.maintenancecesi.TicketRepository.Singleton.ticketList
import java.util.*
import kotlin.coroutines.Continuation


class TicketRepository {

    object Singleton {
        // donner le lien pour acceder au bucket
        private val BUCKET_URL: String = "gs://maintenancecesi.appspot.com"

        // se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // se connecter à la référence Ticket
        val databaseRef = FirebaseDatabase.getInstance().getReference("tickets")

        // créer une liste contenant les tickets
        val ticketList = arrayListOf<TicketModel>()

        // contenir le lien de l'image courante
        var downloadUri: Uri? = null
    }

    fun updateData(callback: () -> Unit) {
        // absorber les données depuis la databaseRef -> liste de tickets
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // retirer le anciens tickets
                ticketList.clear()
                // récolter la liste
                for (ds in snapshot.children) {
                    // Contruire un objet Ticket
                    val ticket = ds.getValue(TicketModel::class.java)

                    // vérifier qu'un ticket n'est pas nul
                    if (ticket != null) {
                        // ajouter un ticket à la liste
                        ticketList.add(ticket)
                    }
                }
                // actionner le callback
                callback()
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    // créer une fonction pour envoyer des fichiers sur le storage
    fun uploadImage(file: Uri, callback: () -> Unit) {
        // vérifier que ce fichier n'est pas null
        if (file != null) {
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(fileName)
            val uploadTask = ref.putFile(file)

            // demarrer la tâche d'envoi
            uploadTask.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->

                // si il y a eu un problème lors de l'envoi du fichier
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }

                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                // verifier si tout a bien fonctionné
                if(task.isSuccessful) {
                    // recuperer l'image
                    downloadUri = task.result
                    callback()
                }
            }
        }
    }

    // mettre a jour un objet ticket en bdd
    fun updateTicket(ticket: TicketModel) = databaseRef.child(ticket.id).setValue(ticket)

    // inserer un nouveau ticket en bdd
    fun insertTicket(ticket: TicketModel) = databaseRef.child(ticket.id).setValue(ticket)

}
