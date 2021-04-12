package fr.antoine.maintenancecesi.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TicketItemDecoration : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = 20
    }
}