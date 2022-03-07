package lu.ewelo.qmk.oled.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Config {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private Properties properties;

    private Config(String configPath, HashMap<String, Object> defaults) {
        File file = new File(configPath);

        this.properties = new Properties();

        if (!file.exists()) {
            logger.info("Config File not found! Creating default " + file.getName());

            try {
                file.createNewFile();

                properties.putAll(defaults);
                properties.store(new FileOutputStream(file), null);
            } catch (IOException e) {
                logger.error("Could not create Config File", e);
            }
        } else {
            try {
                properties.load(new FileInputStream(file));
                logger.info("Loaded Config File " + file.getName());

                defaults.forEach((s, o) -> {
                    if (!properties.containsKey(s)) {
                        properties.setProperty(s, o.toString());
                        logger.info("Added missing " + s + " property");
                    }
                });

                properties.store(new FileOutputStream(file), null);
            } catch (IOException e) {
                logger.error("Could not load Config File", e);
            }
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public String getString(String key) {
        return get(key);
    }

    public static class ConfigBuilder {
        private HashMap<String, Object> defaults = new HashMap<>();
        private String path;

        public ConfigBuilder addDefault(String key, Object value) {
            defaults.put(key, value);
            return this;
        }

        public ConfigBuilder setPath(String path) {
            this.path = path;
            return this;
        }

        public Config build() {
            if (path != null && !path.isBlank()) {
                return new Config(path, defaults);
            } else {
                logger.error("The path was not set or is blank!");
                throw new IllegalStateException("Missing Config Path");
            }
        }
    }

}
