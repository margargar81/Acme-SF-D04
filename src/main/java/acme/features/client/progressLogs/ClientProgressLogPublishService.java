
package acme.features.client.progressLogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.progressLog.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogPublishService extends AbstractService<Client, ProgressLog> {

	@Autowired
	private ClientProgressLogRepository clientProgressLogRepository;


	@Override
	public void authorise() {
		boolean status;
		int progressLogId;
		Contract contract;

		progressLogId = super.getRequest().getData("id", int.class);
		contract = this.clientProgressLogRepository.findContractByProgressLogId(progressLogId);
		ProgressLog pl = this.clientProgressLogRepository.findProgressLogById(progressLogId);

		int activeClientId = super.getRequest().getPrincipal().getActiveRoleId();
		Client activeClient = this.clientProgressLogRepository.findClientById(activeClientId);
		boolean clientPl = pl.getContract().getClient() == activeClient;

		status = pl.isDraftMode() && clientPl && contract != null && !contract.isDraftMode() && super.getRequest().getPrincipal().hasRole(contract.getClient());

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
	public void bind(final ProgressLog object) {

		assert object != null;

		int progressLogId;

		progressLogId = super.getRequest().getData("id", int.class);
		Contract contract = this.clientProgressLogRepository.findContractByProgressLogId(progressLogId);

		super.bind(object, "recordId", "completeness", "comment", "responsiblePerson");
		object.setContract(contract);
	}

	@Override
	public void validate(final ProgressLog object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("recordId")) {
			ProgressLog existing;

			existing = this.clientProgressLogRepository.findProgressLogByRecordId(object.getRecordId());
			super.state(existing == null || existing.equals(object), "recordId", "client.progress-log.form.error.duplicated");
		}
	}

	@Override
	public void perform(final ProgressLog object) {
		assert object != null;

		object.setDraftMode(false);
		this.clientProgressLogRepository.save(object);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson");

		dataset.put("masterId", object.getContract().getId());
		dataset.put("draftMode", object.isDraftMode());

		super.getResponse().addData(dataset);
	}

}
