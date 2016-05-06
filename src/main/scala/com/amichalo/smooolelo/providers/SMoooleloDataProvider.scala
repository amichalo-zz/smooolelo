package com.amichalo.smooolelo.providers

case class SMoooleloDataProvider(
                                healthProvider: HealthProvider,
                                interfaceProvider: InterfaceProvider,
                                versionProvider: Option[VersionProvider] = None,
                                startupSettingsProvider: Option[StartupSettingsProvider] = None,
                                workingDirectoryProvider: Option[WorkingDirectoryProvider] = None
                                )