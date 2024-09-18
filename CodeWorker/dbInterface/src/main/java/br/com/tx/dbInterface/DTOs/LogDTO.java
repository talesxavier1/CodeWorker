package br.com.tx.dbInterface.DTOs;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LogDTO(
	@NotNull
	Date creationDate,
	@NotNull
	String processID,
	@NotNull
	String registryId,
	@NotNull
	@NotBlank
	String content
) {}