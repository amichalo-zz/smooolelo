package com.amichalo.smooolelo.providers

import java.lang.management.{ManagementFactory, RuntimeMXBean}

import com.typesafe.scalalogging.LazyLogging

import scala.collection.JavaConversions._

class JVMStartupSettingsProvider extends StartupSettingsProvider with LazyLogging {
  private val runtimeMxBean: RuntimeMXBean = ManagementFactory.getRuntimeMXBean
  private lazy val startupArguments: List[String] = fetchStartupArguments()

  private def fetchStartupArguments(): List[String] = {
    val arguments = runtimeMxBean.getInputArguments.toList
    logger.debug(s"JVM startup arguments: $arguments")
    arguments
  }

  override def apply(): List[String] = startupArguments
}