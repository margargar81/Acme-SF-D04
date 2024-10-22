
package acme.features.any.progressLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.Contract;
import acme.entities.progressLog.ProgressLog;

@Repository
public interface AnyProgressLogRepository extends AbstractRepository {

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select pl from ProgressLog pl where pl.contract.id = :masterId and pl.draftMode = false")
	Collection<ProgressLog> findManyPublishedProgressLogByMasterId(int masterId);

	@Query("select pl.contract from ProgressLog pl where pl.id = :id")
	Contract findContractByProgressLogId(int id);

	@Query("select pl from ProgressLog pl where pl.id = :id")
	ProgressLog findProgressLogById(int id);

}
