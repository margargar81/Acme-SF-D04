
package acme.features.administrator.objective;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.objective.Objective;
import acme.entities.objective.PriorityType;

@Service
public class AdministratorObjectiveShowService extends AbstractService<Administrator, Objective> {

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
		Objective object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.administratorObjectiveRepository.findObjectiveById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Objective object) {
		assert object != null;

		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(PriorityType.class, object.getPriority());

		dataset = super.unbind(object, "instantiationMoment", "title", "description", "priority", "criticalStatus", "startDuration", "finishDuration", "optionalLink");
		dataset.put("priorities", choices);

		super.getResponse().addData(dataset);
	}

}
