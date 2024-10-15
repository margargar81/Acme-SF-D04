
package acme.features.any.progressLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.progressLog.ProgressLog;

@Service
public class AnyProgressLogListService extends AbstractService<Any, ProgressLog> {

	@Autowired
	private AnyProgressLogRepository anyProgressLogRepository;


	@Override
	public void authorise() {

		boolean status;
		int masterId;
		Contract contract;

		masterId = super.getRequest().getData("masterId", int.class);
		contract = this.anyProgressLogRepository.findContractById(masterId);

		status = contract != null && !contract.isDraftMode();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<ProgressLog> progressLogs;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		progressLogs = this.anyProgressLogRepository.findManyPublishedProgressLogByMasterId(masterId);

		super.getBuffer().addData(progressLogs);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "recordId", "completeness", "registrationMoment", "responsiblePerson");

		super.getResponse().addData(dataset);
	}

}
