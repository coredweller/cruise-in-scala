import models.promotions._

class PromotionCalculatorTests extends munit.FunSuite {
  val Promotions: Seq[Promotion] = Seq(Promotion("P1", Seq("P3")), // P1 is not combinable with P3
    Promotion("P2", Seq("P4", "P5")), // P2 is not combinable with P4 and P5
    Promotion("P3", Seq("P1")), // P3 is not combinable with P1
    Promotion("P4", Seq("P2")), // P4 is not combinable with P2
    Promotion("P5", Seq("P2"))) // P5 is not combinable with P2

  val SuccessfulCombos: Seq[PromotionCombo] = Seq(PromotionCombo(Seq("P1", "P2")),
                                                  PromotionCombo(Seq("P1", "P4", "P5")),
                                                  PromotionCombo(Seq("P2", "P3")),
                                                  PromotionCombo(Seq("P3", "P4", "P5")))

  test("Given no Promotions it should return an empty list") {
    val result = new PromotionCalculator().allCombinablePromotions(Seq.empty)
    assertEquals(result, Seq.empty)
  }

  test("Given normal Promotions it should return successfully") {
    val result = new PromotionCalculator().allCombinablePromotions(Promotions)
    assertEquals(result, SuccessfulCombos)
  }

  test("Given two Promotions with the same code it should return successfully") {
    val promotions: Seq[Promotion] = Seq(Promotion("P1", Seq("P3")), // P1 is not combinable with P2 or P3
      Promotion("P2", Seq("P4", "P5")), // P2 is not combinable with P4 and P5
      Promotion("P3", Seq("P1")), // P3 is not combinable with P1
      Promotion("P4", Seq("P2")), // P4 is not combinable with P2
      Promotion("P1", Seq("P2"))) // P1 is not combinable with P2

    val result = new PromotionCalculator().allCombinablePromotions(promotions)
    val expected = List(PromotionCombo(promotionCodes = List("P1", "P4")),
                        PromotionCombo(promotionCodes = List("P2", "P3")),
                        PromotionCombo(promotionCodes = List("P3", "P4")))

    assertEquals(result, expected)
  }

  test("Given more exclusions it should return successfully") {
    val promotions: Seq[Promotion] = Seq(Promotion("P1", Seq("P2", "P3")), // P1 is not combinable with P2 or P3
      Promotion("P2", Seq("P4", "P5")), // P2 is not combinable with P4 and P5
      Promotion("P3", Seq("P1")), // P3 is not combinable with P1
      Promotion("P4", Seq("P2"))) // P4 is not combinable with P2

    val result = new PromotionCalculator().allCombinablePromotions(promotions)
    val expected = List(PromotionCombo(promotionCodes = List("P1", "P4")),
      PromotionCombo(promotionCodes = List("P2", "P3")),
      PromotionCombo(promotionCodes = List("P3", "P4")))
    assertEquals(result, expected)
  }

  test("Given one promotion with duplicate combinable restriction lists it should return successfully") {
    val promotions: Seq[Promotion] = Seq(Promotion("P1", Seq("P3", "P3")), // P1 is not combinable with P2 or P3
      Promotion("P2", Seq("P4", "P5")), // P2 is not combinable with P4 and P5
      Promotion("P3", Seq("P1")), // P3 is not combinable with P1
      Promotion("P4", Seq("P2")), // P4 is not combinable with P2
      Promotion("P5", Seq("P2"))) // P5 is not combinable with P2

    val result = new PromotionCalculator().allCombinablePromotions(promotions)
    assertEquals(result, SuccessfulCombos)
  }
}
