package fr.antoine.maintenancecesi

import java.util.*

class TicketModel (
        val id: String = "plant0",
        val name: String = "Nom",
        val niveau: String = "Rez-de-chaussée",
        val zone: String = "Zone 1",
        val salle: String = "Salle 01",
        val materiel: String = "Pc portable",
        val categorie: String = "Informatique",
        val description: String = "Petite description",
        val date: String = "01/01/2021",
        val imageUrl: String = "https://cdn.pixabay.com/photo/2016/06/20/13/44/paper-1468883_960_720.jpg",
        var valid: Boolean = false
)