package assistant.tuenti.com.myapplication

import io.reactivex.Completable
import io.reactivex.Observable

interface HelpUseCase {
	fun getCategories(): Observable<Either<GeneralError, List<String>>>
}

interface NotificationsUseCase {
	fun getNotifications(): Observable<Either<GeneralError, List<String>>>
}

interface SendQuestion {
	fun send(question: String): Completable
}

interface Responses {
	fun get(): Observable<Either<GeneralError, Response>>
}

interface SpeechToTextUseCase {

	sealed class SpeechState {
		object SpeechError : SpeechState()
		object Init : SpeechState()
		object PartialResult : SpeechState()
		object FinalResult : SpeechState()
	}

	fun speech(): Observable<SpeechState>
}