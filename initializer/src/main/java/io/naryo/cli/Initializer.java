package io.naryo.cli;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import picocli.CommandLine;

@CommandLine.Command(
        name = "init",
        mixinStandardHelpOptions = true,
        description =
                """
            Init Naryo's project structure.

            Example:
              init --name my-app --modules spring,spring-kafka --output .
            """)
public final class Initializer implements Callable<Integer> {

    private static final String TEMPLATE_BUILD_GRADLE = "templates/build.gradle.mustache";
    private static final String TEMPLATE_APPLICATION = "templates/application.java.mustache";
    private static final String TEMPLATE_CONFIGURATION_YAML = "templates/application.yml.mustache";

    @CommandLine.Option(
            names = {"-n", "--name"},
            description = "Project name",
            required = true)
    private String name;

    @CommandLine.Option(
            names = "--modules",
            split = ",",
            description = "Comma-separated list of modules to include")
    private List<String> modules = new ArrayList<>();

    @CommandLine.Option(
        names = {"-o", "--output"},
        description = "Directory where the project will be created (default: current directory)")
    private Path outputDir = Path.of(".");

    private Map<String, ModuleMetadata> moduleRegistry;

    @Override
    public Integer call() throws Exception {
        loadModuleRegistry();
        validateModules();

        MustacheFactory mf = new DefaultMustacheFactory();

        Path projectDir = getOutputDir().resolve(name);
        String basePackage = ("com.naryo." + name.replaceAll("[^a-zA-Z0-9]", "").toLowerCase());
        String packagePath = basePackage.replace('.', '/');

        createDirectories(projectDir, packagePath);
        generateBuildGradle(mf, projectDir);
        generateApplicationClass(mf, projectDir, basePackage, packagePath);
        generateConfigurationYaml(mf, projectDir);

        System.out.println("✅ Project created at: " + projectDir.toAbsolutePath());
        return 0;
    }

    private void loadModuleRegistry() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("modules.json")) {
            if (is == null) {
                throw new IOException("❌ modules.json not found in resources");
            }
            ModuleList moduleList = mapper.readValue(is, ModuleList.class);
            moduleRegistry =
                    moduleList.modules().stream()
                            .collect(Collectors.toMap(ModuleMetadata::id, m -> m));
        }
    }

    private void validateModules() {
        Set<String> knownIds = moduleRegistry.keySet();
        Set<String> requiredIds =
                moduleRegistry.values().stream()
                        .filter(ModuleMetadata::required)
                        .map(ModuleMetadata::id)
                        .collect(Collectors.toSet());

        List<String> unknown = modules.stream().filter(m -> !knownIds.contains(m)).toList();

        List<String> duplicatedRequired = modules.stream().filter(requiredIds::contains).toList();

        if (!unknown.isEmpty()) {
            throw new IllegalArgumentException("❌ Unknown modules: " + String.join(", ", unknown));
        }

        if (!duplicatedRequired.isEmpty()) {
            System.out.println(
                    "ℹ️ Note: the following required modules are always included automatically and do not need to be specified: "
                            + String.join(", ", duplicatedRequired));
        }
    }

    private List<Map<String, String>> resolveDependencies() {
        return moduleRegistry.values().stream()
                .filter(m -> m.required() || modules.contains(m.id()))
                .map(
                        m ->
                                Map.of(
                                        "groupId", m.groupId(),
                                        "artifactId", m.artifactId(),
                                        "version", m.version()))
                .toList();
    }

    private void createDirectories(Path base, String packagePath) throws IOException {
        Files.createDirectories(base.resolve("src/main/java/" + packagePath));
        Files.createDirectories(base.resolve("src/test/java/" + packagePath));
    }

    private void generateBuildGradle(MustacheFactory mf, Path projectDir) throws IOException {
        Mustache mustache = mf.compile(TEMPLATE_BUILD_GRADLE);
        Map<String, Object> context = new HashMap<>();
        context.put("dependencies", resolveDependencies());
        context.put("name", name);

        try (Writer writer = new FileWriter(projectDir.resolve("build.gradle").toFile())) {
            mustache.execute(writer, context).flush();
        }
    }

    private void generateApplicationClass(
            MustacheFactory mf, Path projectDir, String basePackage, String packagePath)
            throws IOException {
        Mustache mustache = mf.compile(TEMPLATE_APPLICATION);
        Map<String, Object> context = new HashMap<>();
        context.put("package", basePackage);

        Path outputFile = projectDir.resolve("src/main/java/" + packagePath + "/Application.java");
        try (Writer writer = new FileWriter(outputFile.toFile())) {
            mustache.execute(writer, context).flush();
        }
    }

    private void generateConfigurationYaml(MustacheFactory mf, Path projectDir) throws IOException {
        Mustache mustache = mf.compile(TEMPLATE_CONFIGURATION_YAML);
        Map<String, Object> context = new HashMap<>();
        context.put("nodeId", UUID.randomUUID());
        context.put("nodeName", String.format("%s-%s", name, UUID.randomUUID()));
        context.put("broadcasterId", UUID.randomUUID());
        context.put("filterId", UUID.randomUUID());

        Path outputFile = projectDir.resolve("src/main/resources/application.yml");
        Files.createDirectories(outputFile.getParent());
        try (Writer writer = new FileWriter(outputFile.toFile())) {
            mustache.execute(writer, context).flush();
        }
    }

    private Path getOutputDir() {
        String expanded = outputDir.toString().replaceFirst("^~", System.getProperty("user.home"));
        return Path.of(expanded).toAbsolutePath().normalize();
    }

    public record ModuleMetadata(
            String id,
            String groupId,
            String artifactId,
            String version,
            String description,
            boolean required) {}

    public record ModuleList(List<ModuleMetadata> modules) {}

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Initializer()).execute(args);
        System.exit(exitCode);
    }
}
