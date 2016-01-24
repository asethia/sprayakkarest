package com.sprayakkarest.akka.service

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import akka.event.Logging._
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.routing.directives.LogEntry
import akka.event.Logging.InfoLevel
import java.util.concurrent.TimeUnit
import com.sprayakkarest.akka.util.RuleProcessMarshalling
import com.sprayakkarest.akka.model.ProcessModel._
import com.sprayakkarest.akka.actor._
import com.sprayakkarest.akka.util._

class RestAPI(timeout: Timeout) extends HttpServiceActor with Service {

  implicit val requestTimeout = timeout

  implicit def executionContext = context.dispatcher

  def createRuleProcessor = context.actorOf(Props(classOf[RuleProcessor], timeout))

  def requestMethodAndResponseStatusAsInfo(req: HttpRequest): Any => Option[LogEntry] = {
    case res: HttpResponse => Some(LogEntry(req.method + ":::" + req.uri + ":" + res.message.status, InfoLevel))
    case _                 => Some(LogEntry(req.method + "::" + req.uri, InfoLevel)) // other kind of responses
  }

  def routeWithLogging = logRequestResponse(requestMethodAndResponseStatusAsInfo _)(routes)

  def receive: Receive = runRoute(routeWithLogging)

}

trait Service extends HttpService with RuleProcessing with RuleProcessMarshalling {

  import StatusCodes._

  def routes: Route = tranProcessEvent ~ createOffersEvent

  def tranProcessEvent = pathPrefix("transaction") {
    pathEndOrSingleSlash {
      post {
        entity(as[Transaction]) { ed =>
          onSuccess(processRules(ed)) { response =>
            complete(OK, response)
          }
        }
      }
    }
  }

  def createOffersEvent = pathPrefix("offer" / Segment) { offercount =>
    pathEndOrSingleSlash {
      post {
        onSuccess(createOffers(offercount)) { response =>
          complete(OK, response)
        }
      }
    }
  }
}

trait RuleProcessing {

  implicit def executionContext: ExecutionContext
  implicit def requestTimeout: Timeout

  def createRuleProcessor(): ActorRef

  lazy val ruleProcessor = createRuleProcessor()

  def processRules(transaction: Transaction) = {
    ruleProcessor.ask(transaction).mapTo[ResponseMsg]
  }

  def createOffers(offercount: String) = {
    ruleProcessor.ask(new CreateOffers(offercount.toInt)).mapTo[ResponseMsg]
  }

}