
package acme.features.administrator.objective;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.objective.Objective;

@Service
public class AdministratorObjectiveListService extends AbstractService<Administrator, Objective> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorObjectiveRepository administratorObjectiveRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Objective> objects;

		objects = this.administratorObjectiveRepository.findAllObjectives();

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Objective object) {
		assert object != null;

		Dataset dataset;
		String payload;

		dataset = super.unbind(object, "title", "startDuration", "finishDuration", "priority");
		payload = String.format("%s; %s; %s", object.getDescription(), object.isCriticalStatus(), object.getOptionalLink());
		dataset.put("payload", payload);

		super.getResponse().addData(dataset);
	}

}
