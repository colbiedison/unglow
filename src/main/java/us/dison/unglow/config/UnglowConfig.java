package us.dison.unglow.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class UnglowConfig {

    private static final String COMMENT = "Configuration file for Unglow";

    private final Path configPath;

    private boolean enabled = true;

    public UnglowConfig(Path configPath) {
        this.configPath = configPath;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void load() throws IOException {
        if (!Files.exists(configPath)) return;

        Properties properties = new Properties();
        properties.load(Files.newInputStream(configPath));
        this.enabled = !properties.getProperty("enabled").equals("false");
    }

    public void save() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("enabled", enabled ? "true" : "false");
        properties.store(Files.newOutputStream(configPath), COMMENT);
    }
}
