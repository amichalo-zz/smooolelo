package com.amichalo.smooolelo.providers

import java.io.File

class BasicWorkingDirectoryProvider extends WorkingDirectoryProvider {

  private def initialize(): String = new File(".").getCanonicalPath

  private lazy val path: String = initialize()

  override def apply(): String = path
}
