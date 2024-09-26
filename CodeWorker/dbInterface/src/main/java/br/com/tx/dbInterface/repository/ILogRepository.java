package br.com.tx.dbInterface.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.tx.dbInterface.models.LogModel;

@Repository
public interface ILogRepository extends MongoRepository<LogModel, UUID> {

}
