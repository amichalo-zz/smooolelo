package com.amichalo.smooolelo.config

import com.typesafe.config.Config

import scala.util.Random

trait MoooleloServiceConfig {
  def serviceId: Int
  def serviceType: String
  def serviceGroup: String
  def environment: String
  def servicePort: Option[Int]
}

object MoooleloServiceConfig {
  def fromConfig(config: Config, port: Option[Int] = None, _serviceId: Option[Int] = None): MoooleloServiceConfig = new MoooleloServiceConfig {
    override val serviceGroup: String = config.getString("service.group")
    override val serviceType: String = config.getString("service.type")
    override val environment: String = config.getString("service.environment")

    override val serviceId: Int = {
      if (config.hasPath("service.id")) config.getInt("service.id")
      else _serviceId.getOrElse(Random.nextInt())
    }

    override val servicePort: Option[Int] = port
  }
}