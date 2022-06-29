package edu.kit.exp.server.jpa.entity;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.kit.exp.common.sensor.ISensorRecorderConfiguration;

@Entity
@NamedQueries({ @NamedQuery(name = "SensorEntry.findAll", query = "SELECT s FROM SensorEntry s"), @NamedQuery(name = "SensorEntry.findByName", query = "SELECT s FROM SensorEntry s WHERE s.name = :name"), @NamedQuery(name = "SensorEntry.findByIdSensorEntry", query = "SELECT s FROM SensorEntry s WHERE s.idSensorEntry = :idSensorEntry"), @NamedQuery(name = "SensorEntry.findByFullyQualifiedName", query = "SELECT s FROM SensorEntry s WHERE s.fullyQualifiedName = :fullyQualifiedName"), @NamedQuery(name = "SensorEntry.findByExperiment", query = "SELECT s FROM SensorEntry s WHERE s.experiment = :experiment") })
@Table(name = "sensors", schema = "exp")
public class SensorEntry implements Serializable, IEntry {

	private static final long serialVersionUID = 8752869842756713543L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_sensor_entry", unique = true, nullable = false)
	private Integer idSensorEntry;

	@Column(name = "sensor_name", unique = true, nullable = false)
	private String name;

	@Column(name = "fully_qualified_name", unique = true, nullable = false)
	private String fullyQualifiedName;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH })
	@JoinColumn(name = "experiment_id_experiment", nullable = false)
	private Experiment experiment;

	@Lob
	@Column(name = "sensor_configuration")
	private ISensorRecorderConfiguration configuration;

	@Transient
	private boolean isConfigurationFoundInDb;

	public ISensorRecorderConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(ISensorRecorderConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean isConfigurationFoundInDb() {
		return isConfigurationFoundInDb;
	}

	public void setConfigurationFoundInDb(boolean isConfigurationFoundInDb) {
		this.isConfigurationFoundInDb = isConfigurationFoundInDb;
	}

	public void setIdSensorEntry(Integer idSensorEntry) {
		this.idSensorEntry = idSensorEntry;
	}

	public Integer getIdSensorEntry() {
		return idSensorEntry;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	public void setFullyQualifiedName(String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}

	public SensorEntry(String name, String fullyQualifiedname, ISensorRecorderConfiguration configuration) {
		setName(name);
		setFullyQualifiedName(fullyQualifiedname);
		setConfiguration(configuration);
		setConfigurationFoundInDb(false);
	}

	public SensorEntry() {

	}

	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SensorEntry) {
			SensorEntry entry = (SensorEntry) o;
			boolean conditionOne = this.name.equals(entry.getName());
			boolean conditionTwo = this.fullyQualifiedName.equals(entry.getFullyQualifiedName());
			return (conditionOne && conditionTwo);
		}
		return false;
	}

	@Override
	public Object getId() {
		return idSensorEntry;
	}

}
