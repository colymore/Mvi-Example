package assistant.tuenti.com.myapplication

import com.annimon.stream.function.Predicate
import java.util.*

class Either<A, B> private constructor(private val left: A?, private val right: B?) {

	private fun isLeft() = left != null
	private fun isRight() = right != null

	private fun getLeft(): A {
		if (left == null) {
			throw NoSuchElementException("No left value present")
		}
		return left
	}

	private fun getRight(): B {
		if (right == null) {
			throw NoSuchElementException("No right value present")
		}
		return right
	}

	fun swap(): Either<B, A> = Either(right, left)

	fun run(applyRight: (B) -> Unit): Either<A, B> {
		right?.let(applyRight)
		return this
	}

	fun <C> fold(applyLeft: (A) -> C, applyRight: (B) -> C): C {
		return when {
			isLeft() -> applyLeft(getLeft())
			else -> applyRight(getRight())
		}
	}

	fun <C> map(transformRight: (B) -> C): Either<A, C> {
		return when {
			isRight() -> Either.right(transformRight(getRight()))
			else -> Either.left(getLeft())
		}
	}

	fun filter(ifRight: Predicate<B>): Boolean {
		return isLeft() || ifRight.test(getRight())
	}

	companion object {

		fun <A, B> left(left: A): Either<A, B> {
			return Either(left, null)
		}

		fun <A, B> right(right: B): Either<A, B> {
			return Either(null, right)
		}
	}
}
