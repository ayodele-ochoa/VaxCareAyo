package com.ayoapps.vaxcareayo.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ayoapps.vaxcareayo.R
import com.ayoapps.vaxcareayo.models.Book

class BookListAdapter(private val booklist: MutableList<Book>, private val listener: onItemClickListener) :
    RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View, listener: onItemClickListener) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView
        val authorTextView: TextView

        init {
            // Define click listener for the ViewHolder's View
            titleTextView = view.findViewById(R.id.txt_general_book_title)
            authorTextView = view.findViewById(R.id.txt_general_book_author)

            // When view is clicked send index of clicked view to fragment
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.book_general_card, viewGroup, false)

        return ViewHolder(view, listener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.titleTextView.text = booklist[position].title
        viewHolder.authorTextView.text = booklist[position].author
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = booklist.size

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
}