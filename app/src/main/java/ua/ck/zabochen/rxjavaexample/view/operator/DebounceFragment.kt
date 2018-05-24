package ua.ck.zabochen.rxjavaexample.view.operator

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R
import java.util.concurrent.TimeUnit

class DebounceFragment : Fragment(), AnkoLogger {

    /*
    * Debounce operators emits items only when a specified time span is passed.
    * User want to search for `RxJava`. Without debounce, there would be multiple calls to server
    * for keywords `R`, `Rx`, `RxJ` and so on. Instead we can give user a time period say 300 milli sec
    * to type and send the query the to server. Most probably user can type `RxJ` in the given time period.
    */

    private lateinit var mDisposable: CompositeDisposable
    private lateinit var mButterKnifeUnbinder: Unbinder

    // Views
    @BindView(R.id.fragmentDebounce_editTextView)
    lateinit var mEditTextView: EditText

    @BindView(R.id.fragmentDebounce_textView)
    lateinit var mTextView: TextView

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // Subscribers holder init
        mDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Layout
        val view: View = inflater.inflate(R.layout.fragment_debounce, container, false)
        // Binding
        mButterKnifeUnbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RxTextView
                .textChangeEvents(mEditTextView)
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        object : Observer<TextViewTextChangeEvent> {
                            override fun onSubscribe(d: Disposable) {
                                info { "Subscribe" }
                            }

                            override fun onNext(t: TextViewTextChangeEvent) {
                                mTextView.text = t.text()
                                info { "Next => ${t.text()}" }
                            }

                            override fun onComplete() {
                                info { "Complete" }
                            }

                            override fun onError(e: Throwable) {
                                info { "Error => ${e.printStackTrace()}" }
                            }
                        }
                )
    }

    override fun onDetach() {
        super.onDetach()

        // Clear
        mDisposable.clear()
        mButterKnifeUnbinder.unbind()
    }

}