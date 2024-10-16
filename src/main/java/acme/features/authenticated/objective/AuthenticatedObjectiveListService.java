
package acme.features.authenticated.objective;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.objective.Objective;

@Service
public class AuthenticatedObjectiveListService extends AbstractService<Authenticated, Objective> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedObjectiveRepository authenticatedObjectiveRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		/*
		 * El usuario debe estar logueado
		 */
		boolean status;

		status = super.getRequest().getPrincipal().isAuthenticated();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Objective> objects;

		objects = this.authenticatedObjectiveRepository.findAllObjectives();

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
