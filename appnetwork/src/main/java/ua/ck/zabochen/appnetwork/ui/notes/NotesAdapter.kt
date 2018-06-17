package ua.ck.zabochen.appnetwork.ui.notes

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import ua.ck.zabochen.appnetwork.R
import ua.ck.zabochen.appnetwork.network.model.Note
import ua.ck.zabochen.appnetwork.utils.Helper

class NotesAdapter(
        noteList: List<Note>
) : RecyclerView.Adapter<NotesAdapter.MyViewHolder>() {

    private val mNoteList = noteList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_note, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mNoteList[position])
    }

    override fun getItemCount(): Int {
        return if (mNoteList.isNotEmpty()) mNoteList.size else 0
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.adapterItemNote_textView_dot)
        lateinit var mDot: TextView

        @BindView(R.id.adapterItemNote_textView_note)
        lateinit var mNote: TextView

        @BindView(R.id.adapterItemNote_textView_timestamp)
        lateinit var timeStamp: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(note: Note) {
            // Dot
            mDot.text = Html.fromHtml("&#8226;")
            mDot.setTextColor(Helper.getRandomMaterialColor(itemView.context, "400"))

            // Note
            mNote.text = note.name
        }
    }

}