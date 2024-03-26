package com.sto_opka91.memorystick.ui.main_fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sto_opka91.memorystick.R
import com.sto_opka91.memorystick.databinding.ItemNoteLayoutBinding
import com.sto_opka91.memorystick.models.Note
import com.sto_opka91.memorystick.utils.HelpMethods

class MainRcAdapter(val listener: NotesItemClickListener): ListAdapter<Note, MainRcAdapter.Holder>(Comparator()) {

    class  Holder(view: View, val listener: NotesItemClickListener?) : RecyclerView.ViewHolder(view) {

        val binding = ItemNoteLayoutBinding.bind(view)

        @SuppressLint("SuspiciousIndentation")
        fun bind(item: Note) = with(binding) {
            tvTitleText.text = item.title
            tvNoteText.text = item.noteText
            if (item.date != "") {
                tvDateText.text = item.date
                tvDateText.visibility = View.VISIBLE
                tvDateTitle.visibility = View.VISIBLE
                val dateFlag = HelpMethods.getDateBool(item.date!!)
                if (dateFlag) {
                    cvNote.setCardBackgroundColor(itemView.resources.getColor(R.color.light3, null))
                    tvDateTitle.text = itemView.resources.getText(R.string.note_date)
                } else {
                    cvNote.setCardBackgroundColor(itemView.resources.getColor(R.color.light2, null))
                    tvDateTitle.text = itemView.resources.getText(R.string.note_date_done)
                }

            } else {
                cvNote.setCardBackgroundColor(itemView.resources.getColor(R.color.light1, null))
                tvDateText.visibility = View.GONE
                tvDateTitle.visibility = View.GONE
            }
            if (item.title != "") {
                tvTitleText.visibility = View.VISIBLE
                tvTitleText.text = item.title
            } else {
                tvTitleText.visibility = View.GONE
            }
            if (item.noteText != "") {
                tvNoteText.visibility = View.VISIBLE
                tvNoteText.text = item.noteText
            } else {
                tvNoteText.visibility = View.GONE
            }
            if(item.bitmapImage!=null){
                imUploadImage.visibility = View.VISIBLE
                val bitmap = HelpMethods.getBitmapFromByteArray(item.bitmapImage)
                imUploadImage.setImageBitmap(bitmap)

            }else{
                imUploadImage.visibility = View.GONE

            }
            cvNote.setOnClickListener {
                listener?.onItemClick(item)
            }
            ivDeleteNote.setOnClickListener {
                listener?.onLongItemClicked(
                    item
                )
            }

            tvDateCreate.text = item.createDate
        }
    }

    class Comparator : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id==newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note_layout, parent,false)
        return Holder(view, listener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    interface NotesItemClickListener{
        fun onItemClick(note: Note)
        fun onLongItemClicked(note: Note)
    }

}