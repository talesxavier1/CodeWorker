package br.com.tx.worker;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class Main {
	final static String chrootBasePath = "/home/chroot";
	final static String executionPath = "/home/execution";

	public static void main(String[] args) {
		Path sourceDir = Paths.get(chrootBasePath);
		Path targetDir = Paths.get(executionPath);

		try {
			copyDirectory(sourceDir, targetDir);
			System.out.println("Diretório copiado com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void copyDirectory(Path source, Path target) throws IOException {
		Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				Path targetDir = target.resolve(source.relativize(dir));
				if (!Files.exists(targetDir)) {
					Files.createDirectories(targetDir);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Path targetFile = target.resolve(source.relativize(file));
				Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
