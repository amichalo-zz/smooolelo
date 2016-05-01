package com.amichalo.smooolelo.actors

import akka.actor.{Props, Actor}
import com.amichalo.smooolelo.config.MoooleloServiceConfig
import com.amichalo.smooolelo.gateway.MoooleloServerGateway
import com.amichalo.smooolelo.gateway.MoooleloServerGateway.HeartbeatRequest
import com.amichalo.smooolelo.providers.HealthProvider
import com.typesafe.scalalogging.LazyLogging

object HeartbeatActor {
  sealed trait Message
  case object Heartbeat extends Message

  def props(gateway: MoooleloServerGateway, healthProvider: HealthProvider, serviceConfig: MoooleloServiceConfig) =
    Props(new HeartbeatActor(gateway, healthProvider, serviceConfig))
}

class HeartbeatActor(gateway: MoooleloServerGateway, healthProvider: HealthProvider, serviceConfig: MoooleloServiceConfig) extends Actor with LazyLogging {

  private implicit val dispacher = context.dispatcher

  override def receive: Receive = {
    case HeartbeatActor.Heartbeat => handleHeartbeatMessage
  }

  private def handleHeartbeatMessage: Unit = {
    logger.debug("Sending heartbeat to Mooolelo server...")
    val request = HeartbeatRequest(
      id = serviceConfig.serviceId,
      serviceType = serviceConfig.serviceType,
      group = serviceConfig.serviceGroup,
      environment = serviceConfig.environment,
      health = healthProvider.apply()
    )
    gateway.heartbeat(request).onFailure { case t =>
      logger.error("Unable to send heartbeat to Mooolelo server", t)
    }
  }
}