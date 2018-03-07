package assistant.tuenti.com.myapplication

sealed class ViewState {

	data class Initial(
		val assistantBarViewState: BarViewState,
		val notificationCounter: Int
	) : ViewState()

	data class Thinking(
		val assistantBarViewState: BarViewState,
		val question: String,
		val showThinkingFeedback: Boolean
	) : ViewState()

	data class ShowingResponse(
		val assistantBarViewState: BarViewState,
		val question: String,
		val assistantResponse: Response
	) :
		ViewState()

	data class ShowingError(
		val assistantBarViewState: BarViewState,
		val question: String
	) : ViewState()

	object WritingState : ViewState()

	data class NotificationList(
		val assistantBarViewState: BarViewState,
		val notifications: List<Notification>
	) : ViewState()

	data class NotificationDetail(
		val assistantBarViewState: BarViewState,
		val notification: Notification
	) : ViewState()

	data class Walking(
		val assistantBarViewState: BarViewState,
		val assistantResponse: Response
	) : ViewState()

	data class Help(
		val assistantBarViewState: BarViewState,
		val showLoading: Boolean,
		val showBack: Boolean,
		val elements: List<Element>
	) : ViewState()
}

data class BarViewState(
	val alfredState: Int,
	val keyboardState: Int,
	val microphoneState: Int
)

data class Notification(val id: String)
data class Element(val id: String)


sealed class PartialState {

	sealed class InfoPartialState {
		object Waiting : PartialState(), Cloneable
		object Timeout : PartialState()
		data class HelpTopics(val responses: List<String>) : PartialState()
	}

	sealed class NotificationPartialState {
		object NotificationError : PartialState()
		data class Notifications(val responses: List<String>) : PartialState()
	}

	sealed class ListeningPartialState {
		object ListeningError : PartialState()
		object ListeningStart : PartialState()
		data class PartialResult(val value: String) : PartialState()
		data class FinalResult(val value: String) : PartialState()
	}

	sealed class ShowingErrorPartialState {
		data class SendMessageError(val message: String) : PartialState()
		data class GeneralError(val message: String) : PartialState()
	}

	sealed class ShowResponsePartialState {
		data class ShowResponse(val response: Response) : PartialState()
	}

	data class ThinkingState(val message: String) : PartialState()

	object SendingQuestion : PartialState()
	object ShowKeyboard : PartialState()
	object InitialState : PartialState()

}


object GeneralError
object Response
