package fr.antoine.maintenancecesi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.antoine.maintenancecesi.MainActivity
import fr.antoine.maintenancecesi.R
import fr.antoine.maintenancecesi.TicketRepository.Singleton.ticketList
import fr.antoine.maintenancecesi.adapter.TicketAdapter
import fr.antoine.maintenancecesi.adapter.TicketItemDecoration

class CollectionFragment(
        private val context: MainActivity
) : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_collection, container, false)

        // recuperer ma recyclerView
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.collection_recycler_list)
        collectionRecyclerView.adapter = TicketAdapter(context, ticketList.filter { it.valid }, R.layout.item_vertical_ticket)
        collectionRecyclerView.layoutManager = LinearLayoutManager(context)
        collectionRecyclerView.addItemDecoration(TicketItemDecoration())

        return view
    }
}