
package acme.features.client.progressLogs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.Contract;
import acme.entities.progressLog.ProgressLog;
import acme.roles.Client;

@Repository
public interface ClientProgressLogRepository extends AbstractRepository {

	@Query("select c from Contract c where c.id= :contractId")
	Contract findContractById(int contractId);

	@Query("select pl from ProgressLog pl where pl.contract.id = :contractId")
	Collection<ProgressLog> findManyProgressLogByContractId(int contractId);

	@Query("select pl.contract from ProgressLog pl where pl.id = :id")
	Contract findContractByProgressLogId(int id);

	@Query("select pl from ProgressLog pl where pl.id = :id")
	ProgressLog findProgressLogById(int id);

	@Query("select pl from ProgressLog pl where pl.recordId = :recordId")
	ProgressLog findProgressLogByRecordId(String recordId);

	@Query("select cl from Client cl where cl.id = :clientId")
	Client findClientById(int clientId);

}
