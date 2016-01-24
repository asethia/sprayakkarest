package com.sprayakkarest.akka.actor

import akka.actor._
import scala.concurrent.Future
import scala.concurrent.Await
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._



class RuleProcessor(timeout: Timeout) extends Actor {
  import com.sprayakkarest.akka.model.ProcessModel._

  implicit def executionContext = context.dispatcher
  implicit val requestTimeout = timeout

  def receive = {
    case Transaction(items: ListLineItem) =>
      //this will Iterable[Future[Int]
      val result = context.children.map { child => ask(child, items).mapTo[Int] }
      //convert Iterable[Future[Int] to Future[Iterable[Int]
      val futureList = Future.sequence(result)
      val oddSum = Await.result(futureList.map(_.sum), Duration(200, MILLISECONDS)).asInstanceOf[Int]
      sender() ! ResponseMsg("Points earned "+ oddSum.toString)
    case CreateOffers(count: Int) =>
      for {
        i <- List.range(10000, 10000 + count)
      } yield (context.actorOf(Props(classOf[OfferActor], new Offer(i, 10)), "offer" + i.toString()))
      sender() ! ResponseMsg("test1")
  }
}

