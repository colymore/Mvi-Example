package assistant.tuenti.com.myapplication

import assistant.tuenti.com.myapplication.PartialState.InfoPartialState.HelpTopics
import assistant.tuenti.com.myapplication.PartialState.InfoPartialState.Timeout
import assistant.tuenti.com.myapplication.PartialState.InfoPartialState.Waiting
import assistant.tuenti.com.myapplication.PartialState.ListeningPartialState
import assistant.tuenti.com.myapplication.PartialState.NotificationPartialState.NotificationError
import assistant.tuenti.com.myapplication.PartialState.NotificationPartialState.Notifications
import assistant.tuenti.com.myapplication.PartialState.ShowingErrorPartialState
import assistant.tuenti.com.myapplication.PartialState.ThinkingState
import assistant.tuenti.com.myapplication.SpeechToTextUseCase.SpeechState.FinalResult
import assistant.tuenti.com.myapplication.SpeechToTextUseCase.SpeechState.Init
import assistant.tuenti.com.myapplication.SpeechToTextUseCase.SpeechState.PartialResult
import assistant.tuenti.com.myapplication.SpeechToTextUseCase.SpeechState.SpeechError
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


class AssistantMainPresenter {

	private lateinit var getHelp: HelpUseCase
	private lateinit var getNotificationsUseCase: NotificationsUseCase
	private lateinit var speechToText: SpeechToTextUseCase
	private lateinit var sendQuestion: SendQuestion
	private lateinit var response: Responses

	private val reducer: StateReducer = StateReducer()

	fun bindIntents(assistantMainView: AssistantView) {

		val alfredIntent: Observable<PartialState> = processAlfredIntent(assistantMainView)
		val notificationsIntent: Observable<PartialState> = processNotificationsIntent(assistantMainView)
		val microphoneIntent: Observable<PartialState> = processMicrophoneIntent(assistantMainView)
		val sendRequestIntent: Observable<PartialState> = progressSendRequestIntent(assistantMainView)
		val keyboardIntent: Observable<PartialState> = processKeyboardIntent(assistantMainView)
		val assistantResponsesIntent: Observable<PartialState> = processAssistantResponses()

		val allIntents: Observable<PartialState> =
			Observable.merge(
				listOf(
					alfredIntent,
					notificationsIntent,
					microphoneIntent,
					sendRequestIntent,
					keyboardIntent,
					assistantResponsesIntent
				)
			)

		allIntents
			.scan<ViewState>(ViewState.Initial(BarViewState(1, 1, 1), 0), { previous, partial2 -> reducer.reduce(previous, partial2) })
			.doOnNext { assistantMainView.render(it) }
	}

	private fun processAssistantResponses(): Observable<PartialState> = response
		.get()
		.map {
			it.fold(
				{ PartialState.ShowingErrorPartialState.GeneralError("GeneralError generaal") },
				{ PartialState.ShowResponsePartialState.ShowResponse(it) }
			)
		}

	private fun processKeyboardIntent(assistantMainView: AssistantView): Observable<PartialState> =
		assistantMainView.keyboardIntent().map { PartialState.ShowKeyboard }

	private fun progressSendRequestIntent(assistantMainView: AssistantView): Observable<PartialState> = assistantMainView
		.sendRequestIntent()
		.flatMap { sendQuestion.send(it).toObservable<PartialState>() }
		.map<PartialState>({ ThinkingState("Pensando..") })
		.onErrorResumeNext(Observable.just(ShowingErrorPartialState.SendMessageError("GeneralError enviando mensaje")))

	private fun processMicrophoneIntent(assistantMainView: AssistantView): Observable<PartialState> = assistantMainView
		.microphoneIntent()
		.flatMap { speechToText.speech() }
		.map<PartialState> {
			when (it) {
				SpeechError -> ListeningPartialState.ListeningError
				PartialResult -> ListeningPartialState.PartialResult(it.toString())
				FinalResult -> ListeningPartialState.FinalResult(it.toString())
				Init -> ListeningPartialState.ListeningStart
			}
		}

	private fun processNotificationsIntent(assistantMainView: AssistantView): Observable<PartialState> = assistantMainView
		.alfredIntent()
		.flatMap { getNotificationsUseCase.getNotifications() }
		.mergeWith(Observable.timer(30, TimeUnit.SECONDS)
			.flatMap({ Observable.just(Either.left<GeneralError, List<String>>(GeneralError)) })
		)
		.take(1)
		.map { it.fold({ NotificationError }, { Notifications(it) }) }
		.startWith(Waiting)

	private fun processAlfredIntent(assistantMainView: AssistantView): Observable<PartialState> = assistantMainView
		.alfredIntent()
		.flatMap { getHelp.getCategories() }
		.mergeWith(Observable.timer(30, TimeUnit.SECONDS)
			.flatMap({ Observable.just(Either.left<GeneralError, List<String>>(GeneralError)) }))
		.take(1)
		.map { it.fold({ Timeout }, { HelpTopics(it) }) }
		.startWith(Waiting)
}




