package kz.em.logic.report.service;

import kz.em.logic.report.jdbc.ConnectionManager;
import kz.em.logic.report.params.ReportQueryParams;
import kz.em.logic.report.resource.UTF8Control;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Класс, управляющий созданием отчетов
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReportCreator {

	private static Logger log = LoggerFactory.getLogger(ReportCreator.class);
	private static final String TEMPLATE_EXT = ".jrxml";
	private static final String PATH_TO_TEMPLATES = "/kz/em/logic/report/template/";
	public static final String REPORT_RESOURCE_BUNDLE = "REPORT_RESOURCE_BUNDLE";
	private static final String REPORT_LOCALE = "REPORT_LOCALE";

	@Inject
	private JasperExporter exporter;
	@Inject
	private ReportExecutorCreator reportExecutorCreator;
	@Inject
	private ConnectionManager connectionManager;

	/**
	 * Выгрузка отчета в массив байт
	 *
	 * @param params параметры запроса отчета
	 * @return массив байт выполненного отчета
	 */
	public byte[] createReport(ReportQueryParams params) {
		ReportExecutor executor = reportExecutorCreator.createExecutor(params.getType());
		Map<String, Object> parameters = executor.executeReport();
		parameters.put(REPORT_LOCALE, params.getLocale());
		ResourceBundle bundle = getReportResourceBundle(params);
		if (bundle != null) {
			parameters.put(REPORT_RESOURCE_BUNDLE, bundle);
		}
		return exporter.exportReport(params.getFormat(), performExport(parameters, getReportTemplatePath(executor)));
	}

	/**
	 * Формирование отчета
	 *
	 * @param parameters   Мапа параметров отчета
	 * @param templatePath Путь к шаблону JR
	 * @return Сформированный отчет
	 */
	private JasperPrint performExport(Map<String, Object> parameters, String templatePath) {
		try (Connection connection = connectionManager.obtainConnection()) {
			JasperDesign jasperDesign = JRXmlLoader.load(ReportCreator.class.getClassLoader().getResourceAsStream(templatePath));
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			return JasperFillManager.fillReport(jasperReport, parameters, connection);
		} catch (JRException e) {
			log.error("JasperPrint report fill failed", e);
		} catch (SQLException e) {
			log.error("Database connection error", e);
		}
		return null;
	}

	private String getReportTemplatePath(ReportExecutor report) {
		return PATH_TO_TEMPLATES + report.getClass().getSimpleName() + TEMPLATE_EXT;
	}

	private ResourceBundle getReportResourceBundle(ReportQueryParams params) {
		String reportName = params.getType().toString().toLowerCase();
		Locale locale = params.getLocale();
		try {
			return ResourceBundle.getBundle("l10n." + reportName, locale, new UTF8Control());
		} catch (MissingResourceException e) {
			return null;
		}
	}
}
