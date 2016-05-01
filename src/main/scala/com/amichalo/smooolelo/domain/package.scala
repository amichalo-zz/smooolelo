package com.amichalo.smooolelo

package object domain {

  case class HealthStatus(isHealthy: Boolean, reason: Option[String])
}
