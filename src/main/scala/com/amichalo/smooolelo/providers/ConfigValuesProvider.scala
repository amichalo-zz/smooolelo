package com.amichalo.smooolelo.providers

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging

import scala.collection.JavaConversions._

class ConfigValuesProvider(config: Config) extends ConfigurationProvider with LazyLogging {

  private lazy val configuration: Map[String, String] = initialize()
  private def initialize(): Map[String, String] = {
    val map = config.entrySet().map(entry => entry.getKey -> entry.getValue.render()).toMap
    logger.trace(s"Application config values: $map")
    map
  }

  override def apply(): Map[String, String] = configuration
}