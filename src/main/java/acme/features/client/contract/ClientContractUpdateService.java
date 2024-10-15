
package acme.features.client.contract;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contract.Contract;
import acme.entities.project.Project;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Client;

@Service
public class ClientContractUpdateService extends AbstractService<Client, Contract> {

	@Autowired
	ClientContractRepository clientContractRepository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Contract contract;
		Client client;

		masterId = super.getRequest().getData("id", int.class);
		contract = this.clientContractRepository.findContractById(masterId);
		client = contract != null ? contract.getClient() : null;
		status = contract != null && contract.isDraftMode() && super.getRequest().getPrincipal().hasRole(client);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract contract;
		int id;

		id = super.getRequest().getData("id", int.class);
		contract = this.clientContractRepository.findContractById(id);

		super.getBuffer().addData(contract);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.clientContractRepository.findProjectById(projectId);

		super.bind(object, "code", "providerName", "customerName", "goals", "budget");
		object.setProject(project);
	}

	@Override
	public void validate(final Contract object) {

		assert object != null;

		this.validateBudget(object);

	}

	@Override
	public void perform(final Contract object) {
		assert object != null;
		this.clientContractRepository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Collection<Project> projects;
		SelectChoices choices;

		projects = this.clientContractRepository.findAllProjects();
		choices = SelectChoices.from(projects, "code", object.getProject());

		Dataset dataset;

		dataset = super.unbind(object, "code", "providerName", "customerName", "goals", "budget");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);

		super.getResponse().addData(dataset);
	}

	private void validateBudget(final Contract object) {

		Project project = object.getProject();
		Money projectCost = project.getCost();

		if (!super.getBuffer().getErrors().hasErrors("budget")) {
			super.state(object.getBudget().getAmount() < projectCost.getAmount(), "budget", "client.contract.form.error.max-budget");
			super.state(object.getBudget().getAmount() > 0, "budget", "client.contract.form.error.negative-amount");

			if (project != null)
				super.state(object.getBudget().getCurrency().equals(object.getProject().getCost().getCurrency()), "budget", "client.contract.form.error.different-currency");

			List<SystemConfiguration> sc = this.clientContractRepository.findSystemConfiguration();
			final boolean foundCurrency = Stream.of(sc.get(0).acceptedCurrencies.split(",")).anyMatch(c -> c.equals(object.getBudget().getCurrency()));

			super.state(foundCurrency, "budget", "client.contract.form.error.currency-not-suported");
		}
	}

}
