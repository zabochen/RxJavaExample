package ua.ck.zabochen.appnetwork.ui.notes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import ua.ck.zabochen.appnetwork.R

class NotesFragment : Fragment() {

    @BindView(R.id.fragmentNotes_recyclerView)
    lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // View & ButterKnife
        val view: View = inflater.inflate(R.layout.fragment_notes, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}