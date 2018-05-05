package uk.me.rembrandt.sparkhivespike

import java.sql.Date

case class SecurityDetails(
  secId: Int,
  secMnem: String,
  productSupType: String,
  product: String,
  cUSIP: String,
  agency: String,
  prefix: String,
  issAmt: Double,
  collType: String,
  issDt: Date,
  matDt: Date,
  origWac: Double,
  origWam: Int,
  origWala: Int,
  retireDt: Date,
  issueId: Int,
  priceId: Int,
  tBAEligCode: String,
  ssnCode: String,
  insDt: String,
  updDt: String,
  normalisationDate: Date,
  isNew: Boolean)
