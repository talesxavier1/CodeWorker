package br.com.tx.config;

import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
	private static final Logger logger = LogManager.getLogger(Config.class);

	public static String tryGetConfig(String key) {

		final String configFilePath = "config.properties";
		Properties properties = getProperties(configFilePath);

		Object result = properties.get(key);
		if (result != null) {
			return (String) result;
		} else {
			return null;
		}
	}

	private static Properties getProperties(String path) {
		InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(path);
		if (inputStream == null) {
			return null;
		}

		try {
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		} catch (Exception e) {
			logger.error("Erro ao carregar inputStream.", e);
			return null;
		}
	}
}