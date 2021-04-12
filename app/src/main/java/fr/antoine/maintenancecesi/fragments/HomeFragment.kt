package fr.antoine.maintenancecesi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.antoine.maintenancecesi.MainActivity
import fr.antoine.maintenancecesi.R
import fr.antoine.maintenancecesi.TicketModel
import fr.antoine.maintenancecesi.TicketRepository.Singleton.ticketList
import fr.antoine.maintenancecesi.adapter.TicketAdapter
import fr.antoine.maintenancecesi.adapter.TicketItemDecoration

class HomeFragment(
        private val context: MainActivity
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(R.layout.fragment_home, container, false)

        // Recuperer le recyclerView
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = TicketAdapter(context, ticketList.filter { !it.valid }, R.layout.item_horizontal_ticket)

        // Recuperer le second recyclerView
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter = TicketAdapter(context, ticketList.sortedBy { it.date }, R.layout.item_vertical_ticket)
        verticalRecyclerView.addItemDecoration(TicketItemDecoration())

        return view
    }
}