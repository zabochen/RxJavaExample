package ua.ck.zabochen.rxjavaexample.view.binding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import ua.ck.zabochen.rxjavaexample.R
import java.util.concurrent.TimeUnit

class ClickListenerFragment : androidx.fragment.app.Fragment(), AnkoLogger {

    private lateinit var mCompositeDisposable: CompositeDisposable
    private lateinit var mButterKnifeUnbinder: Unbinder

    @BindView(R.id.fragmentListener_editText)
    lateinit var mEditText: EditText

    @BindView(R.id.fragmentListener_buttonNative)
    lateinit var mButtonNative: Button

    @BindView(R.id.fragmentListener_buttonRx)
    lateinit var mButtonRx: Button

    @BindView(R.id.fragmentListener_textView)
    lateinit var mTextView: TextView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_listener, container, false)
        mButterKnifeUnbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonClickListener()
        editTextChangedListener()
    }

    override fun onDetach() {
        super.onDetach()
        // Clear
        mButterKnifeUnbinder.unbind()
        mCompositeDisposable.clear()
    }

    private fun buttonClickListener() {

        // Native Button ClickListener
        mButtonNative.setOnClickListener(
                object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        Toast.makeText(activity, "Native Click Listener", Toast.LENGTH_LONG)
                                .show()
                    }
                }
        )

        // Rx Button Click Listener - Version 1
        val observer: DisposableObserver<Any> = RxView.clicks(mButtonRx)
                .subscribeWith(
                        object : DisposableObserver<Any>() {
                            override fun onNext(t: Any) {
                                Toast.makeText(activity, "onNext: Rx Button Click Listener - Version 1", Toast.LENGTH_LONG)
                                        .show()
                            }

                            override fun onComplete() {
                                Toast.makeText(activity, "onComplete: Rx Button Click Listener - Version 1", Toast.LENGTH_LONG)
                                        .show()
                            }

                            override fun onError(e: Throwable) {
                                Toast.makeText(activity, "onError: Rx Button Click Listener - Version 1", Toast.LENGTH_LONG)
                                        .show()
                            }
                        }
                )

        mCompositeDisposable.add(observer)

        // Rx Button Click Listener - Version 2
        mCompositeDisposable.add(RxView.clicks(mButtonRx)
                .subscribe(
                        object : Consumer<Any> {
                            override fun accept(t: Any?) {
                                Toast.makeText(activity, "Rx Button Click Listener - Version 2", Toast.LENGTH_LONG)
                                        .show()
                            }
                        }
                )
        )

        // Rx Button Click Listener - Version 3
        mCompositeDisposable.add(
                RxView.clicks(mButtonRx)
                        .subscribe {
                            Toast.makeText(activity, "Rx Button Click Listener - Version 3", Toast.LENGTH_LONG)
                                    .show()
                        }
        )

    }

    private fun editTextChangedListener() {

        // Native Text Changed Listener
        mEditText.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        mTextView.text = s
                    }

                    override fun afterTextChanged(s: Editable?) {
                    }
                }
        )

        // Rx Text Changed Listener
        mCompositeDisposable.add(RxTextView.textChanges(mEditText)
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        object : Consumer<CharSequence> {
                            override fun accept(t: CharSequence?) {
                                mTextView.text = t
                            }
                        }
                )
        )
    }

}