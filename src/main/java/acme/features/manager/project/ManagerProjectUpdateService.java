
package acme.features.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Manager;

@Service
public class ManagerProjectUpdateService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		// The logged user must have the Manager role
		// The project must belong to the logged manager
		// The project must be in draft mode
		boolean status;
		Project project = this.repository.findOneProjectById(super.getRequest().getData("id", int.class));
		Manager manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());

		status = super.getRequest().getPrincipal().hasRole(Manager.class) && project.getManager().equals(manager) && project.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Project object = this.repository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;
		super.bind(object, "code", "title", "projectAbstract", "link", "cost", "indication");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;

		// Validate the uniqueness of the project code
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			int projectId = super.getRequest().getData("id", int.class);
			boolean duplicatedCode = this.repository.findAllProjects().stream().filter(e -> e.getId() != projectId).anyMatch(e -> e.getCode().equals(object.getCode()));

			super.state(!duplicatedCode, "code", "manager.project.form.error.duplicated-code");
		}

		// Validate cost is non-negative and the currency is accepted
		if (!super.getBuffer().getErrors().hasErrors("cost")) {
			Double amount = object.getCost().getAmount();
			super.state(amount >= 0, "cost", "manager.project.form.error.negative-cost");

			SystemConfiguration sc = this.repository.findActualSystemConfiguration();
			String currency = object.getCost().getCurrency();
			super.state(sc.getAcceptedCurrencies().contains(currency), "cost", "manager.project.form.error.not-accepted-currency");
		}
	}

	@Override
	public void perform(final Project object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset = super.unbind(object, "code", "title", "projectAbstract", "link", "cost", "indication", "draftMode");
		super.getResponse().addData(dataset);
	}
}
