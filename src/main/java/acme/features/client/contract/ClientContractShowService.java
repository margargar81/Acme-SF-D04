
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contract.Contract;
import acme.entities.project.Project;
import acme.roles.Client;

@Service
public class ClientContractShowService extends AbstractService<Client, Contract> {

	@Autowired
	ClientContractRepository clientContractRepository;


	@Override
	public void authorise() {
		boolean status;
		int contractId;
		Contract contract;
		Client client;

		contractId = super.getRequest().getData("id", int.class);
		contract = this.clientContractRepository.findContractById(contractId);

		client = contract != null ? contract.getClient() : null;
		int authClientId = super.getRequest().getPrincipal().getActiveRoleId();
		Client activeClient = this.clientContractRepository.findClientById(authClientId);

		boolean ownerContract = contract.getClient() == activeClient;
		status = contract != null && ownerContract || super.getRequest().getPrincipal().hasRole(client);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.clientContractRepository.findContractById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		int contractId;
		Collection<Project> projects;
		SelectChoices choices;

		if (object.isDraftMode())
			projects = this.clientContractRepository.findAllProjects();
		else {
			contractId = super.getRequest().getData("id", int.class);
			projects = this.clientContractRepository.findProjectByContractId(contractId);

		}

		choices = SelectChoices.from(projects, "code", object.getProject());

		Dataset dataset;

		dataset = super.unbind(object, "code", "providerName", "customerName", "goals", "budget", "draftMode");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);

		super.getResponse().addData(dataset);
	}

}