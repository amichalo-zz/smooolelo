package com.amichalo.smooolelo.protocol

import com.amichalo.smooolelo.domain.HealthStatus
import com.amichalo.smooolelo.gateway.MoooleloServerGateway.{HeartbeatRequest, RegistrationRequest}
import spray.json.DefaultJsonProtocol

trait SMoooleloJsonProtocol extends DefaultJsonProtocol {

  implicit val healthStatusFormat = jsonFormat2(HealthStatus)
  implicit val registrationRequestFormat = jsonFormat10(RegistrationRequest)
  implicit val heartbeatRequestFormat = jsonFormat5(HeartbeatRequest)
}
