package fr.antoine.maintenancecesi.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import fr.antoine.maintenancecesi.MainActivity
import fr.antoine.maintenancecesi.R
import fr.antoine.maintenancecesi.TicketModel
import fr.antoine.maintenancecesi.TicketRepository
import fr.antoine.maintenancecesi.TicketRepository.Singleton.downloadUri
import java.text.DateFormat
import java.util.*


class AddTicketFragment(
        private val context: MainActivity
): Fragment() {

    private var file: Uri? = null
    private var uploadedImage: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_add_ticket, container, false)

        // recupere uploadedImage pour lui associer son composant
        uploadedImage = view.findViewById(R.id.preview_image)

        // recuperer le boutton pour charger l'image
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)

        // lorsqu'on clique dessus ça ouvre les images du téléphone
        pickupImageButton.setOnClickListener { pickupImage() }

        val intent = Intent().setClass(context, HomeFragment::class.java)

        // recuperer le bouton confirmer
        val confirmButton = view.findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener {
            sendForm(view)
            returnHome()
        }

        return view
    }

    // permet de revenir à la page d'accueil après clique sur le bouton confirmer pour réaliser une demande
    private fun returnHome(){
        val repo = TicketRepository()

        repo.updateData {
            val frman: FragmentManager? = fragmentManager
            val ftran: FragmentTransaction = frman?.beginTransaction() ?: return@updateData
            val ffrag = HomeFragment(context)
            ftran.replace(R.id.fragment_container, ffrag)
            ftran.commit()
        }
    }

    private fun pickupImage () {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 47)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 47 && resultCode == Activity.RESULT_OK){
            // verifier si les données réceptionner sont null
            if(data == null || data.data == null) return

            // recupere l'image
            file = data.data

            // mettre a jour l'aperçu de l'image
            uploadedImage?.setImageURI(file)
        }
    }

    private fun sendForm(view: View) {
        // heberger sur le bucket
        val repo = TicketRepository()
        repo.uploadImage(file!!) {
            val ticketName = view.findViewById<EditText>(R.id.name_input).text.toString()
            val ticketNiveau = view.findViewById<Spinner>(R.id.niveau_input).selectedItem.toString()
            val ticketZone = view.findViewById<Spinner>(R.id.zone_input).selectedItem.toString()
            val ticketSalle = view.findViewById<EditText>(R.id.salle_input).text.toString()
            val ticketMateriel = view.findViewById<EditText>(R.id.materiel_input).text.toString()
            val ticketCategorie = view.findViewById<Spinner>(R.id.categorie_input).selectedItem.toString()
            val ticketDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
            val downloadImageUrl = downloadUri

            val now = Date()
            val dateformatter: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
            val formattedDate: String = dateformatter.format(now).toString()
            // val timeformatter: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
            // val formattedTime: String = timeformatter.format(now).toString()

            // créer un nouvel objet de type TicketModel
            val ticket = TicketModel(
                    UUID.randomUUID().toString(),
                    ticketName,
                    ticketNiveau,
                    ticketZone,
                    ticketSalle,
                    ticketMateriel,
                    ticketCategorie,
                    ticketDescription,
                    formattedDate,
                    downloadImageUrl.toString()
            )

            // envoyer en bdd
            repo.insertTicket(ticket)
        }
    }
}

