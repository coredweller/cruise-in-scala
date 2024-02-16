package cruiseCalculation

import models.pricing.{BestGroupPrice, CabinPrice, PricingCalculator, Rate}
import models.promotions.*

//TODO: think of a better way to handle params, this was quick and dirty
@main def run(problemName: String, functionNumber: Int, promotionCode: String): Unit =
  println("Welcome and thanks for coming!")

  problemName match {
    case Inputs.PricingName => {
      val calculator = new PricingCalculator()
      val bestPrices: Seq[BestGroupPrice] = calculator.getBestGroupPrices(Inputs.Rates, Inputs.Prices)
      println("BestPrices " + bestPrices)
    }
    case Inputs.PromotionName => {
      val calculator = new PromotionCalculator()
      functionNumber match {
        case 1 => {
          val allCombinations: Seq[PromotionCombo] = calculator.allCombinablePromotions(Inputs.Promotions)
          println("AllCombos " + allCombinations)
        }
        case 2 => {
          val filteredCombos: Seq[PromotionCombo] = calculator.combinablePromotions(promotionCode, Inputs.Promotions)
          println("FilteredCombos " + filteredCombos)
        }
      }
    }
    case _ =>
      println(s"Invalid problemName of $problemName given. Please enter ${Inputs.PricingName} or ${Inputs.PromotionName}.")
  }
end run