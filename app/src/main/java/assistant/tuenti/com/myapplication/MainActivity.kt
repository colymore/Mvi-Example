package assistant.tuenti.com.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.FrameLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable


class MainActivity : AppCompatActivity(), AssistantView {

	private lateinit var presenter: AssistantMainPresenter
	private lateinit var micButton: Button
	private lateinit var textButton: Button
	private lateinit var helpButton: Button
	private lateinit var not: Button
	private lateinit var container: FrameLayout


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		micButton = findViewById(R.id.micbtn)
		textButton = findViewById(R.id.micbtn)
		helpButton = findViewById(R.id.micbtn)
		not = findViewById(R.id.not)
		container = findViewById(R.id.container)

		presenter.bindIntents(this)
	}

	override fun alfredIntent(): Observable<Unit> = RxView.clicks(helpButton).map { _ -> Unit }

	override fun microphoneIntent(): Observable<Unit> = RxView.clicks(micButton).map { _ -> Unit }

	override fun keyboardIntent(): Observable<Unit> = RxView.clicks(textButton).map { _ -> Unit }

	override fun notificationIntent(): Observable<Int> = RxView.clicks(not).map { _ -> 1 }

	@SuppressLint("CheckResult")
	override fun sendRequestIntent(): Observable<String> = Observable.create { emitter ->
		if (supportFragmentManager.findFragmentByTag("TAG") is WrittingContent) {
			val fragment = supportFragmentManager.findFragmentByTag("TAG") as WrittingContent
			RxTextView
				.editorActionEvents(fragment.getInput(), { t -> t.actionId() == EditorInfo.IME_ACTION_SEND })
				.subscribe {
					emitter.onNext(fragment.getInput().text.toString())
				}
		}
	}

	override fun render(viewState: ViewState) {
		//render
	}
}
