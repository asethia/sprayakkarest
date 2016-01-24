package com.sprayakkarest.akka.util

import spray.json._
import spray.httpx.SprayJsonSupport
import com.sprayakkarest.akka.model.ProcessModel._

trait RuleProcessMarshalling extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val lineItemFormat = jsonFormat2(LineItem)
  implicit val listLineItemFormat = jsonFormat1(ListLineItem)
  implicit val transactionFormat = jsonFormat1(Transaction)
  implicit val responseMsgFormat = jsonFormat1(ResponseMsg)
  implicit val createOffersMsgFormat = jsonFormat1(CreateOffers)
}