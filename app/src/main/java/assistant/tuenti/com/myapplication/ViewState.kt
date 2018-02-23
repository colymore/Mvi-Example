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
