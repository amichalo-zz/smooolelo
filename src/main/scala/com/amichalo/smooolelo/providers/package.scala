package com.amichalo.smooolelo

import com.amichalo.smooolelo.domain.HealthStatus

package object providers {

  trait VersionProvider extends Function0[String]
  trait HealthProvider extends Function0[HealthStatus]

  trait InterfaceProvider {
    def hostname: String
    def ip: String
  }

  trait StartupSettingsProvider extends Function0[List[String]]
  trait ConfigurationProvider extends Function0[Map[String, String]]
}
