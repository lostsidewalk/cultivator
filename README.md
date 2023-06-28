# Cultivator

Cultivator is a smart gardening system that enables monitoring and control of various environmental factors in a garden. It uses sensors to gather data and actuators to perform actions based on predefined rules. It leverages the Drools rule engine to enable flexible and customizable automation in various domains.

## Features

- Monitor multiple environmental factors, including air quality, light, relative humidity, reservoir pH, soil conditions, and temperature.
- Control actuators to automate actions such as adjusting lighting, irrigation, and ventilation.
- Rule-based engine for evaluating sensor data and triggering appropriate actions.
- Integration with GPIO pins using the Pi4J library for Raspberry Pi.

## Getting Started

These instructions will guide you on how to set up and run the Cultivator project on your Raspberry Pi.

This module comprises the back-end only, which must run on the Raspberry Pi device.  

The front-end, which can run from any network-connected device, is located here: https://github.com/lostsidewalk/cultivator-client.

### Prerequisites

- Raspberry Pi board 
  - [Raspberry Pi 4 Computer Model 4 8 GB SBC](https://www.amazon.com/dp/B0899VXM8F?psc=1&ref=ppx_yo2ov_dt_b_product_details)
  - [Flirc Raspberry Pi 4 Case](https://www.amazon.com/dp/B07WG4DW52?psc=1&ref=ppx_yo2ov_dt_b_product_details)
  - [CanaKit 3.5A Raspberry Pi 4 Power Supply (USB-C)](https://www.amazon.com/dp/B07TYQRXTK?psc=1&ref=ppx_yo2ov_dt_b_product_details)
  - SD card w/adequate storage 
- Java Development Kit (JDK) 8 or later installed on the Raspberry Pi
- Pi4J library installed on the Raspberry Pi
- Other dependencies as specified in the project

### Installation

1. Clone the repository to your Raspberry Pi:

`git clone https://github.com/lostsidewalk/cultivator.git`

2. Build the project using a build tool of your choice (e.g., Maven):

`cd cultivator && mvn clean install`

### Usage

1. Configure the sensor and actuator definitions in the `application.properties` file or through environment variables.
```
#
# plant 1 temp sensor
#
com.lostsidewalk.cultivator.sensor-definitions[0].name=plant1_temp
com.lostsidewalk.cultivator.sensor-definitions[0].type=temp
com.lostsidewalk.cultivator.sensor-definitions[0].pinAddress=11
#
# plant 2 temp sensor
#
com.lostsidewalk.cultivator.sensor-definitions[1].name=plant2_temp
com.lostsidewalk.cultivator.sensor-definitions[1].type=temp
com.lostsidewalk.cultivator.sensor-definitions[1].pinAddress=12
#
# plant 3 temp sensor
#
com.lostsidewalk.cultivator.sensor-definitions[2].name=plant3_temp
com.lostsidewalk.cultivator.sensor-definitions[2].type=temp
com.lostsidewalk.cultivator.sensor-definitions[2].pinAddress=13
#
# fan actuator
#
com.lostsidewalk.cultivator.actuator-definitions[0].name=fan1
com.lostsidewalk.cultivator.actuator-definitions[0].pinAddress=14
```

2. Setup rules:

You can customize the behavior of the Cultivator application by modifying the rules defined in the rules directory. These rules dictate how the sensor data is evaluated and what actions are triggered on the actuators. Feel free to adapt the rules to suit your specific use case.

```
package com.lostsidewalk.cultivator

declare defaultKieSession
end

declare Map
    @typesafe(false)
end

declare Actuator
    @typesafe(false)
end

rule "plant temp GT 25 deg -> fan1 (on)"
when
    $sensorState: Map((this["plant1_temp"] > 25.0) || (this["plant2_temp"] > 25.0) || (this["plant3_temp"] > 25.0))
    $actuator: Actuator(name == "fan1") from $actuators.values()
then
    $actuator.setState(true);
end

rule "plant temp LE 25 deg -> fan1 (off)"
when
    $sensorState: Map((this["plant1_temp"] <= 25.0) && (this["plant2_temp"] <= 25.0) && (this["plant3_temp"] <= 25.0))
    $actuator: Actuator(name == "fan1") from $actuators.values()
then
    $actuator.setState(true);
end
```

3. Run the application:

`java -jar cultivator.jar`

4. The application should now be running on `http://localhost:8080`.

The Cultivator application provides a RESTful API for interacting with sensors and actuators.

5. Monitor the logs to observe sensor readings and rule evaluations.

```
c.l.cultivator.app.Application           : Starting Application using Java 17.0.3 with PID 244250 (/world/src/coldchillinsw/cultivator/build/classes/java/main started by me in /world/src/coldchillinsw/cultivator)
c.l.cultivator.app.Application           : No active profile set, falling back to 1 default profile: "default"
o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
o.apache.catalina.core.StandardService   : Starting service [Tomcat]
o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.8]
o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 823 ms
o.k.m.i.embedder.MavenSettings           : Environment variable M2_HOME is not set
c.l.cultivator.MonitorService            : Cultivator sensors=[[name=plant1_temp,timeout=500], [name=plant2_temp,timeout=500], [name=plant3_temp,timeout=500]]
c.l.cultivator.MonitorService            : Cultivator actuators={fan1=[name=fan1,timeout=500]}
c.l.cultivator.MonitorService            : Starting Cultivator reactor...
o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
c.l.cultivator.app.Application           : Started Application in 3.221 seconds (process running for 3.504)
```

## REST API


### Sensors

Get Sensor Data
- Endpoint: `GET /sensors/current/{sensorName}`
- Description: Retrieves the data for the specified sensor.
- Parameters:
  - `sensorName` - The name ID of the sensor to retrieve data for.
- Response:
  - Status Code: 200 (OK)
  - Body: JSON representation of the sensor data.

### Actuators

Enable Actuator
- Endpoint: `POST /actuators/{actuatorName}/enable`
- Description: Enables the specified actuator.
- Parameters:
  - `actuatorName` - The name ID of the actuator to enable.
- Response:
  - Status Code: 200 (OK)

Disable Actuator
- Endpoint: `POST /actuators/{actuatorName}/disable`
- Description: Disables the specified actuator.
- Parameters:
  - `actuatorName` - The name ID of the actuator to disable.
- Response:
  - Status Code: 200 (OK)

## Contributing
Contributions are welcome! If you find any issues or would like to suggest enhancements, please open an issue or submit a pull request.

### License

This project is licensed under the [MIT License](LICENSE).

### Acknowledgments

- [Pi4J](https://pi4j.com/) - Java library for Raspberry Pi GPIO control
- [Spring Boot](https://spring.io/projects/spring-boot) - Java framework for building applications

