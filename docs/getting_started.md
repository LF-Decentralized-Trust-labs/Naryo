# Getting Started with Naryo

Welcome to Naryo! This guide will walk you through the first steps to get Naryo up and running.

There are three main ways to use Naryo:

1.  **Docker (Easiest)**: Run the Naryo reference server in a Docker container. This is the fastest way to see Naryo in action.
2.  **Spring Boot (Recommended)**: Embed Naryo into your Spring Boot application. This gives you a balance of power and ease of use.
3.  **Core Library (Advanced)**: Use the core Naryo library in any Java application. This gives you maximum control but requires manual setup.

## Prerequisites

-   Java 21 or higher
-   Docker (if you want to use the Docker-based options)
-   Gradle or Maven

## 1. Running Naryo with Docker

The easiest way to start is by using our Docker-based quickstarts. These guides will show you how to run Naryo with a local blockchain node in minutes.

-   [Naryo with Besu Quickstart](./tutorials/naryo_quickstart.md)
-   [Naryo with Hedera Quickstart](./tutorials/naryo_quickstart_hedera.md)

## 2. Using Naryo with Spring Boot (Recommended)

This is the recommended way to use Naryo in your own application.

### Step 1: Add Naryo as a Dependency

To add Naryo to your project, you need to configure your build tool to download artifacts from GitHub Packages.

<details>
<summary><b>GitHub Packages Authentication</b></summary>

You'll need a GitHub Personal Access Token with the `read:packages` scope. You can create one [here](https://github.com/settings/tokens).

#### For Gradle:

Add the following to your `settings.gradle` or `build.gradle`:

```groovy
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/LF-Decentralized-Trust-labs/Naryo")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
        }
    }
    mavenCentral()
}
```

Then, provide your GitHub credentials in your `gradle.properties` file:

```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.key=YOUR_GITHUB_PERSONAL_ACCESS_TOKEN
```

#### For Maven:

Add the following to your `~/.m2/settings.xml`:

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>${env.GITHUB_USERNAME}</username>
            <password>${env.GITHUB_TOKEN}</password>
        </server>
    </servers>
</settings>
```

And add this repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/LF-Decentralized-Trust-labs/Naryo</url>
    </repository>
</repositories>
```
</details>

Now, add the `spring-core` dependency to your project.

<details>
<summary><b>Gradle Dependency</b></summary>

```groovy
dependencies {
    implementation("io.naryo:spring-core:<version>")
    // ... other dependencies
}
```
</details>

<details>
<summary><b>Maven Dependency</b></summary>

```xml
<dependencies>
    <dependency>
        <groupId>io.naryo</groupId>
        <artifactId>spring-core</artifactId>
        <version>${naryo.version}</version>
    </dependency>
    <!-- ... other dependencies -->
</dependencies>
```
</details>

### Step 2: Configure Naryo

Create an `application.yml` file in your `src/main/resources` directory and configure Naryo. Here is a minimal example:

```yaml
naryo:
  nodes:
    - id: demo
      name: anvil
      type: ETHEREUM
      connection:
        type: HTTP
        endpoint:
          url: http://localhost:8545
  # ... add your filters and broadcasters
```

For a complete list of configuration options, please refer to the [Configuration Documentation](./configuration/index.md).

### Step 3: Start Naryo

In your main Spring Boot application class, implement `InitializingBean` and start Naryo.

```java
@SpringBootApplication
public class Application implements InitializingBean {

    private final NodeInitializer nodeInitializer;

    public Application(NodeInitializer nodeInitializer) {
        this.nodeInitializer = nodeInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NodeContainer container = nodeInitializer.init();
        container.start();
    }
}
```

## 3. Using the Core Library (Advanced)

If you are not using Spring, you can use the `core` library directly. This requires you to manually instantiate and wire all the necessary components.

```java
// 1. Add the core dependency
// (see step 1 above, but use io.naryo:core:<version>)

// 2. Instantiate and wire the components
NodeRunner nodeRunner = new NodeRunner(/* your wiring here */);
NodeContainer container = new NodeContainer(List.of(nodeRunner));

// 3. Start the container
container.start();
```

## Next Steps

Now that you have Naryo running, here are some resources to help you continue your journey:

-   **[Configuration Guide](./configuration/index.md)**: Learn how to configure nodes, filters, and broadcasters.
-   **[Tutorials](./tutorials/index.md)**: Explore more advanced use cases and examples.
-   **[Architecture](./architecture.md)**: Understand the inner workings of Naryo.
