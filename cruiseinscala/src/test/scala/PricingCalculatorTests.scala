import models.*
import models.pricing.{BestGroupPrice, CabinPrice, PricingCalculator, Rate}

class PricingCalculatorTests extends munit.FunSuite {
  val DefaultRates: Seq[Rate] = Seq(Rate("M1", "Military"), Rate("M2", "Military"), Rate("S1", "Senior"), Rate("S2", "Senior"))
  val DefaultPrices = Seq(CabinPrice("CA", "M1", 200.00),
    CabinPrice("CA", "M2", 250.00),
    CabinPrice("CA", "S1", 225.00),
    CabinPrice("CA", "S2", 260.00),
    CabinPrice("CB", "M1", 230.00),
    CabinPrice("CB", "M2", 260.00),
    CabinPrice("CB", "S1", 245.00),
    CabinPrice("CB", "S2", 270.00))
  val DefaultResult = Seq(BestGroupPrice("CA", "M1", 200.00, "Military"),
                          BestGroupPrice("CA", "S1", 225.00, "Senior"),
                          BestGroupPrice("CB", "M1", 230.00, "Military"),
                          BestGroupPrice("CB", "S1", 245.00, "Senior"))

  test("Given no rate groups it should return an empty list") {
    val result = new PricingCalculator().getBestGroupPrices(Seq.empty, DefaultPrices)
    assertEquals(result, Seq.empty)
  }

  test("Given no prices it should return an empty list") {
    val result = new PricingCalculator().getBestGroupPrices(DefaultRates, Seq.empty)
    assertEquals(result, Seq.empty)
  }

  test("Given 2 cabins with the same price it should return correctly") {
    def prices = Seq(CabinPrice("CA", "M1", 200.00),
      CabinPrice("CA", "M2", 200.00),
      CabinPrice("CA", "S1", 225.00),
      CabinPrice("CA", "S2", 260.00),
      CabinPrice("CB", "M1", 230.00),
      CabinPrice("CB", "M2", 260.00),
      CabinPrice("CB", "S1", 245.00),
      CabinPrice("CB", "S2", 270.00))
    val result = new PricingCalculator().getBestGroupPrices(DefaultRates, prices)
    assertEquals(result, DefaultResult)
  }

  test("Given a cabin with an unknown rate group it should return successfully without the invalid item") {
   def prices = Seq(CabinPrice("CA", "Q1", 200.00),
     CabinPrice("CA", "M2", 250.00),
     CabinPrice("CA", "S1", 225.00),
     CabinPrice("CA", "S2", 260.00),
     CabinPrice("CB", "M1", 230.00),
     CabinPrice("CB", "M2", 260.00),
     CabinPrice("CB", "S1", 245.00),
     CabinPrice("CB", "S2", 270.00))
   val result = new PricingCalculator().getBestGroupPrices(DefaultRates, prices)
   val desiredResult = Seq(BestGroupPrice("CA", "M2", 250.00, "Military"),
                           BestGroupPrice("CA", "S1", 225.00, "Senior"),
                           BestGroupPrice("CB", "M1", 230.00, "Military"),
                           BestGroupPrice("CB", "S1", 245.00, "Senior"))
   assertEquals(result, desiredResult)
  }

  test("Given lots of one rateGroup type it should return successfully") {
    val prices = Seq(CabinPrice("CA", "M1", 200.00),
      CabinPrice("CA", "M2", 250.00),
      CabinPrice("CA", "M2", 225.00),
      CabinPrice("CA", "M2", 260.00),
      CabinPrice("CB", "M2", 230.00),
      CabinPrice("CB", "M2", 260.00),
      CabinPrice("CB", "M1", 245.00),
      CabinPrice("CB", "M1", 210.00))
    val result = new PricingCalculator().getBestGroupPrices(DefaultRates, prices)
    val desiredResult = Seq(BestGroupPrice("CA", "M1", 200.00, "Military"), BestGroupPrice("CB", "M1", 210.00, "Military"))
    assertEquals(result, desiredResult)
  }

  test("Given no prices are in the rate groups it should return empty") {
    val rates: Seq[Rate] = Seq(Rate("Q1", "Military"), Rate("Z2", "Military"), Rate("R1", "Senior"), Rate("N2", "Senior"))
    val result = new PricingCalculator().getBestGroupPrices(rates, DefaultPrices)
    assertEquals(result, Seq.empty)
  }

  test("Given a rateCode in multiple rateGroups it should return empty") {
    val rates: Seq[Rate] = Seq(Rate("M1", "Military"), Rate("M2", "Military"), Rate("M1", "Senior"), Rate("S2", "Senior"))
    val result = new PricingCalculator().getBestGroupPrices(rates, DefaultPrices)
    assertEquals(result, Seq.empty)
  }
}
