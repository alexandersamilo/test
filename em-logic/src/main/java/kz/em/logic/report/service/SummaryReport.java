package kz.em.logic.report.service;

import kz.em.logic.report.jdbc.ConnectionManager;
import kz.em.logic.report.params.ReportType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Бин отчета "Отчёт по пилоту КИС МиУСС"
 *
 * В качестве примера выбрал количество записей и отдал в отчет
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SummaryReport implements ReportExecutor {

	private static final Logger log = LoggerFactory.getLogger(SummaryReport.class);
	@Inject
	private ConnectionManager connectionManager;

	private static final String GET_INVOCATION_COUNT = "SELECT COUNT(*) FROM invocation";
	private static final String GET_ACCIDENT_COUNT = "SELECT COUNT(*) FROM accident";

	@Override
	public Map<String, Object> executeReport() {
		Map<String, Object> params = new HashMap<>();
		try (Connection connection = connectionManager.obtainConnection()) {

			PreparedStatement stmt = connection.prepareStatement(GET_INVOCATION_COUNT);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				params.put("invocationCount", rs.getLong(1));
			}

			stmt = connection.prepareStatement(GET_ACCIDENT_COUNT);
			rs = stmt.executeQuery();
			if (rs.next()) {
				params.put("accidentCount", rs.getLong(1));
			}
		} catch (SQLException e) {
			log.warn("SQL connection error", e);
		}
		return params;
	}

	@Override
	public ReportType getReportType() {
		return ReportType.SUMMARY;
	}
}
