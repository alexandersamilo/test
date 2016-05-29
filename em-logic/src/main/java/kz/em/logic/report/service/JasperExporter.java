package kz.em.logic.report.service;

import kz.em.logic.report.params.FileFormat;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Экспортёр внутреннего пердставления отчета в конктретный формат документа
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JasperExporter {
	private static Logger log = LoggerFactory.getLogger(JasperExporter.class);

	public byte[] exportReport(FileFormat format, JasperPrint jp) {
		byte[] output = null;
		if (jp != null) {
			Exporter<ExporterInput, ReportExportConfiguration, ExporterConfiguration, ExporterOutput> exporter = defineExporter(format);
			exporter.setExporterInput(new SimpleExporterInput(jp));

			try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
				exporter.exportReport();
				outputStream.flush();
				output = outputStream.toByteArray();
			} catch (IOException e) {
				log.warn("Failed to close output stream after filling jasper report: " + e);
			} catch (JRException e) {
				log.warn("Failed to export jasper report: " + e);
			}
		}
		return output;
	}

	private Exporter<ExporterInput, ReportExportConfiguration, ExporterConfiguration, ExporterOutput> defineExporter(FileFormat format) {
		switch (format) {
			case XLSX: {
				JRAbstractExporter exporter = new JRXlsxExporter();
				SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
				config.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
				config.setWhitePageBackground(Boolean.FALSE);
				config.setIgnorePageMargins(Boolean.FALSE);
				config.setFontSizeFixEnabled(Boolean.TRUE);
				config.setIgnoreCellBorder(Boolean.FALSE);
				exporter.setConfiguration(config);
				return exporter;
			}
			default: {
				JRAbstractExporter pdfExporter = new JRPdfExporter();
				SimplePdfReportConfiguration config = new SimplePdfReportConfiguration();
				config.setSizePageToContent(true);
				pdfExporter.setConfiguration(config);
				return pdfExporter;
			}
		}
	}
}
