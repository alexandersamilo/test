package kz.em.logic.report.controller;

import kz.em.logic.report.params.BaseReportQueryParams;
import kz.em.logic.report.params.FileFormat;
import kz.em.logic.report.params.ReportQueryParams;
import kz.em.logic.report.params.ReportType;
import kz.em.logic.report.service.ReportCreator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

@Controller
public class ReportController {
	private static final Logger log = LoggerFactory.getLogger(ReportController.class);

	@Inject
	private ReportCreator reportCreator;

	/**
	 * Обработка запроса отчета по умолчанию
	 */
	@RequestMapping(value = "/report", method = RequestMethod.GET)
	@ResponseBody
	public void load(HttpServletResponse response) {
		writeReportBack(response, getReportParams(FileFormat.XLSX, ReportType.SUMMARY));
	}

	/**
	 * Обработка запроса отчета
	 */
	@RequestMapping(value = "/report/{type}/{format}", method = RequestMethod.GET)
	@ResponseBody
	public void load(HttpServletResponse response, @PathVariable String type, @PathVariable String format) {
		ReportQueryParams params;
		try {
			FileFormat fileFormat = FileFormat.valueOf(format.toUpperCase());
			ReportType reportType = ReportType.valueOf(type.toUpperCase());
			params = getReportParams(fileFormat, reportType);
		} catch (IllegalArgumentException e) {
			params = getReportParams(FileFormat.XLSX, ReportType.SUMMARY);
		}
		writeReportBack(response, params);
	}

	private ReportQueryParams getReportParams(FileFormat fileFormat, ReportType reportType) {
		ReportQueryParams params = new BaseReportQueryParams();
		params.setFormat(fileFormat);
		params.setType(reportType);
		params.setLocale(new Locale("ru", "RU"));
		return params;
	}

	private void writeReportBack(HttpServletResponse response, ReportQueryParams params) {
		byte[] report = reportCreator.createReport(params);
		if (report != null) {
			try (InputStream is = new ByteArrayInputStream(report)) {
				response.setContentType(params.getFormat().getContentType());
				response.setHeader("Content-Disposition", "attachment; filename=\"" + getReportFilename(params) + "\"");
				IOUtils.copy(is, response.getOutputStream());
				response.flushBuffer();
			} catch (IOException ex) {
				log.warn("Error while uploading report to client", ex);
			}
		} else {
			log.warn(String.format("Could not create %s report", params.getType()));
		}
	}

	private String getReportFilename(ReportQueryParams params) {
		return params.getType().toString().toLowerCase() + "." + params.getFormat().toString().toLowerCase();
	}

	@ExceptionHandler
	public void handle(Exception e) {
		log.warn("Exception in report controller: ", e);
	}
}
