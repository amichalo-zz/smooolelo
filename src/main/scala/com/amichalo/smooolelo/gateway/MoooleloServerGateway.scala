package com.amichalo.smooolelo.gateway

import java.io.IOException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import com.amichalo.smooolelo.config.MoooleloServerConfig
import com.amichalo.smooolelo.domain.HealthStatus
import com.amichalo.smooolelo.gateway.MoooleloServerGateway.{HeartbeatRequest, RegistrationRequest}
import com.amichalo.smooolelo.protocol.SMoooleloJsonProtocol
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{Future, ExecutionContext}

object MoooleloServerGateway {
  case class RegistrationRequest(id: Int,
                                 serviceType: String,
                                 group: String,
                                 environment: String,
                                 hostname: String,
                                 ip: String,
                                 port: Option[Int],
                                 health: HealthStatus,
                                 version: Option[String])

  case class HeartbeatRequest(id: Int,
                              serviceType: String,
                              group: String,
                              environment: String,
                              health: HealthStatus)
}

class MoooleloServerGateway(config: MoooleloServerConfig)(implicit ac: ActorSystem, materializer: Materializer, ec: ExecutionContext)
  extends LazyLogging with SMoooleloJsonProtocol with SprayJsonSupport {

  private[this] val moooleloFlow = Http().outgoingConnection(config.host, config.port)

  private[this] def moooleloManager(request: HttpRequest): Future[HttpResponse] = {
    Source.single(request).via(moooleloFlow).runWith(Sink.head)
  }

  def register(request: RegistrationRequest): Future[Unit] = {
    moooleloManager(RequestBuilding.Post("/services", request)) flatMap { response =>
      response.status match {
        case sc if sc.isSuccess() =>
          Future.successful()
        case sc =>
          val msg = s"Unable to register in Mooolelo server (${config.host}:${config.port}), received: ${sc.reason()}, ${sc.reason()}"
          logger.error(msg)
          Future.failed(new IOException(msg))
      }
    }
  }

  def heartbeat(request: HeartbeatRequest): Future[Unit] = {
    moooleloManager(RequestBuilding.Post(s"/services/${request.serviceType}/${request.group}/${request.id}", request)) flatMap { response =>
      response.status match {
        case sc if sc.isSuccess() =>
          Future.successful()
        case sc =>
          val msg = s"Unable to send heartbeat to Mooolelo server (${config.host}:${config.port}), received: ${sc.reason()}, ${sc.reason()}"
          logger.error(msg)
          Future.failed(new IOException(msg))
      }
    }
  }
}