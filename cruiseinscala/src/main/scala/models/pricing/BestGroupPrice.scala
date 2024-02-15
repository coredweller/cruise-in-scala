package models.pricing

case class BestGroupPrice(cabinCode: String,
                          rateCode: String,
                          price: BigDecimal,
                          rateGroup: String)
