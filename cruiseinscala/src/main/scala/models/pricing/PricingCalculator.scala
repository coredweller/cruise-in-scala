package models.pricing

import models.pricing.{BestGroupPrice, CabinPrice}

class PricingCalculator {
  def getBestGroupPrices(rates: Seq[Rate], prices: Seq[CabinPrice]): Seq[BestGroupPrice] = {
    if(rates.isEmpty) {
      println("Rates are empty. Please provide valid Rates to continue.")
      return Seq.empty
    }

    if(prices.isEmpty) {
      println("Prices are empty. Please provide valid Prices to continue.")
      return Seq.empty
    }

    //Get all the codes in each group together
    val codesForGroup = rates.map(r => (r.rateGroup, r.rateCode)).groupBy(_._1).mapValues(_.map(_._2)).toList
    //Make sure no 2 rateGroups have a common rateCode
    val rateGroupCounts = codesForGroup.flatMap(c => c._2).groupBy(cf => cf).mapValues(_.size).toList

    //Make sure no Rate Code is found in more than 1 Rate Group
    val rgc = rateGroupCounts.find(rgc => rgc._2 > 1)
    rgc match {
      case Some(s, i) => {
        println(s"Rate Code $s is found in $i Rate Groups. The same Rate Code cannot exist in multiple rate groups")
        return Seq.empty
      }
      case None =>
    }

    //Get all distinct cabin codes that exist
    val allCabinCodes: Seq[String] = prices.map(p => p.cabinCode).distinct
    //For each distinct cabin code extract the prices that have the lowest price for the cabin code and rate code combination
    allCabinCodes
        .flatMap(cabin => codesForGroup
          .flatMap(code => prices
            .filter(pr => pr.cabinCode == cabin && code._2.contains(pr.rateCode))
              .sortBy(_.price).take(1)
                .map(cp => {
                  val group = codesForGroup.find(cfg => cfg._2.contains(cp.rateCode)).get._1
                  BestGroupPrice(cp.cabinCode, cp.rateCode, cp.price, group)
                })))
  }
}