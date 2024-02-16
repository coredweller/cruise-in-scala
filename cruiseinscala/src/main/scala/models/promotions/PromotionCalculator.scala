package models.promotions

class PromotionCalculator {

  def allCombinablePromotions(allPromotions: Seq[Promotion]): Seq[PromotionCombo] = {
    if(allPromotions.isEmpty){
      println("allPromotions are empty. Please provide Promotions to continue.")
      return Seq.empty
    }

    //Get all of the codes by themselves. Ex: List(P1, P2, P3, P4, P5)
    val allCodes: Seq[String] = allPromotions.map(ap => ap.code)
    //Get only the codes that are not itself or not immediately excluded. Ex: List((P1,List(P2, P4, P5)), (P2,List(P1, P3)), (P3,List(P2, P4, P5)), (P4,List(P1, P3, P5)), (P5,List(P1, P3, P4)))
    val allValidCodes: Seq[(String, Seq[String])] = allPromotions.map(ap => (ap.code,
      allCodes.filter(all => all != ap.code
        && !ap.notCombinableWith.contains(all)
        )))
    println("ALL VALID CODES: " + allValidCodes)

    val result: Seq[PromotionCombo] = allValidCodes.flatMap(avc => {
        println("VALID CODE: " + avc)
        //Check each child (each set of possible valid codes is a child here)
        avc._2.map(currentChild => {
          println("CURRENT CHILD: " + currentChild)
          allPromotions.find(_.code == currentChild).get.notCombinableWith.contains(avc._1)
          //Check all of the ones that are not the current child and see if it can be combined with the current child
          val possibleValidPromotions: Seq[Option[String]] = avc._2.filter(a => a != currentChild).map(v => {
            println("V V V V V V: " + v)
            allPromotions.find(_.code == v).get.notCombinableWith.contains(currentChild) match {
              case true => None
              case false => Some(v)
            }
          })
          println("ALL POSSIBLE VALID PROMOTIONS: " + possibleValidPromotions)
          //Only take the ones that can be combined
          //val childCombo = if(allPromotions.find(_.code == currentChild).get.notCombinableWith.contains(avc._1)) { Seq.empty } else { Seq(currentChild) }
          val childCombo = Seq(currentChild)
          val combos: Seq[String] = childCombo.concat(possibleValidPromotions.collect {
            case Some(s: String) => s
          })
          println("COMBOS COMBOS: " + combos)
          //Sort them so they can be distinctified later
          val finalCombo: Seq[String] = Seq(avc._1).concat(combos).sortBy(f => f)
          println("FINAL FINAL FINAL: " + finalCombo)
          PromotionCombo(finalCombo)
        })
      }).distinct

    //Turn promotionCodes into Sets to make sure there are no subsets of other entries
    result.filter(r => !result.exists(k => k != r && r.promotionCodes.toSet.subsetOf(k.promotionCodes.toSet)))
  }

  def combinablePromotions(promotionCode: String, allPromotions: Seq[Promotion]): Seq[PromotionCombo] = {
    //Get all combinations for all codes
    val allCombos: Seq[PromotionCombo] = allCombinablePromotions(allPromotions)
    //Filter the Combinations for only the desired promotionCode
    var filteredCombos: Seq[PromotionCombo] = allCombos.filter(ac => ac.promotionCodes.contains(promotionCode))
    //Filter the lists so that it doesn't include the desired promotionCode as it may be anywhere in the list
    val removedItem: Seq[PromotionCombo] = filteredCombos.map(fc => PromotionCombo(fc.promotionCodes.filterNot(c => c == promotionCode)))
    //Add the promotion code to the front of every PromotionCombo
    removedItem.map(ri => PromotionCombo(promotionCode +: ri.promotionCodes))
  }
}
