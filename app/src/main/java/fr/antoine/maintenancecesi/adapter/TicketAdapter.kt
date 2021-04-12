package fr.antoine.maintenancecesi.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.antoine.maintenancecesi.*

class TicketAdapter(
        val context: MainActivity,
        private val ticketList: List<TicketModel>,
        private val layoutId: Int
) : RecyclerView.Adapter<TicketAdapter.ViewHolder>(){

    // Boite pour ranger tout les composants à controller
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val etageImage = view.findViewById<ImageView>(R.id.image_item)
        val ticketName: TextView? = view.findViewById(R.id.name_item)
        val ticketDescription: TextView? = view.findViewById(R.id.description_item)
        val circleIcon = view.findViewById<ImageView>(R.id.circle_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // récuperer les informations d'un ticket
        val currentTicket = ticketList[position]

        // récuperer le repository
        val repo = TicketRepository()

        // utiliser glide pour recuperer l'image a partir de son lien -> composant
        Glide.with(context).load(Uri.parse(currentTicket.imageUrl)).into(holder.etageImage)

        // mettre a jour le nom de la plante
        holder.ticketName?.text = currentTicket.name

        // mettre a jour la descritpion du ticket
        holder.ticketDescription?.text = currentTicket.description

        // vérifier si le ticket a été validé ou non
        if (currentTicket.valid) {
            holder.circleIcon.setImageResource(R.drawable.ic_circle_valid)
        }
        else {
            holder.circleIcon.setImageResource(R.drawable.ic_circle)
        }

        // rajouter une interaction sur ce circle
        holder.circleIcon.setOnClickListener{
            // inverser si le bouton est like ou non
            currentTicket.valid = !currentTicket.valid

            // mettre a jour l'objet ticket
            repo.updateTicket(currentTicket)
        }

        // interaction lors du click sur un ticket
        holder.itemView.setOnClickListener{
            // afficher la popup
            TicketPopup(this, currentTicket).show()
        }
    }

    override fun getItemCount(): Int = ticketList.size
}