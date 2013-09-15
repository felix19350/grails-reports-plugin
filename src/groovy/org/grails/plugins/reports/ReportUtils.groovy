package org.grails.plugins.reports

import javax.servlet.ServletContext
import groovy.util.logging.Commons

import org.apache.commons.logging.LogFactory

public class ReportUtils {

	private static final log = LogFactory.getLog(this)

	private static final String REPORTS_FOLDER = "/reports"

	public static void updateReportsFromResources(ServletContext servletContext, List<String> reportNames) {
		for(String reportName: reportNames) {
			Report report = Report.findByName(reportName)
			if(report == null) {
				String templateDocument = loadResourceContent(servletContext, "${reportName}.gsp")
                String bindingBuilder = loadResourceContent(servletContext, "${reportName}.groovy")
                String sampleParams = loadResourceContent(servletContext, "${reportName}.params.groovy")
                
                report = new Report(
                	name: reportName,
                	templateDocument: templateDocument,
                	bindingBuilder: bindingBuilder,
                	sampleParams: sampleParams
                )

                report.save(failOnError: true)
			} 
		}
	}

	private static String loadResourceContent(ServletContext servletContext, String filename) {
        String filePath = "${REPORTS_FOLDER}/${filename}"
        log.debug("loading report from ${filePath}")
        InputStream input = servletContext.getResourceAsStream(filePath)
        String content = input.getText()
        return content
	}

}