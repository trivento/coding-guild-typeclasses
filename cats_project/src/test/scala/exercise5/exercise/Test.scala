package exercise5.exercise

import cats.data.NonEmptyList
import cats.data.Validated.{Invalid, Valid}
import exercise5.exercise.Domain._
import org.scalatest.{FlatSpec, Inside, Matchers}

class Test extends FlatSpec with Matchers with Inside {

  behavior of "order-validation"

  it should "return a validated result for proper input" in {
    val order = Order(Map(
      "cola" → 2,
      "wine" → 3,
      "beer" → 5
    ))

    val validatedOrder = validateOrder(order)

    validatedOrder shouldBe Valid(order)
  }

  it should "return all validation errors for incorrect input" in {
    val illegalOrder = Order(Map(
      "Rhum" → 1337, // two errors (not exists and too high)
      "beer" → 42, // one error (too high)
      "cola" → -1, // one error (too low)
      "wine" → 3 // valid (should not appear in errors)
    ))

    val expectedErrors = NonEmptyList.of(
      NonExistingProduct("Rhum"),
      TooLargeOrder("Rhum", 1337, 10),
      TooLargeOrder("beer", 42, 10),
      TooSmallOrder("cola", -1, 1)
    )

    val validatedOrder = validateOrder(illegalOrder)

    validatedOrder shouldBe 'invalid
    inside(validatedOrder) { case Invalid(list: NonEmptyList[OrderError]@unchecked) ⇒
      list.toList should contain theSameElementsAs expectedErrors.toList
    }
  }
}
