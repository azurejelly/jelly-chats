package dev.azuuure.jellychats.core.configuration;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Configuration {

    private final Path path;
    private final String name;

    @Getter
    private ConfigurationNode rootNode;

    @Getter
    private YamlConfigurationLoader configurationLoader;

    public Configuration(Path path, String name) throws IOException {
        this.name = name.endsWith(".yml") ? name : name + ".yml";
        this.path = path.resolve(name);
        this.initialize();
    }

    public Configuration(File file) throws IOException {
        this.path = file.toPath();
        this.name = file.getName();
        this.initialize();
    }

    public void initialize() throws IOException {
        File file = path.toFile();

        if (!file.exists()) {
            Files.createDirectories(path.getParent());

            InputStream res = getClass().getClassLoader().getResourceAsStream(name);
            if (res == null) {
                throw new IOException("Failed to obtain resource " + name);
            }

            try (InputStream stream = new BufferedInputStream(res)) {
                Files.copy(stream, path);
            }
        }

        this.configurationLoader = YamlConfigurationLoader.builder()
                .path(path)
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .build();

        this.rootNode = configurationLoader.load();
    }

    public void reload() throws ConfigurateException {
        if (configurationLoader == null) {
            throw new IllegalStateException("Cannot reload a file that hasn't been loaded yet");
        }

        this.rootNode = configurationLoader.load();
    }

    public void save() throws ConfigurateException {
        if (configurationLoader == null) {
            throw new IllegalStateException("Cannot save a file that hasn't been loaded");
        }

        this.configurationLoader.save(rootNode);
    }

    public void set(String path, Object value) throws SerializationException {
        List<String> list = Arrays.stream(path.split("\\.")).toList();
        rootNode.node(list).set(value);
    }

    public void set(String path, String value) {
        try {
            List<String> list = Arrays.stream(path.split("\\.")).toList();
            rootNode.node(list).set(value);
        } catch (SerializationException e) {
            throw new RuntimeException("This shouldn't happen", e);
        }
    }

    public void set(String path, int value) {
        try {
            List<String> list = Arrays.stream(path.split("\\.")).toList();
            rootNode.node(list).set(value);
        } catch (SerializationException e) {
            throw new RuntimeException("This shouldn't happen", e);
        }
    }

    public void set(String path, double value) {
        try {
            List<String> list = Arrays.stream(path.split("\\.")).toList();
            rootNode.node(list).set(value);
        } catch (SerializationException e) {
            throw new RuntimeException("This shouldn't happen", e);
        }
    }

    public void set(String path, float value) {
        try {
            List<String> list = Arrays.stream(path.split("\\.")).toList();
            rootNode.node(list).set(value);
        } catch (SerializationException e) {
            throw new RuntimeException("This shouldn't happen", e);
        }
    }

    public void set(String path, boolean value) {
        try {
            List<String> list = Arrays.stream(path.split("\\.")).toList();
            rootNode.node(list).set(value);
        } catch (SerializationException e) {
            throw new RuntimeException("This shouldn't happen", e);
        }
    }

    private ConfigurationNode getPath(String path) {
        List<String> list = Arrays.stream(path.split("\\.")).toList();
        return rootNode.node(list);
    }

    public String getString(String path, String def) {
        return getPath(path).getString(def);
    }

    public int getInt(String path) {
        return getPath(path).getInt();
    }

    public int getInt(String path, int def) {
        return getPath(path).getInt(def);
    }

    public boolean getBoolean(String path) {
        return getPath(path).getBoolean();
    }

    public boolean getBoolean(String path, boolean def) {
        return getPath(path).getBoolean(def);
    }

    public Component getComponent(String path) {
        return getComponent(path, path);
    }

    public Component getComponent(String path, TagResolver... resolvers) {
        return getComponent(path, path, resolvers);
    }

    public Component getComponent(String path, String def) {
        String raw = getString(path, def);
        return MiniMessage.miniMessage().deserialize(raw);
    }

    public Component getComponent(String path, String def, TagResolver... resolvers) {
        String raw = getString(path, def);
        return MiniMessage.miniMessage().deserialize(raw, resolvers);
    }
}
