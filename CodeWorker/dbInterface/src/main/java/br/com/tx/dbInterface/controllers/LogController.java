package br.com.tx.dbInterface.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.tx.dbInterface.DTOs.LogDTO;
import br.com.tx.dbInterface.models.LogModel;
import br.com.tx.dbInterface.models.ResponseModel;
import br.com.tx.dbInterface.repository.ILogRepository;
import jakarta.validation.Valid;

@RestController
public class LogController {

	@Autowired
	ILogRepository iLogRepository;

	@PostMapping("/add")
	public ResponseEntity<ResponseModel<LogModel>> addLog(@RequestBody @Valid LogDTO log, Errors errors) {

		if (errors.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel<LogModel>(null, errors.toString()));
		}

		LogModel logModel = new LogModel();
		BeanUtils.copyProperties(log, logModel);
		
		iLogRepository.save(logModel);

		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseModel<LogModel>(logModel, null));
	}
}
