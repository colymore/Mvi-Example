package assistant.tuenti.com.myapplication

import io.reactivex.Observable

interface AssistantView {

	/**
	 * The alfred intent
	 *
	 * @return An observable emitting the action tap over alfred option
	 */
	fun alfredIntent(): Observable<Unit>

	/**
	 * The microphone intent
	 *
	 * @return An observable emitting the action tap over microphone option
	 */
	fun microphoneIntent(): Observable<Unit>

	/**
	 * The keyboard intent
	 *
	 * @return An observable emitting the action tap over keyboard option
	 */
	fun keyboardIntent(): Observable<Unit>

	/**
	 * The notification intent
	 *
	 * @return An observable emitting the action tap over notification option
	 */
	fun notificationIntent(): Observable<Int>

	/**
	 * The send message intent
	 *
	 * @return An observable emitting the action done by user when has written his request
	 */
	fun sendRequestIntent(): Observable<String>

	/**
	 * Renders the View
	 *
	 * @param viewState The current viewState state that should be displayed
	 */
	fun render(viewState: ViewState)
}