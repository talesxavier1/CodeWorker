package com.br.tx.Groovy;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.tx.Models.InitModel;
import com.google.gson.Gson;

public class Main {

	static final Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
		String b64InitData;
		if (args.length > 0) {
			b64InitData = args[0];
		} else {
			logger.warn("initData não passado como args da execução");
			return;
		}

		InitModel initModel = parseAndValidInitModel(b64InitData);
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
			System.exit(1);
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

	private static InitModel parseAndValidInitModel(String b64InitData) {
		String jsonString;
		try {
			byte[] bJsonString = Base64.getDecoder().decode(b64InitData);
			jsonString = new String(bJsonString, StandardCharsets.UTF_8);
		} catch (Exception e) {
			String[] arrayLogs = {
					"Erro ao fazer o decode do b64InitData.",
					String.format("Valor passado: '%s'", b64InitData),
					" "
			};

			logger.fatal(String.join(System.lineSeparator(), arrayLogs), e);
			return null;
		}

		InitModel finalInitModel = null;
		try {
			InitModel initModel = new Gson().fromJson(jsonString, InitModel.class);
			String resultValidateModel = initModel.validModel();
			if (resultValidateModel != null) {
				String[] arrayLogs = {
						"Valor passado para b64InitData não é valido.",
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
					String.format("Valor b64 passado: '%s'", b64InitData),
					String.format("Valor b64 decodificado: '%s'", jsonString),
					" ",
			};

			logger.fatal(String.join(System.lineSeparator(), arrayLogs), e);
		}
		return finalInitModel;
	}
}
