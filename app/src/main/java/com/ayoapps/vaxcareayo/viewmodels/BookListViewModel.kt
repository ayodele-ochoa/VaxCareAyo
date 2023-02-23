package com.ayoapps.vaxcareayo.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoapps.vaxcareayo.models.Book
import com.ayoapps.vaxcareayo.models.BookListDataModel
import com.ayoapps.vaxcareayo.utils.NetworkCheck
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class BookListViewModel : ViewModel() {

    private val client = OkHttpClient()
    private val requestUrl =
        "https://gist.githubusercontent.com/android-test-vaxcare/02cb5f20bf3398ca46884e6c8e18ce89/raw/462e69054eaef1ac92386c549f66324e4b89dbde/local-database.json"
    val bookListDataModel = BookListDataModel()
    val bookListSetLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val networkDisconnectedLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private val dispatcherMain: CoroutineDispatcher = Dispatchers.Main

    // Returns list of books from remote API request
    fun getBookList(context: Context) {
        // Check for internet connection
        if (NetworkCheck.networkConnected(context)) {
            val request = Request.Builder() // Send request to url
                .url(requestUrl)
                .build()

            client.newCall(request).enqueue(object : Callback {
                // On failure throw exception with details
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: okhttp3.Call, response: Response) {
                    response.use {
                        /* If response is not successful throw exception, else parse results and
                        create list of Books */
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        // Parse results with gson to create list of Books
                        val gson = GsonBuilder().create()
                        val books = gson.fromJson(response.body!!.string(), Array<Book>::class.java)
                            .toList()

                        bookListDataModel.booklist = books.toMutableList()
                        viewModelScope.launch(dispatcherMain) {
                            // List is loaded, update recyclerview in main thread
                            bookListSetLiveData.value = true
                        }
                    }
                }
            })
        } else {
            viewModelScope.launch(dispatcherMain) {
                // Network is not connected. Trigger alert dialog in main thread
                networkDisconnectedLiveData.value = true
            }
        }
    }
}