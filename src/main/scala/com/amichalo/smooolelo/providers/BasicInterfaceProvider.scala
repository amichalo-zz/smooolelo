package com.amichalo.smooolelo.providers

import java.net.{InetAddress, NetworkInterface}

import scala.collection.JavaConversions._

object BasicInterfaceProvider {
  val ipPattern = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b".r
}

class BasicInterfaceProvider(interfaceName: String = "eth0") extends InterfaceProvider {

  private val interface: Option[InetAddress] = findInterface()

  private def findInterface(): Option[InetAddress] = {
    import BasicInterfaceProvider._
    val interfaces = NetworkInterface.getByName(interfaceName).getInetAddresses.toIterable

    (for {
      address <- interfaces
      if ipPattern.findFirstIn(address.getHostAddress).isDefined
    } yield address).headOption
  }

  val ip: String = interface.map(_.getHostAddress).getOrElse("unknown")

  val hostname: String = InetAddress.getLocalHost.getHostName
}

class StaticInterfaceProvider(override val ip: String, override val hostname: String) extends InterfaceProvider