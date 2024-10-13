
package acme.forms;

import java.util.Map;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	double						totalNumProgressLogBelow25;

	double						totalNumProgressLogBetween25And50;

	double						totalNumProgressLogBetween50And75;

	double						totalNumProgressLogAbove75;

	Map<String, Double>			averageBudgetPerCurrency;

	Map<String, Double>			deviationBudgetPerCurrency;

	Map<String, Double>			minimumBudgetPerCurrency;

	Map<String, Double>			maximumBudgetPerCurrency;

	String[]					supportedCurrenciesPerCurrency;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
