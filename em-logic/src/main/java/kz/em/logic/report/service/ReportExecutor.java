package kz.em.logic.report.service;

import kz.em.logic.report.params.ReportType;

import java.sql.Connection;
import java.util.Map;

/**
 * Интерфейс класса, подготавливающего данные для отчета
 *
 * В этот класс можно перенести всю бизнес-логику формирования данных
 * для отчёта, отказавшись от выборки из БД.
 * В том случае необходимо передавать коллекции бинов данных JRBeanCollectionDatasource
 * в возвращаемой методом executeReport() мапе. В шаблоне для таблиц, списков и пр. следует
 * использовать источники данных из глобальных параметров
 */
public interface ReportExecutor {

	Map<String, Object> executeReport();

	ReportType getReportType();
}
