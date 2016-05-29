package kz.em.logic.report.params;

import java.util.Locale;

/**
 * Параметры запроса отчета
 */
public interface ReportQueryParams {
	ReportType getType();

	void setType(ReportType type);

	FileFormat getFormat();

	void setFormat(FileFormat format);

	Locale getLocale();

	void setLocale(Locale locale);
}
