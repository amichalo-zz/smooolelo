package com.amichalo.smooolelo.example

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.amichalo.smooolelo.MoooleloClient
import com.amichalo.smooolelo.config.{MoooleloClientConfig, MoooleloServiceConfig}
import com.amichalo.smooolelo.domain.HealthStatus
import com.amichalo.smooolelo.providers._
import com.typesafe.config.ConfigFactory

object Example extends App {
  implicit val actorSystem = ActorSystem("example")
  implicit val materializer = ActorMaterializer()
  implicit val dispacher = actorSystem.dispatcher

  val config = ConfigFactory.load()

  // Service config contains all Metadata about the service like id, type, group, env
  val serviceConfig = MoooleloServiceConfig.fromConfig(config.getConfig("mooolelo"))

  // Client config contains Mooolelo endpoint, refresh intervals
  val clientConfig = MoooleloClientConfig.fromConfig(config.getConfig("mooolelo"))

  // DataProvider provides data about our service during the runtime
  val dataProvider = new SMoooleloDataProvider(
    // Yes... our service is always healthy...
    healthProvider = new HealthProvider { override def apply(): HealthStatus = HealthStatus(true, None) },
    // We would like yo provide ip associated with wlan0 interface, it will also give you a hostname
    interfaceProvider = new BasicInterfaceProvider("wlan0"),
    // We shouldn't pass hardcoded version but... ;)
    versionProvider = Option(new VersionProvider { override def apply(): String = "v0.1"}),
    startupSettingsProvider = Option(new JVMStartupSettingsProvider()),
    workingDirectoryProvider = Option(new BasicWorkingDirectoryProvider())
  )

  // Create a client ActorSystem, ActorMaterializer, ExecutionContext are being passed implicitly
  val client = new MoooleloClient(clientConfig, serviceConfig, dataProvider)

  // When you are ready start the client, it will send first registration requests and schedule others
  client.start()
}
