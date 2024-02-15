package cruiseCalculation

import models.*
import models.pricing.{CabinPrice, PricingCalculator, Rate}

@main def run(problemNumber: Int): Unit =
  println("Hello and thanks for coming!")

  if(problemNumber == 1) {
    def rates: Seq[Rate] = Seq(Rate("M1", "Military"), Rate("M2", "Military"), Rate("S1", "Senior"), Rate("S2", "Senior"))
    def prices = Seq(CabinPrice("CA", "M1", 200.00),
      CabinPrice("CA", "M2", 250.00),
      CabinPrice("CA", "S1", 225.00),
      CabinPrice("CA", "S2", 260.00),
      CabinPrice("CB", "M1", 230.00),
      CabinPrice("CB", "M2", 260.00),
      CabinPrice("CB", "S1", 245.00),
      CabinPrice("CB", "S2", 270.00))

    val calculator = new PricingCalculator()
    val bestPrices = calculator.getBestGroupPrices(rates, prices)
    println("BestPrices " + bestPrices)
  }
  else if(problemNumber == 2) {

  }
  else {
    println(s"Invalid problem number of $problemNumber given. Please enter 1 or 2.")
  }


end run