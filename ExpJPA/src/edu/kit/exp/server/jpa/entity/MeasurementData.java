package edu.kit.exp.server.jpa.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "measurement", schema = "exp")
@NamedQueries({ @NamedQuery(name = "MeasurementData.findAll", query = "SELECT m FROM MeasurementData m"), @NamedQuery(name = "MeasurementData.findByIdMeasurement", query = "SELECT m FROM MeasurementData m WHERE m.idMeasurementData = :idMeasurementData") })
public class MeasurementData implements Serializable, IEntry {

	private static final long serialVersionUID = 841809313825716982L;

	@Id
	@GeneratedValue
	@Column(name = "id_measurement_data")
	private int idMeasurementData;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "client_id")
	private String clientId;

	@Column(name = "subject_id")
	private String subjectId;

	@Column(name = "start_time")
	private Long startTimeStamp;

	@Column(name = "stop_time")
	private Long stopTimeStamp;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "file_data")
	private byte[] fileData;

	public MeasurementData(String fileName, String clientId, String subjectId, long startTimeStamp, long stopTimeStamp) {
		setFileName(fileName);
		setClientId(clientId);
		setSubjectId(subjectId);
		setStartTimeStamp(startTimeStamp);
		setStopTimeStamp(stopTimeStamp);
		setFileData(new byte[0]);
	}

	public MeasurementData() {

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getIdMeasurementData() {
		return idMeasurementData;
	}

	public void setIdMeasurementData(int id) {
		this.idMeasurementData = id;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	@Override
	public Object getId() {
		return idMeasurementData;
	}

	public Long getStopTimeStamp() {
		return stopTimeStamp;
	}

	public void setStopTimeStamp(Long stopTimeStamp) {
		this.stopTimeStamp = stopTimeStamp;
	}

	public Long getStartTimeStamp() {
		return startTimeStamp;
	}

	public void setStartTimeStamp(Long startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}
}
