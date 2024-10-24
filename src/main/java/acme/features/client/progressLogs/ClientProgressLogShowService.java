
package acme.features.client.progressLogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.progressLog.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogShowService extends AbstractService<Client, ProgressLog> {

	@Autowired
	private ClientProgressLogRepository clientProgressLogRepository;


	@Override
	public void authorise() {

		boolean status;
		int progressLogId;
		Contract contract;
		ProgressLog pl;

		progressLogId = super.getRequest().getData("id", int.class);
		pl = this.clientProgressLogRepository.findProgressLogById(progressLogId);
		contract = this.clientProgressLogRepository.findContractByProgressLogId(progressLogId);

		int activeClientId = super.getRequest().getPrincipal().getActiveRoleId();
		Client activeClient = this.clientProgressLogRepository.findClientById(activeClientId);

		boolean clientPl = pl.getContract().getClient() == activeClient;
		status = contract != null && clientPl && !contract.isDraftMode() && super.getRequest().getPrincipal().hasRole(contract.getClient());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProgressLog object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.clientProgressLogRepository.findProgressLogById(id);

		super.getBuffer().addData(object);

	}

	@Override
	public void unbind(final ProgressLog object) {

		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson", "contract", "draftMode");

		dataset.put("masterId", object.getContract().getId());
		//		dataset.put("draftMode", object.isDraftMode());

		super.getResponse().addData(dataset);
	}

}
