package com.ayoapps.vaxcareayo.views

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayoapps.vaxcareayo.R
import com.ayoapps.vaxcareayo.databinding.BookListFragmentBinding
import com.ayoapps.vaxcareayo.models.Book
import com.ayoapps.vaxcareayo.viewmodels.BookListViewModel

class BookListFragment : Fragment(), BookListAdapter.onItemClickListener {

    private var viewModel = BookListViewModel()
    private var _binding: BookListFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerview: RecyclerView
    lateinit var spinner: ProgressBar
    lateinit var bookList: MutableList<Book>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BookListFragmentBinding.inflate(inflater, container, false)

        bookList = mutableListOf()

        spinner = binding.progressBar
        spinner.visibility = View.VISIBLE

        recyclerview = binding.bookListRecyclerView
        recyclerview.layoutManager = LinearLayoutManager(activity?.applicationContext)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BookListViewModel::class.java)
        activity?.let { viewModel.getBookList(it?.applicationContext) }

        /* Set observer to listen for LiveData update.
           When bookList is set, set adapter to display results */
        viewModel.bookListSetLiveData.observe(viewLifecycleOwner, Observer {
            if (viewModel.bookListSetLiveData.value == true) {
                setRecyclerViewAdapter()
            }
        })

        /* Set observer to listen for LiveData update.
           If network is disconnected show network alert dialog */
        viewModel.networkDisconnectedLiveData.observe(viewLifecycleOwner, Observer {
            if (viewModel.bookListSetLiveData.value == true) {
                showNetworkDialog()
            }
        })
    }

    private fun showNetworkDialog() {
        spinner.visibility = View.GONE
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.network_error)
            builder.setMessage(R.string.check_network)
            builder.apply {
                setNegativeButton(R.string.ok, { dialog, _ ->
                    dialog.dismiss()
                })
            }
            builder.create()
        }

        alertDialog?.show()
    }

    // Stop progress spinner and display Book results
    fun setRecyclerViewAdapter(){
        spinner.visibility = View.GONE
        val dataModel = viewModel.bookListDataModel
        bookList = dataModel.booklist
        val adapter = BookListAdapter(bookList, this@BookListFragment)
        recyclerview.adapter = adapter
    }

    // On item click navigate to BookDetailFragment with data of selected Book
    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("bookDetails", bookList.get(position))
        findNavController().navigate(R.id.action_list_to_detail, bundle)
    }
}
