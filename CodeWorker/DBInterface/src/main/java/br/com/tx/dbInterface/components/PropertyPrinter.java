package br.com.tx.dbInterface.components;

import java.lang.management.ManagementFactory;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class PropertyPrinter {
	@Value("${spring.data.mongodb.uri}")
	private String dbPassword;

	@Value("${jasypt.encryptor.password}")
	private String jasyptPassword;

	@PostConstruct
	public void printPassword() {
		List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
		String strArguments = String.join("|", inputArguments);
		boolean isDebugging = strArguments.contains("-agentlib:jdwp");

		if (isDebugging) {
			System.out.println("Jasypt password: " + jasyptPassword);
			System.out.println("Descriptografado: " + dbPassword);
		}
	}
}
