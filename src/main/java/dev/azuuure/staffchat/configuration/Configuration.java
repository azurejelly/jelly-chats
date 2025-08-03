package dev.azuuure.staffchat.configuration;

import com.google.common.base.Splitter;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Configuration {

    private final Path path;
    private final String name;

    @Getter
    private ConfigurationNode configurationNode;

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

        this.configurationNode = YamlConfigurationLoader.builder()
                .path(path)
                .build()
                .load();
    }

    private ConfigurationNode getPath(String path) {
        return configurationNode.node(Splitter.on('.').splitToList(path));
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
        String raw = getString(path, path);
        return MiniMessage.miniMessage().deserialize(raw);
    }

    public Component getComponent(String path, TagResolver... resolvers) {
        String raw = getString(path, path);
        return MiniMessage.miniMessage().deserialize(raw, resolvers);
    }
}
