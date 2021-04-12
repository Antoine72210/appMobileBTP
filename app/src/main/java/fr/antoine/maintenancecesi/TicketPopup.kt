package fr.antoine.maintenancecesi

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import fr.antoine.maintenancecesi.adapter.TicketAdapter

class TicketPopup(
    private val adapter: TicketAdapter,
    private val currentTicket: TicketModel
) : Dialog(adapter.context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_tickets_details)
        setupComponents()
        setupCloseButton()
        setupCircleButton()
    }

    private fun updateCircle(button: ImageView){
        if(currentTicket.valid) {
            button.setImageResource(R.drawable.ic_circle_valid)
        }
        else {
            button.setImageResource(R.drawable.ic_circle)
        }
    }

    private fun setupCircleButton() {
        // recuperer
        val circleButton = findViewById<ImageView>(R.id.circle_button)

        updateCircle(circleButton)

        // interaction
        circleButton.setOnClickListener {
            currentTicket.valid = !currentTicket.valid
            val repo = TicketRepository()
            repo.updateTicket(currentTicket)
            updateCircle(circleButton)
        }
    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.close_button).setOnClickListener {
            // fermer la fenêtre popup
            dismiss()
        }
    }

    private fun setupComponents() {
        // actualiser l'image du ticket
        val ticketImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentTicket.imageUrl)).into(ticketImage)

        // actualiser le nom du ticket
        findViewById<TextView>(R.id.popup_page_titre_title).text = currentTicket.name

        // actualiser la description du ticket
        findViewById<TextView>(R.id.popup_page_description_subtitle).text = currentTicket.description

        // actualiser la categorie du ticket
        findViewById<TextView>(R.id.popup_page_categorie_subtitle).text = currentTicket.categorie

        // actualiser le materiel du ticket
        findViewById<TextView>(R.id.popup_page_materiel_subtitle).text = currentTicket.materiel

        // actualiser le niveau du ticket
        findViewById<TextView>(R.id.popup_page_niveau_subtitle).text = currentTicket.niveau

        // actualiser la salle du ticket
        findViewById<TextView>(R.id.popup_page_salle_subtitle).text = currentTicket.salle

        // actualiser la zone du ticket
        findViewById<TextView>(R.id.popup_page_zone_subtitle).text = currentTicket.zone
    }
}