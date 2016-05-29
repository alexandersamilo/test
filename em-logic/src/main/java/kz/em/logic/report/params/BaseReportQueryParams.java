package kz.em.logic.report.params;

import java.util.Locale;

/**
 * Базовый класс параметров запроса отчета
 */
public class BaseReportQueryParams implements ReportQueryParams {
	private ReportType type;
	private FileFormat format;
	private Locale locale;

	public ReportType getType() {
		return type;
	}

	public void setType(ReportType type) {
		this.type = type;
	}

	public FileFormat getFormat() {
		return format;
	}

	public void setFormat(FileFormat format) {
		this.format = format;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
