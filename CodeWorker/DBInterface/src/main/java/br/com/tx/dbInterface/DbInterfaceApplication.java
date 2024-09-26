package br.com.tx.dbInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbInterfaceApplication {
	static final Logger logger = LogManager.getLogger(DbInterfaceApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(DbInterfaceApplication.class, args);
	}

}
