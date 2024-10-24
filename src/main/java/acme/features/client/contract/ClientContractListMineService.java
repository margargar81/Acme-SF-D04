
package acme.features.client.contract;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.roles.Client;

@Service
public class ClientContractListMineService extends AbstractService<Client, Contract> {

	// Internal State ---------------------------------------------------------------------------------------------------------------------------------------

	@Autowired
	ClientContractRepository clientContractRepository;

	// AbstractService interface ----------------------------------------------------------------------------------------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Contract> contracts;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		contracts = this.clientContractRepository.findAllContractsByClientId(principal.getActiveRoleId());

		super.getBuffer().addData(contracts);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "providerName", "customerName", "goals", "budget", "project.code", "draftMode");

		if (object.isDraftMode()) {
			final Locale local = super.getRequest().getLocale();

			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");
		} else
			dataset.put("draftMode", "No");

		super.getResponse().addData(dataset);
	}

}
