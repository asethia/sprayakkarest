package com.sprayakkarest.akka.actor

import akka.actor._
import com.sprayakkarest.akka.actor._
import com.sprayakkarest.akka.model.ProcessModel._

/**
 * offer actor to eval offer based on line item
 */
class OfferActor(offer: Offer) extends Actor {
  def receive = {
    case items: ListLineItem =>
      val points = for {
        item <- items.lineItems
        if (offer.itemId.equals(item.itemId))
      } yield (offer.points)
      sender() ! sum(points)
  }

  /**
   * sum all points
   */
  def sum(xs: List[Int]): Int = {
    xs match {
      case x :: tail => x + sum(tail)
      case Nil       => 0
    }
  }
}

