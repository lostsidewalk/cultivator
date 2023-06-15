# GrowMate

GrowMate is a smart gardening system that enables monitoring and control of various environmental factors in a garden. It uses sensors to gather data and actuators to perform actions based on predefined rules. It leverages the Drools rule engine to enable flexible and customizable automation in various domains.

## Features

- Monitor multiple environmental factors, including air quality, light, relative humidity, reservoir pH, soil conditions, and temperature.
- Control actuators to automate actions such as adjusting lighting, irrigation, and ventilation.
- Rule-based engine for evaluating sensor data and triggering appropriate actions.
- Integration with GPIO pins using the Pi4J library for Raspberry Pi.

<hr />

## Getting Started

These instructions will guide you on how to set up and run the GrowMate project on your Raspberry Pi.

### Prerequisites

- Raspberry Pi board
- Java Development Kit (JDK) 8 or later installed on the Raspberry Pi
- Pi4J library installed on the Raspberry Pi
- Other dependencies as specified in the project

### Installation

1. Clone the repository to your Raspberry Pi:

git clone https://github.com/your-username/growmate.git

2. Build the project using a build tool of your choice (e.g., Maven):

cd growmate
mvn clean install

### Usage

1. Configure the sensor and actuator definitions in the `application.properties` file or through environment variables.

2. Run the application:

java -jar growmate.jar

3. The application should now be running on `http://localhost:8080`.

4. Monitor the logs to observe sensor readings and rule evaluations.

<hr />

## REST API

The Growmate application provides a RESTful API for interacting with sensors and actuators.

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

<hr />

## Customization
You can customize the behavior of the GrowMate application by modifying the rules defined in the rules directory. These rules dictate how the sensor data is evaluated and what actions are triggered on the actuators. Feel free to adapt the rules to suit your specific use case.

<hr />

## Contributing
Contributions are welcome! If you find any issues or would like to suggest enhancements, please open an issue or submit a pull request.

<hr />

### License

This project is licensed under the [MIT License](LICENSE).

### Acknowledgments

- [Pi4J](https://pi4j.com/) - Java library for Raspberry Pi GPIO control
- [Spring Boot](https://spring.io/projects/spring-boot) - Java framework for building applications

