package com.amichalo.smooolelo.config

import com.typesafe.config.Config

import scala.concurrent.duration.FiniteDuration

import ConfigUtils._

trait MoooleloClientConfig {
  def moooleloEndpoint: MoooleloServerConfig
  def registrationInterval: FiniteDuration
  def heartbeatInterval: FiniteDuration
}

object MoooleloClientConfig {
  def fromConfig(config: Config): MoooleloClientConfig = new MoooleloClientConfig {
    override def moooleloEndpoint: MoooleloServerConfig = MoooleloServerConfig.fromConfig(config)
    override def heartbeatInterval: FiniteDuration = config.getFiniteDuration("heartbeat-interval")
    override def registrationInterval: FiniteDuration = config.getFiniteDuration("registration-interval")
  }
}
