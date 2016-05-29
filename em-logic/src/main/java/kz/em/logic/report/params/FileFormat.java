package kz.em.logic.report.params;

/**
 * Доступные форматы выгружаемых отчетов
 */
public enum FileFormat {
	XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	PDF("application/pdf");

	private String contentType;

	FileFormat(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}
}