package com.ayoapps.vaxcareayo.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

data class BookListDataModel(private var _booklist: MutableList<Book> = mutableListOf()) : BaseObservable() {

    var booklist: MutableList<Book>
        @Bindable get() = _booklist
        set(value) {
            _booklist = value
            notifyChange()
        }

}
