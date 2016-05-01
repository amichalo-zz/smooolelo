package com.amichalo.smooolelo.actors

import akka.actor.{Props, Actor}
import com.amichalo.smooolelo.config.MoooleloServiceConfig
import com.amichalo.smooolelo.gateway.MoooleloServerGateway
import com.amichalo.smooolelo.gateway.MoooleloServerGateway.RegistrationRequest
import com.amichalo.smooolelo.providers.SMoooleloDataProvider
import com.typesafe.scalalogging.LazyLogging

object RegistrationActor {
  sealed trait Message
  case object Register extends Message

  def props(gateway: MoooleloServerGateway, dataProvider: SMoooleloDataProvider, serviceConfig: MoooleloServiceConfig) =
    Props(new RegistrationActor(gateway, dataProvider, serviceConfig))
}

class RegistrationActor(gateway: MoooleloServerGateway, dataProvider: SMoooleloDataProvider, serviceConfig: MoooleloServiceConfig) extends Actor with LazyLogging {

  private implicit val dispacher = context.dispatcher

  override def receive: Receive = {
    case RegistrationActor.Register => onRegisterMessage
  }

  def onRegisterMessage: Unit = {
    logger.debug("Sending registration request to Mooolelo server...")
    val request = RegistrationRequest(
      id = serviceConfig.serviceId,
      serviceType = serviceConfig.serviceType,
      group = serviceConfig.serviceGroup,
      environment = serviceConfig.environment,
      hostname = dataProvider.interfaceProvider.hostname,
      ip = dataProvider.interfaceProvider.ip,
      port = serviceConfig.servicePort,
      health = dataProvider.healthProvider.apply(),
      version = dataProvider.versionProvider.map(_.apply())
    )
    gateway.register(request).onFailure { case t =>
      logger.error("Unable to register in Mooolelo server", t)
    }
  }
}