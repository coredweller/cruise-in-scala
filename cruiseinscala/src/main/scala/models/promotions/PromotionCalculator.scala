package models.promotions

class PromotionCalculator {

  def allCombinablePromotions(allPromotions: Seq[Promotion]): Seq[PromotionCombo] = {
    if(allPromotions.isEmpty){
      println("allPromotions are empty. Please provide Promotions to continue.")
      return Seq.empty
    }

    //Merge notCombinableWith lists for duplicate Promotions
    val allProms: Seq[Promotion] = allPromotions.groupMapReduce(_.code)(_.notCombinableWith)(_.concat(_)).map(x => Promotion(x._1, x._2)).toSeq.sortBy(r => r.code)

    //Get only the codes that are not itself or not immediately excluded.
    //   Ex: List((P1,List(P2, P4, P5)), (P2,List(P1, P3)), (P3,List(P2, P4, P5)), (P4,List(P1, P3, P5)), (P5,List(P1, P3, P4)))
    val allValidCodes: Seq[(String, Seq[String])] = allProms.map(ap => (ap.code,
      allProms.map(ap => ap.code).filterNot(a => a == ap.code)
              .filterNot(all => ap.notCombinableWith.contains(all))
              .filterNot(dd => canSourceBeCombinedWithItem(allProms, dd, ap.code))))

    val result: Seq[PromotionCombo] = allValidCodes.flatMap(avc => {
        //Check each child (each set of possible valid codes is a child here)
        avc._2.map(currentChild => {
          //Check all of the ones that are not the current child and see if it can be combined with the current child
          val possibleValidPromotions: Seq[String] = avc._2.filterNot(a => a == currentChild).filterNot(v => {
            canSourceBeCombinedWithItem(allProms, v, currentChild)
              && canSourceBeCombinedWithItem(allProms, currentChild, v)})

          //Only take the ones that can be combined with the original Promotion
          val childCombo: Seq[Option[String]] = if(canSourceBeCombinedWithItem(allProms, currentChild, avc._1)) { Seq.empty } else { Seq(Some(currentChild)) }

          //Remove promotions that are notCombinableWith the others in the valid promotions list
          val combos: Seq[Option[String]] = childCombo.concat(possibleValidPromotions.collect {
            case s: String => {
              allProms.filterNot(_.code == s)
                      .filter(x => possibleValidPromotions.contains(x.code))
                      .flatMap(_.notCombinableWith)
                      .contains(s) match {
                                    case true => None
                                    case false => Some(s)
                                  }}})

          //Add the original code to the final combo
          PromotionCombo(Seq(avc._1).concat(combos
                                    .collect { case Some(s: String) => s })
                                    .sortBy(f => f))
        })
      }).distinct

    //Turn promotionCodes into Sets to make sure there are no subsets of other entries
    result.filter(r => !result.exists(k => k != r && r.promotionCodes.toSet.subsetOf(k.promotionCodes.toSet)))
  }

  private def canSourceBeCombinedWithItem(promotions: Seq[Promotion], source: String, item2: String): Boolean ={
    promotions.find(_.code == source).get.notCombinableWith.contains(item2)
  }

  def combinablePromotions(promotionCode: String, allPromotions: Seq[Promotion]): Seq[PromotionCombo] = {
    if(allPromotions.isEmpty || !allPromotions.map(_.code).contains(promotionCode))
    {
      return Seq.empty
    }

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
