package com.sprayakkarest.akka.model

object ProcessModel {
  /**
   * offer case class
   */
  case class Offer(itemId: Int, points: Int)
  case class ResponseMsg(msg: String)
  //transaction will be passed as {"items":{"lineItems": [{"itemId":10001,"qty":20},{"itemId":10002,"qty":20}]}}
  case class Transaction(items: ListLineItem)
  case class LineItem(itemId: Int, qty: Int)
  case class ListLineItem(lineItems: List[LineItem])
  case class CreateOffers(count: Int)

}