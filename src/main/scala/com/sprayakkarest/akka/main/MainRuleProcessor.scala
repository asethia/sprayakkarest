package com.sprayakkarest.akka.main

import akka.actor._
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import spray.can.Http
import akka.io.IO
import akka.pattern.ask

/*
 * This is main object to start the process
 */

object MainRuleProcessor {

  import com.sprayakkarest.akka.service._
  
  implicit val system = ActorSystem("smauleprocessing")

  implicit val timeout = Timeout(100, TimeUnit.SECONDS)

  val service = system.actorOf(Props(new RestAPI(timeout)), "rest-service")

  IO(Http) ? Http.Bind(service, interface = "localhost", port = 9090)

}