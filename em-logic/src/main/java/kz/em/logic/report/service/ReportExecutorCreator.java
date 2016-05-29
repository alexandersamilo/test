package kz.em.logic.report.service;

import kz.em.logic.report.params.ReportType;

public interface ReportExecutorCreator {

	ReportExecutor createExecutor(ReportType type);

}
