package com.br.tx.Groovy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.tx.Models.InitModel;
import com.google.gson.Gson;

public class Main {
	// static final Logger logger = ConfigLogger.getLogger(Main.class);
	static final Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
		Map<String, String> env = System.getenv();
		String initData = env.get("INIT_DATA");
		InitModel initModel = parseAndValidInitModel(initData);
		if (initModel == null) {
			logger.warn("Processo finalizado por falta de parâmetros.");

			return;
		}

		GroovyBase groovyBase = new GroovyBase(initModel);
	}

	private static InitModel parseAndValidInitModel(String jsonString) {
		InitModel finalInitModel = null;
		try {
			InitModel initModel = new Gson().fromJson(jsonString, InitModel.class);
			String resultValidateModel = initModel.validModel();
			if (resultValidateModel != null) {
				List<String> logList = new ArrayList<String>();
				logList.add("Valor passado para variável de ambiente 'INIT_DATA' não é valida.");
				logList.add(String.format("Valor passado: '%s'", jsonString));
				logList.add(String.format("Mensagem da validação: '%s'", resultValidateModel));

				logger.fatal(String.join(System.lineSeparator(), logList));
			} else {
				finalInitModel = initModel;
			}
		} catch (Exception e) {
			List<String> logList = new ArrayList<String>();
			logList.add("Erro ao Desserializar o initModel.");
			logList.add(String.format("Valor passado: '%s'", jsonString));

			logger.fatal(String.join(System.lineSeparator(), logList), e);
		}
		return finalInitModel;
	}
}
