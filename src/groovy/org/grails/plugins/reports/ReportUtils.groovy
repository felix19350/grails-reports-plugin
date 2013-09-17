package org.grails.plugins.reports

import javax.servlet.ServletContext
import groovy.util.logging.Commons

import org.apache.commons.logging.LogFactory

public class ReportUtils {

	private static final log = LogFactory.getLog(this)

	private static final String REPORTS_FOLDER = "/reports"

	public static void updateReportsFromResources(ServletContext servletContext, List<Map> reportInfoList) {
		for(Map reportInfo: reportInfoList) {
                        String name = reportInfo.name
                        String title = reportInfo.title
			Report report = Report.findByName(name)
			if(report == null) {
				String templateDocument = loadResourceContent(servletContext, "${name}.gsp")
                                String bindingBuilder = loadResourceContent(servletContext, "${name}.groovy", "[:]")
                                String sampleParams = loadResourceContent(servletContext, "${name}.params.groovy", "[:]")
                
                                report = new Report(
                                        name: name,
                                	title: title,
                                	templateDocument: templateDocument,
                                	bindingBuilder: bindingBuilder,
                                	sampleParams: sampleParams
                                )

                                report.save(failOnError: true)
			} 
		}
	}

	private static String loadResourceContent(ServletContext servletContext, String filename, String defaultText = "") {
                String filePath = "${REPORTS_FOLDER}/${filename}"
                log.debug("loading report from ${filePath}")
                InputStream input = servletContext.getResourceAsStream(filePath)
                String content = input?.getText() ?: defaultText
                return content
	}

}