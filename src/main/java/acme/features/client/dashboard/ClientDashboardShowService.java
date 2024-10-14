
package acme.features.client.dashboard;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.progressLog.ProgressLog;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.forms.ClientDashboard;
import acme.roles.Client;

@Service
public class ClientDashboardShowService extends AbstractService<Client, ClientDashboard> {

	@Autowired
	private ClientDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int clientId = super.getRequest().getPrincipal().getActiveRoleId();
		ClientDashboard clientDashboard = new ClientDashboard();

		Collection<ProgressLog> progressLogsPublished = this.repository.findAllProgressLogs().stream().filter(x -> !x.isDraftMode()).toList();

		Collection<Contract> myPublishedContracts = this.repository.findManyContractsByClientId(clientId).stream().filter(x -> !x.isDraftMode()).toList();

		Set<Integer> myContractsIds = myPublishedContracts.stream().map(Contract::getId).collect(Collectors.toSet());

		Collection<Money> myBudgets = this.repository.findManyBudgetsByClientId(clientId);

		Map<String, List<Money>> budgetsByCurrency = myBudgets.stream().collect(Collectors.groupingBy(Money::getCurrency));
		Map<String, Double> mediaPorCurrency = budgetsByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularMedia(entry.getValue()).getAmount()));
		Map<String, Double> maximoPorCurrency = budgetsByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularMaximo(entry.getValue()).getAmount()));
		Map<String, Double> minimoPorCurrency = budgetsByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularMinimo(entry.getValue()).getAmount()));
		Map<String, Double> desviacionPorCurrency = budgetsByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularDesviacion(entry.getValue()).getAmount()));

		List<SystemConfiguration> systemConfiguration = this.repository.findSystemConfiguration();
		if (systemConfiguration == null || systemConfiguration.isEmpty())
			throw new IllegalStateException("System configuration not found.");

		String[] supportedCurrencies = systemConfiguration.get(0).acceptedCurrencies.split(",");
		long totalNumProgressLogBelow25 = progressLogsPublished.stream().filter(x -> myContractsIds.contains(x.getContract().getId())).filter(x -> x.getCompleteness() < 25.0).count();
		long totalNumProgressLogBetween25and50 = progressLogsPublished.stream().filter(x -> myContractsIds.contains(x.getContract().getId())).filter(x -> x.getCompleteness() >= 25.0 && x.getCompleteness() <= 50.0).count();
		long totalNumProgressLogBetween50and75 = progressLogsPublished.stream().filter(x -> myContractsIds.contains(x.getContract().getId())).filter(x -> x.getCompleteness() > 50.0 && x.getCompleteness() <= 75.0).count();
		long totalNumProgressLogAbove75 = progressLogsPublished.stream().filter(x -> myContractsIds.contains(x.getContract().getId())).filter(x -> x.getCompleteness() > 75.0).count();

		clientDashboard.setTotalNumProgressLogBelow25(totalNumProgressLogBelow25);
		clientDashboard.setTotalNumProgressLogBetween25And50(totalNumProgressLogBetween25and50);
		clientDashboard.setTotalNumProgressLogBetween50And75(totalNumProgressLogBetween50and75);
		clientDashboard.setTotalNumProgressLogAbove75(totalNumProgressLogAbove75);

		clientDashboard.setAverageBudgetPerCurrency(mediaPorCurrency);
		clientDashboard.setDeviationBudgetPerCurrency(desviacionPorCurrency);
		clientDashboard.setMinimumBudgetPerCurrency(minimoPorCurrency);
		clientDashboard.setMaximumBudgetPerCurrency(maximoPorCurrency);
		clientDashboard.setSupportedCurrencies(supportedCurrencies);

		super.getBuffer().addData(clientDashboard);
	}

	@Override
	public void unbind(final ClientDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "totalNumProgressLogBelow25", "totalNumProgressLogBetween25And50", //
			"totalNumProgressLogBetween50And75", "totalNumProgressLogAbove75", //
			"maximumPerCurrency", "minimumPerCurrency", "averagePerCurrency", "deviationPerCurrency", "supportedCurrencies");

		super.getResponse().addData(dataset);
	}

	private Money calcularMedia(final Collection<Money> budgets) {
		Money moneyFinal = new Money();
		if (!budgets.isEmpty()) {
			moneyFinal.setCurrency(budgets.iterator().next().getCurrency());
			moneyFinal.setAmount(budgets.stream().mapToDouble(Money::getAmount).average().orElse(0.0));
		}
		return moneyFinal;
	}

	private Money calcularMaximo(final Collection<Money> budgets) {
		Money moneyFinal = new Money();
		if (!budgets.isEmpty()) {
			moneyFinal.setCurrency(budgets.iterator().next().getCurrency());
			moneyFinal.setAmount(budgets.stream().mapToDouble(Money::getAmount).max().orElse(0.0));
		}
		return moneyFinal;
	}

	private Money calcularMinimo(final Collection<Money> budgets) {
		Money moneyFinal = new Money();
		if (!budgets.isEmpty()) {
			moneyFinal.setCurrency(budgets.iterator().next().getCurrency());
			moneyFinal.setAmount(budgets.stream().mapToDouble(Money::getAmount).min().orElse(0.0));
		}
		return moneyFinal;
	}

	private Money calcularDesviacion(final Collection<Money> budgets) {
		Money desviacion = new Money();
		if (!budgets.isEmpty()) {
			desviacion.setCurrency(budgets.iterator().next().getCurrency());

			double media = budgets.stream().mapToDouble(Money::getAmount).average().orElse(0.0);
			double sumaDiferenciasCuadradas = budgets.stream().mapToDouble(budget -> Math.pow(budget.getAmount() - media, 2)).sum();

			double varianza = sumaDiferenciasCuadradas / budgets.size();
			desviacion.setAmount(Math.sqrt(varianza));
		}
		return desviacion;
	}
}
