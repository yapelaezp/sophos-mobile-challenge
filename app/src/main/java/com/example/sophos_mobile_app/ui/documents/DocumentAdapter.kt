package com.example.sophos_mobile_app.ui.documents

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.data.model.Document
import com.example.sophos_mobile_app.databinding.ItemDocumentDetailBinding

class DocumentAdapter(private val documentList: List<Document>) :
    RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        return DocumentViewHolder(
            ItemDocumentDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(documentHolder: DocumentViewHolder, position: Int) {
        val document = documentList[position]
        documentHolder.binding.tvItemDocumentDate.text = document.date
        documentHolder.binding.tvItemDocumentDetailType.text = document.attachedType
        documentHolder.binding.tvItemDocumentDetailName.text = document.name + " " + document.lastname
            //Resources.getSystem().getString(R.string.full_name,document.name, document.lastname) // TODO preguntar
    }

    override fun getItemCount() = documentList.size

    inner class DocumentViewHolder(val binding: ItemDocumentDetailBinding) :
        RecyclerView.ViewHolder(binding.root)

}