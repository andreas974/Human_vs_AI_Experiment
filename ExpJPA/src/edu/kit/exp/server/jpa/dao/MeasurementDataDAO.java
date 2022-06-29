package edu.kit.exp.server.jpa.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.MeasurementData;

public class MeasurementDataDAO extends DefaultDAO<MeasurementData> {

	private static final String ERROR_MESSAGE = "JPA object not supposed to be used for data input. Pleas use 'fileInputStream'-parameter";

	public MeasurementDataDAO() {
		super(MeasurementData.class);
	}

	public void createMeasurementData(MeasurementData measurementData, FileInputStream fileInputStream) throws DataManagementException, SQLException, IOException {

		if (measurementData.getFileData().length > 0) {
			throw new IllegalArgumentException(ERROR_MESSAGE);
		}

		EntityManager em = PersistenceUtil.createEntityManager();

		em.getTransaction().begin();
		em.persist(measurementData);
		em.getTransaction().commit();
		em.refresh(measurementData);
		PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
		Integer id = (Integer) util.getIdentifier(measurementData);
		measurementData = findMeasurementDataById(id);
		writeFileStreamToDatabase(measurementData, fileInputStream);
		em.close();
	}

	private MeasurementData findMeasurementDataById(Integer id) throws DataManagementException {
		EntityManager em = PersistenceUtil.createEntityManager();
		MeasurementData result = null;

		try {

			TypedQuery<MeasurementData> query = em.createNamedQuery("MeasurementData.findByIdMeasurement", MeasurementData.class);
			query.setParameter("idMeasurementData", id);
			result = query.getSingleResult();

		} catch (Exception e) {

			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;

		} finally {
			em.close();
		}

		return result;
	}

	public InputStream retrieveMeasurementData(MeasurementData measurementData) throws DataManagementException {
		InputStream inputStream = null;
		try {
			Properties properties = PersistenceUtil.getPersistenceProperties();
			loadDatabaseDriver(properties);
			Connection connection = getDatabaseConnection(properties);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT file_data FROM measurement WHERE id = '" + measurementData.getId() + "'");
			resultSet.next();
			inputStream = resultSet.getBinaryStream("file_data");
			connection.commit();
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	private void writeFileStreamToDatabase(MeasurementData measurementData, FileInputStream fileInputStream) throws SQLException, IOException {
		Properties properties = PersistenceUtil.getPersistenceProperties();
		loadDatabaseDriver(properties);

		Connection connection = getDatabaseConnection(properties);
		PreparedStatement preparedStatement = connection.prepareStatement(getQueryString(measurementData));
		preparedStatement.setBinaryStream(1, fileInputStream);
		preparedStatement.executeUpdate();
		connection.commit();
		preparedStatement.close();
		fileInputStream.close();
		connection.close();
	}

	private String getQueryString(MeasurementData measurementData) {
		return "UPDATE measurement SET file_data = ? WHERE id_measurement_data = '" + measurementData.getId() + "'";
	}

	private void loadDatabaseDriver(Properties properties) {
		try {
			Class.forName(properties.getProperty("javax.persistence.jdbc.driver"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Connection getDatabaseConnection(Properties properties) throws SQLException {
		return DriverManager.getConnection(properties.getProperty("javax.persistence.jdbc.url"), properties.getProperty("javax.persistence.jdbc.user"), properties.getProperty("javax.persistence.jdbc.password"));
	}

}
