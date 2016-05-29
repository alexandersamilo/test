package kz.em.logic.report.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Компонент создания подключения к базе данных
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ConnectionManager {

	@Value("${connection.driver}")
	private String driver;
	@Value("${connection.url}")
	private String url;
	@Value("${connection.user}")
	private String user;
	@Value("${connection.password}")
	private String password;

	public Connection obtainConnection() throws SQLException {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
		return DriverManager.getConnection(url, user, password);
	}
}
