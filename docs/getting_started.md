## 🚀 Getting Started

To start using **Naryo**, you need to embed and instantiate it manually inside your project. There are **two
main ways** to use this library:

* ✅ **Spring Boot integration (recommended)** — Simplifies setup using Spring context and YAML configuration.
* ⚙️ **Core module (framework-agnostic)** — Gives you full control but requires manual instantiation and wiring.

---

### 1. Setting up GitHub Packages Authentication

Since our artifacts are hosted on GitHub Package Registry, you'll need to configure authentication in your Gradle or
Maven settings. Please refer to
the [GitHub documentation](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens)
to configure the access token.

#### For Gradle:

Add the following to your `settings.gradle` or `build.gradle` file:

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

You can provide your GitHub credentials either through environment variables or Gradle properties in your
`gradle.properties` file:

```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.key=YOUR_GITHUB_PERSONAL_ACCESS_TOKEN
```

> Make sure your GitHub token has the `read:packages` scope.

#### For Maven:

Add the following to your `settings.xml` file:

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

---

### 2. Add the Dependency to Your Project

<details>
<summary>Gradle</summary>

Add Spring Boot plugin and dependency management (recommended setup, see more in [here](https://docs.spring.io/spring-boot/gradle-plugin/managing-dependencies.html)):

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.5.4'
    id 'io.spring.dependency-management' version '1.1.7'
}

ext {
    naryoVersion = "0.0.1" // Replace with the actual version
}

dependencies {
    // Core module
    implementation("io.naryo:core:${naryoVersion}")

    // Spring Boot integration
    implementation("io.naryo:spring-core:${naryoVersion}")

    // Typical Spring Boot starters you may need
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // ...
}
```

</details>

<details>
<summary>Maven</summary>

Add Spring Boot parent for dependency management and starters:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.4</version>
</parent>

<variables>
    <naryoVersion>0.0.1</naryoVersion> // Replace with the actual version
</variables>

<dependencies>
    <!-- Core module -->
    <dependency>
        <groupId>io.naryo</groupId>
        <artifactId>core</artifactId>
        <version>${naryoVersion}</version>
    </dependency>

    <!-- Spring Boot integration -->
    <dependency>
        <groupId>io.naryo</groupId>
        <artifactId>spring-core</artifactId>
        <version>${naryoVersion}</version>
    </dependency>

    <!-- Spring Boot starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

</details>

---

### 3. Usage Options

#### ✅ Using Spring Boot (recommended)

1. Add the Spring Boot plugin and dependency management
   (for Gradle: `org.springframework.boot` and `io.spring.dependency-management`;
   for Maven: use `spring-boot-starter-parent`)
   and include the needed Spring Boot starters (for example, `spring-boot-starter`, `spring-boot-starter-web`) along with Naryo’s spring-core dependency.
2. Define your configuration in `application.yml`:

    ```yaml
    naryo:
      nodes:
        - name: default
    #...
    ```

   Please refer to the [Configuration Documentation](./configuration/index.md) for more details.

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

1. [Configuration Documentation](./configuration/index.md)
2. [Broadcasting Documentation](./broadcasting/index.md)
3. [Tutorials](./tutorials/index.md)
