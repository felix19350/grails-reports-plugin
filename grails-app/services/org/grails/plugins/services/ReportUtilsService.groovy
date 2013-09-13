package org.grails.plugins.services

import org.grails.plugins.reports.Report
import org.grails.plugins.reports.ReportHook

class ReportUtilsService {

    /**
     * Verifies if the list of reports are in the database. It saves the new report if it is not.
     *
     * @param reports list of reports
     * @return
     */
    def verifyReports(def reports, def servletContext) {
        log.debug("verifying reports...")
        reports.each { it ->
            String title = it.title
            if (!Report.findByTitle(title)) {
                String templateDocument = getReport(servletContext, "${it.hook}.gsp")
                String sampleBinding = getReport(servletContext, "${it.hook}.groovy")
                Report reportInstance = new Report(title: title, templateDocument: templateDocument, sampleBinding: sampleBinding)
                reportInstance.save(failOnError: true)

                //By default the hook matches the hook name so we bind it
                def reportHook = ReportHook.findByName(it.hook)
                if (!reportHook) {
                    reportHook = createReportHook(it.hook)
                }

                reportHook.report = reportInstance
                reportHook.save(failOnError: true)
            }
        }
    }

    private String getReport(def servletContext, filename) {
        log.debug("getting report at " + "/reports/$filename")
        return servletContext.getResourceAsStream("/reports/$filename").text
        //return grailsApplication.mainContext.getResourceAsStream("html/$filename".toString()).text
    }

    private ReportHook createReportHook(String name) {
        def temp = new ReportHook(name: name)
        temp.save(failOnError: true)
        return temp
    }


}
