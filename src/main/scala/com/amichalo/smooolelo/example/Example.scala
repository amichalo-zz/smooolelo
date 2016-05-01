package com.amichalo.smooolelo.example

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.amichalo.smooolelo.MoooleloClient
import com.amichalo.smooolelo.config.{MoooleloClientConfig, MoooleloServiceConfig}
import com.amichalo.smooolelo.domain.HealthStatus
import com.amichalo.smooolelo.providers.{BasicInterfaceProvider, HealthProvider, SMoooleloDataProvider}
import com.typesafe.config.ConfigFactory

object Example extends App {
  implicit val actorSystem = ActorSystem("example")
  implicit val materializer = ActorMaterializer()
  implicit val dispacher = actorSystem.dispatcher

  val config = ConfigFactory.load()

  val serviceConfig = MoooleloServiceConfig.fromConfig(config.getConfig("mooolelo"))
  val clientConfig = MoooleloClientConfig.fromConfig(config.getConfig("mooolelo"))

  val dataProvider = new SMoooleloDataProvider(
    healthProvider = new HealthProvider { override def apply(): HealthStatus = HealthStatus(true, None) },
    interfaceProvider = new BasicInterfaceProvider("wlan0")
  )

  val client = new MoooleloClient(clientConfig, serviceConfig, dataProvider)

  client.start()

}
