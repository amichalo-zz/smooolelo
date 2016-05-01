package com.amichalo.smooolelo.config

import com.typesafe.config.Config

trait MoooleloServerConfig {
  def host: String
  def port: Int
}

object MoooleloServerConfig {
  def fromConfig(config: Config): MoooleloServerConfig = new MoooleloServerConfig {
    override def host: String = config.getString("server.host")
    override def port: Int = config.getInt("server.port")
  }
}