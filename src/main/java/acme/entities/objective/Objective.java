
package acme.entities.objective;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Objective extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				instantiationMoment;

	@NotBlank
	@Length(max = 75)
	private String				title;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@NotNull
	private PriorityType		priority;

	@NotNull
	private Boolean				criticalStatus;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startDuration;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				finishDuration;

	@URL
	private String				optionalLink;

	// Derived attributes ----------------------------------------------------


	@Transient
	public Double getDuration() {

		final Double duration;

		//en milisegundos
		final long diferencia = this.finishDuration.getTime() - this.startDuration.getTime();

		//en dias 
		duration = (double) (diferencia / (1000 * 60 * 60 * 24));

		return duration;
	}

	// Relationships ----------------------------------------------------------

}
