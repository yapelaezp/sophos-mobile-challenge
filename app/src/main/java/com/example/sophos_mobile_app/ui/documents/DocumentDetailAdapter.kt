package com.example.sophos_mobile_app.ui.documents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sophos_mobile_app.data.model.Document
import com.example.sophos_mobile_app.databinding.ItemDocumentDetailBinding

class DocumentDetailAdapter(private val documentList: List<Document>,private val  onDocumentItemClickListener: (String) -> Unit) :
    RecyclerView.Adapter<DocumentDetailAdapter.DocumentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        return DocumentViewHolder(
            ItemDocumentDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ){ position ->
            onDocumentItemClickListener(documentList[position].logId)
        }
    }

    override fun onBindViewHolder(documentHolder: DocumentViewHolder, position: Int) {
        val document = documentList[position]
        documentHolder.binding.tvItemDocumentDate.text = document.date
        documentHolder.binding.tvItemDocumentDetailType.text = document.attachedType
        documentHolder.binding.tvItemDocumentDetailName.text = document.name + " " + document.lastname
    }

    override fun getItemCount() = documentList.size

    inner class DocumentViewHolder(val binding: ItemDocumentDetailBinding, clickAtPosition: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root){
            init {
                binding.root.setOnClickListener{
                    clickAtPosition(adapterPosition)
                }
            }
        }
}