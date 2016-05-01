package com.amichalo.smooolelo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.amichalo.smooolelo.actors.{HeartbeatActor, RegistrationActor}
import com.amichalo.smooolelo.config.{MoooleloClientConfig, MoooleloServiceConfig}
import com.amichalo.smooolelo.gateway.MoooleloServerGateway
import com.amichalo.smooolelo.providers.SMoooleloDataProvider
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

class MoooleloClient(clientConfig: MoooleloClientConfig, serviceConfig: MoooleloServiceConfig, dataProvider: SMoooleloDataProvider)
                    (implicit actorSystem: ActorSystem, materializer: ActorMaterializer, ec: ExecutionContext) extends LazyLogging {

  private val gateway = new MoooleloServerGateway(clientConfig.moooleloEndpoint)
  private val registrationActor = actorSystem.actorOf(RegistrationActor.props(gateway, dataProvider, serviceConfig))
  private val heartbeatActor = actorSystem.actorOf(HeartbeatActor.props(gateway, dataProvider.healthProvider, serviceConfig))

  def start() = {
    logger.info("Starting Mooolele client...")
    actorSystem.scheduler.schedule(0 seconds, clientConfig.registrationInterval, registrationActor, RegistrationActor.Register)
    actorSystem.scheduler.schedule(clientConfig.heartbeatInterval, clientConfig.heartbeatInterval, heartbeatActor, HeartbeatActor.Heartbeat)
  }
}


