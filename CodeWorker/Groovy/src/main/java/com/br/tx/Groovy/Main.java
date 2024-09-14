package com.br.tx.Groovy;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.tx.Models.InitModel;
import com.google.gson.Gson;

public class Main {
	// static final Logger logger = ConfigLogger.getLogger(Main.class);
	static final Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
		System.out.println("Class Loader: " + Main.class.getClassLoader());
		System.out.println("Classpath: " + System.getProperty("java.class.path"));
		logger.warn("teste");
		Map<String, String> env = System.getenv();
		String initData = env.get("INIT_DATA");
		InitModel initModel = parseAndValidInitModel(initData);
		if (initModel == null) {
			logger.warn(String.format("Não foi possível fazer o parse do initData. %s Porcesso finalizado.", System.lineSeparator()));
			return;
		}

		GroovyBase groovyBase = new GroovyBase(initModel);
		if (groovyBase.isEverythingOk() == false) {
			logger.warn(String.format("Não foi possivel montar classe base para execução do groovy. %s Porcesso finalizado.", System.lineSeparator()));
			return;
		}

		Object result = groovyBase.executeScript();
		if (result == null) {
			logger.warn("Execução do script retornou 'null'. Processo finalizado.");
			return;
		}

		String strResult = new Gson().toJson(result);

		String[] arrayFinalStrMessage = {
				"<FINAL_MESSAGE_STR_RESULT>",
				strResult,
				"</FINAL_MESSAGE_STR_RESULT>"
		};
				
		System.out.println(String.join(System.lineSeparator(), arrayFinalStrMessage));

	}

	private static InitModel parseAndValidInitModel(String jsonString) {
		InitModel finalInitModel = null;
		try {
			InitModel initModel = new Gson().fromJson(jsonString, InitModel.class);
			String resultValidateModel = initModel.validModel();
			if (resultValidateModel != null) {
				String[] arrayLogs = {
						"Valor passado para variável de ambiente 'INIT_DATA' não é valida.",
						String.format("Valor passado: '%s'", jsonString),
						String.format("Mensagem da validação: '%s'", resultValidateModel)
				};

				logger.fatal(String.join(System.lineSeparator(), arrayLogs));
			} else {
				finalInitModel = initModel;
			}
		} catch (Exception e) {
			String[] arrayLogs = {
					"Erro ao Desserializar o initModel.",
					String.format("Valor passado: '%s'", jsonString)
			};

			logger.fatal(String.join(System.lineSeparator(), arrayLogs), e);
		}
		return finalInitModel;
	}
}
