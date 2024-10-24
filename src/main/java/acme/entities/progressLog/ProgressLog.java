
package acme.entities.progressLog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.client.data.AbstractEntity;
import acme.entities.contract.Contract;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "contract_id")
})
public class ProgressLog extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "^PG-[A-Z]{1,2}-\\d{4}$", message = "PG-[A-Z]{1,2}-[0-9]{4}")
	protected String			recordId;

	@Digits(integer = 3, fraction = 2)
	@Min(0)
	@Max(100)
	protected double			completeness;

	@NotBlank
	@Length(max = 100)
	protected String			comment;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	protected Date				registrationMoment;

	@NotBlank
	@Length(max = 75)
	protected String			responsiblePerson;

	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Contract			contract;
}
