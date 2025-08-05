package dev.azuuure.jellychats.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import dev.azuuure.jellychats.configuration.Configuration;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigurationModule extends AbstractModule {

    @Singleton
    @Provides
    public Configuration provideConfiguration(@DataDirectory Path directory) throws IOException {
        return new Configuration(directory, "config.yml");
    }
}
