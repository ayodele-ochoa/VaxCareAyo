package com.ayoapps.vaxcareayo.views

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.ayoapps.vaxcareayo.databinding.FragmentBookDetailBinding
import com.ayoapps.vaxcareayo.models.Book
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class BookDetailFragment : Fragment() {

    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var bookDetails: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve bundle of data sent to fragment by source fragment
        bookDetails = arguments?.getSerializable("bookDetails") as Book
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    // Format data to set text for views
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        binding.txtTitleDesc.text = bookDetails.title
        binding.txtAuthorDesc.text = bookDetails.author
        val cashFee = "%.2f".format(bookDetails.fee.toFloat())
        binding.txtFeeDesc.text = "$$cashFee"
        val editedDateTime = ZonedDateTime.parse(bookDetails.lastEdited)
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm")
        val formattedDateTime: String = editedDateTime.format(formatter)
        binding.txtLastEditedDesc.text = formattedDateTime
    }
}