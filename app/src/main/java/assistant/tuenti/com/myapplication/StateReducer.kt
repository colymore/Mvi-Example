package assistant.tuenti.com.myapplication

class StateReducer {
	fun reduce(previousState: ViewState, newPartialState: PartialState): ViewState {
		return when (newPartialState) {
			PartialState.InfoPartialState.Waiting -> ViewState.Help(BarViewState(1, 2, 3), true, false, emptyList())
			else -> ViewState.Help(BarViewState(1, 2, 3), false, false, listOf(Element("id")))//Pending other states
		}
	}
}