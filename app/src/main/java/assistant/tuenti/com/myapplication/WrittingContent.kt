package assistant.tuenti.com.myapplication

import android.support.v4.app.Fragment
import android.widget.TextView


class WrittingContent : Fragment() {
	fun getInput(): TextView {
		return TextView(context)
	}
}
