package com.amichalo.smooolelo.config

import com.typesafe.config.Config

import scala.concurrent.duration._

object ConfigUtils {

  implicit class FiniteDurationReader(config: Config) {
    def getFiniteDuration(path: String): FiniteDuration = {
      val millis = config.getDuration(path, java.util.concurrent.TimeUnit.MILLISECONDS)
      FiniteDuration(millis, MILLISECONDS)
    }
  }

}
