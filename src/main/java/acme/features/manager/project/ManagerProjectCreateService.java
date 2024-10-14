
package acme.features.manager.project;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Manager;

@Service
public class ManagerProjectCreateService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		// The logged user must be a manager
		boolean status = super.getRequest().getPrincipal().hasRole(Manager.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Manager manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());

		Project project = new Project();
		project.setDraftMode(true);
		project.setManager(manager);

		super.getBuffer().addData(project);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;
		super.bind(object, "code", "title", "projectAbstract", "indication", "cost", "link");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;

		// Validate the uniqueness of the project code
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Optional<Project> existing = this.repository.findOneProjectByCode(object.getCode());
			super.state(existing.isEmpty(), "code", "manager.project.form.error.duplicated-code");
		}

		// Validate the cost is non-negative and the currency is accepted
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

		Dataset dataset = super.unbind(object, "code", "title", "projectAbstract", "indication", "cost", "link", "draftMode");
		super.getResponse().addData(dataset);
	}
}
