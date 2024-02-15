package models.promotions

case class Promotion(code: String, notCombinableWith: Seq[String])
