## 🚀 Getting Started

To start using **Naryo**, you need to embed and instantiate it manually inside your project. There are **two
main ways** to use this library:

* ✅ **Spring Boot integration (recommended)** — Simplifies setup using Spring context and YAML configuration.
* ⚙️ **Core module (framework-agnostic)** — Gives you full control but requires manual instantiation and wiring.

> 📦 This library is not yet published to any public registry, so you will need to build and publish it locally first.

---

### 1. Build & Publish Locally

Before adding the dependency, publish the library to your local Maven repository:

```bash
  ./gradlew publishToMavenLocal
```

> This will install the library to your local Maven repository (`~/.m2/repository`).

---

### 2. Add the Dependency to Your Project

<details>
<summary>Gradle</summary>

```groovy
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

ext {
    naryoVersion = "0.0.1" // Replace with the actual version
}

dependencies {
    // Core module
    implementation("io.naryo:new-core:${naryoVersion}")

    // Spring Boot integration
    implementation("io.naryo:core-spring:${naryoVersion}")
}
```

</details>

<details>
<summary>Maven</summary>

```xml

<repositories>
    <repository>
        <id>local</id>
        <url>file://${user.home}/.m2/repository</url>
    </repository>
</repositories>

<variables>
<naryoVersion>0.0.1</naryoVersion>
</variables>

<dependencies>
<!-- Core module -->
<dependency>
    <groupId>io.naryo</groupId>
    <artifactId>new-core</artifactId>
    <version>${naryoVersion}</version>
</dependency>

<!-- Spring Boot integration -->
<dependency>
    <groupId>io.naryo</groupId>
    <artifactId>core-spring</artifactId>
    <version>${naryoVersion}</version>
</dependency>
</dependencies>
```

</details>

---

### 3. Usage Options

#### ✅ Using Spring Boot (recommended)

1. Add the Spring Boot starter dependency (see above).
2. Define your configuration in `application.yml`:

    ```yaml
    naryo:
      nodes:
        - name: default
    #...
    ```

   Please refer to the [Configuration Documentation](./configuration.md) for more details.

3. Start naryo when your application starts:

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

> Spring will automatically parse the YAML and inject dependencies.

#### ⚙️ Using the Core Library (manual setup)

1. Instantiate all required components manually and start them:

    ```java
    NodeRunner nodeRunner = new NodeRunner(...);
    NodeContainer container = new NodeContainer(List.of(nodeRunner));

    container.start();
    ```

2. You are responsible for building and wiring all components. No YAML parsing is provided out-of-the-box.

---

Need help setting it up in your use case? Let us know or check out the [tutorials](./tutorials/index.md).

## 👉 Next Steps

1. [Configuration Documentation](./configuration.md)
2. [Tutorials](./tutorials/index.md)
