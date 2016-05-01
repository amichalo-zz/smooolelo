# smooolelo

### About

SMooolelo is a Scala client to [Mooolelo](https://github.com/amichalo/mooolelo) services registry.

Although this project is being used in production environment it's still in a very early stage, so please report bugs through GitHub issues.

### Requirements

SMooolelo uses [Akka](http://akka.io/) to schedule requests sent to [Mooolelo](https://github.com/amichalo/mooolelo) and AkkaHttp as a HTTP client. SMooolelo is not creating an actor system on it's own. Instead of that actor system is being *implicitly* passed during client creation giving to the user full control.

### Configuration

Using SMooolelo client you would like to have an entry in your application looking more or less like this one:
```
mooolelo {

  service {
    id = 1
    type = "default-type"
    group = "default-group"
    environment = "dev"
  }

  server {
    host = "127.0.0.1"
    port = 8000
  }

  heartbeat-interval = 10 seconds
  registration-interval = 2 minutes
}
```
Important here is that `mooolelo.service.id` is optional. If you won't specify this path in your configuration, client will pick a random integer as a service id.

### Usage

A full example can be found in `com.amichalo.smooolelo.example`.

```Scala
  val config = ConfigFactory.load()

  // Service config contains all Metadata about the service like id, type, group, env
  val serviceConfig = MoooleloServiceConfig.fromConfig(config.getConfig("mooolelo"))

  // Client config contains Mooolelo endpoint, refresh intervals
  val clientConfig = MoooleloClientConfig.fromConfig(config.getConfig("mooolelo"))

  // DataProvider provides data about our service during the runtime
  val dataProvider = new SMoooleloDataProvider(
    // Yes... our service is always healthy...
    healthProvider = new HealthProvider { override def apply(): HealthStatus = HealthStatus(true, None) },
    // We would like yo provide ip associated with wlan0 interface, it will also give you a hostname
    interfaceProvider = new BasicInterfaceProvider("wlan0")
  )

  // Create a client ActorSystem, ActorMaterializer, ExecutionContext are being passed implicitly
  val client = new MoooleloClient(clientConfig, serviceConfig, dataProvider)

  // When you are ready start the client, it will send first registration requests and schedule others
  client.start()
```

### License

For licensing info see LICENSE file in project's root directory.
