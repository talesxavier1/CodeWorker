package com.br.tx.Groovy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.tx.Models.InitModel;
import com.google.gson.Gson;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

public class GroovyBase {
	static final Logger logger = LogManager.getLogger(GroovyBase.class);

	private File scriptFolder;
	private File libFolder;
	private File mainGroovyScriptFile;
	private Object groovyMessage;
	private PrintStream originalOut;
	private PrintStream originalErr;

	private boolean everythingOk = true;

	public GroovyBase(InitModel initModel) {
		validAndSetScriptPathFile(initModel.getScriptFolderDir());
		if (initModel.isHaveLib()) {
			validAndSetLibPathFile(initModel.getLibFolderDir());
		}
		validAndSetMainGroovyScriptFile(initModel.getMainScriptName());

		Gson gson = new Gson();
		this.groovyMessage = gson.fromJson(initModel.getJsonPayload(),
				initModel.getMainScriptMessageClass().getClass());

		this.originalOut = System.out;
		this.originalErr = System.err;
	}

	private void printcustomAction(String log) {
		this.originalOut.print("\n printcustomAction \n" + log + "\n");
	}

	private void setCustomPrintAction() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream customPrintStream = new PrintStream(outputStream) {
			@Override
			public void print(String s) {
				super.print(s);
				printcustomAction(s);
			}

			@Override
			public void println(String s) {
				super.println(s);
				printcustomAction(s);
			}

			@Override
			public PrintStream printf(String format, Object... args) {
				super.printf(format, args);
				printcustomAction(String.format(format, args));
				return null;
			}
		};

		System.setOut(customPrintStream);
		System.setErr(customPrintStream);
	}

	private void unsetCustomPrintAction() {
		System.setOut(this.originalOut);
		System.setErr(this.originalErr);
	}

	private GroovyShell mountGroovyShell() {
		Binding binding = new Binding();
		GroovyClassLoader classLoader = new GroovyClassLoader();

		for (File file : this.libFolder.listFiles()) {
			classLoader.addClasspath(file.getPath());
		}
		for (File file : this.scriptFolder.listFiles()) {
			if (!file.getName().equals(this.mainGroovyScriptFile.getName())) {
				classLoader.addClasspath(file.getPath());
			}
		}

		GroovyShell shell = new GroovyShell(classLoader, binding);
		return shell;
	}

	public Object executeScript() {
		if (this.everythingOk == false) {
			logger.warn("Classe GroovyBase carregada com erro. Não será posível executar função 'executeScript'.");
		}

		GroovyShell shell = this.mountGroovyShell();
		Script script;
		try {
			script = shell.parse(this.mainGroovyScriptFile);
		} catch (Exception e) {
			logger.fatal("Erro ao fazer o parse do mainGroovyScriptFile", e);
			return null;
		}

		ExecutorService executor = Executors.newSingleThreadExecutor();
		Callable<Object> invoke = () -> script.invokeMethod("processData", this.groovyMessage);
		Future<Object> future = executor.submit(invoke);
		this.setCustomPrintAction();

		Object result = null;
		try {
			result = future.get(10, TimeUnit.SECONDS);
			System.out.println("Resultado: " + result);
		} catch (TimeoutException e) {
			logger.fatal("Tempo limite de execução atingido. execução finalizada.", e);
			future.cancel(true);
		} catch (ExecutionException e) {
			logger.fatal("Erro ao executar groovy script.", e);
		} catch (Exception e) {
			logger.fatal("Erro desconhecido ao executar groovy script.", e);
		} finally {
			executor.shutdownNow();
			this.unsetCustomPrintAction();
		}

		if (result != null) {
			if (result.getClass().getName() != this.groovyMessage.getClass().getName()) {
				String[] arrayLogs = {
						"Script executado, mas classe de retorno não foi a mesma enviada como parâmetro.",
						String.format("Classe enviada como parâmetro:%s", this.groovyMessage.getClass().getName()),
						String.format("Classe de retorno:%s", result.getClass().getName()) };
				logger.fatal(String.join(System.lineSeparator(), arrayLogs));
				return null;
			}
		}

		return result;
	}

	private void validAndSetScriptPathFile(String scriptPath) {
		File folder = new File(scriptPath);
		boolean isValid = validFileFolder(folder);
		if (isValid == false) {
			logger.fatal(String.format("Diretório '%s' não encontrado ou vazio.", scriptPath));
			this.everythingOk = false;
			return;
		}

		this.scriptFolder = folder;
	}

	private void validAndSetLibPathFile(String libPath) {
		File folder = new File(libPath);
		boolean isValid = validFileFolder(folder);
		if (isValid == false) {
			logger.fatal(String.format("Diretório '%s' não encontrado ou vazio.", libPath));
			this.everythingOk = false;
			return;
		}

		this.libFolder = folder;
	}

	private void validAndSetMainGroovyScriptFile(String mainGroovyScriptName) {
		if (everythingOk == false) {
			return;
		}
		File file = new File(this.scriptFolder.getPath() + File.separator + mainGroovyScriptName);
		if (file.exists() == false) {
			logger.fatal(String.format("Diretório '%s' Não possui o arquivo '%s'.", this.scriptFolder.getPath(), mainGroovyScriptName));
			this.everythingOk = false;
			return;
		}
		this.mainGroovyScriptFile = file;
	}

	private boolean validFileFolder(File folder) {
		if (!folder.exists() || !folder.isDirectory()) {
			return false;
		}

		File[] listOfFiles = folder.listFiles();
		if (listOfFiles.length == 0) {
			return false;
		}
		return true;
	}

	public boolean isEverythingOk() {
		return everythingOk;
	}
}
