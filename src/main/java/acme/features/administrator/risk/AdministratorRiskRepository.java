
package acme.features.administrator.risk;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.risk.Risk;

@Repository
public interface AdministratorRiskRepository extends AbstractRepository {

	@Query("select r from Risk r where r.id = :id")
	Risk findRiskById(int id);

	@Query("select r from Risk r where r.reference = :reference")
	Risk findRiskByReferece(String reference);

	@Query("select r from Risk r")
	Collection<Risk> findAllRisks();

}
