# Getting Started

This guide will help you set up **Naryo** on your local machine for development and testing.

## Prerequisites

Before running Naryo, ensure you have the following installed:

- **Java 21**
- **Maven**
- **Docker & Docker Compose** (for containerized deployment)

## Building the Project

1. Clone the repository and navigate to the project directory:

    ```sh
    git clone https://github.com/LF-Decentralized-Trust-labs/naryo
    cd naryo
    ```

2. Compile, test, and package the project using Maven:

    ```sh
    mvn clean package
    ```

## Running Naryo

### Running with Docker Compose (Recommended)

Naryo provides a **docker-compose** setup that includes all required dependencies. There are two ways to run it:

#### Option 1: Using the setup script

1. Navigate to the `server` directory:

    ```sh
    cd server
    ```

2. Make the setup script executable and run it with the desired configurations:

    ```sh
    chmod +x setup_env.sh
    ./setup_env.sh <broker> <database> <blockchain>
    ```

   This script automatically configures and starts the required services without needing to run `docker-compose` manually.

#### Option 2: Manually building Naryo and running Docker Compose

1. Navigate to the `server` directory:

    ```sh
    cd server
    ```

2. Build Naryo's Docker image and start it using `docker-compose` with the desired profiles:

    ```sh
    docker-compose --profile <profile-name> --profile <profile-name> up --build
    ```

   Replace `<profile-name>` with the desired profile:

    - `kafka` → Uses Kafka for messaging
    - `rabbitmq` → Uses RabbitMQ for messaging
    - `mongodb` → Uses Mongo for database
    - `postgresql` → Uses Postgres for database
    - `naryo` → Use Naryo as a Docker service with a pre-built image

3. To stop the services, use:

    ```sh
    docker-compose down
    ```

### Running as a JAR File (Alternative)

If you prefer to run Naryo manually and have an existing instance of **MongoDB, Kafka, Zookeeper, and an Ethereum node**, you can start it as a JAR file:

```sh
cd server
SPRING_PROFILES_ACTIVE=kafka,mongodb,ethereum java -jar target/naryo-server.jar
```

## Next Steps

- [Configuration](configuration.md)
- [Usage](usage.md)
- [Metrics](metrics.md)
- [Known Caveats / Issues](issues.md)
