package ua.ck.zabochen.rxjavaexample.view.operator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.Disposable
import ua.ck.zabochen.rxjavaexample.R

class ConcatMapFragment : Fragment() {

    /*
    * FlatMap & ConcatMap produces the same output but the sequence the data emitted changes
    * ConcatMap maintains the order of items and waits for the current Observable to complete its job before emitting the next one.
    * ConcatMap is more suitable when you want to maintain the order of execution
    */

    private lateinit var mDisposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_concat_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDetach() {
        super.onDetach()

        // Unsubscribe
        mDisposable.dispose()
    }

}