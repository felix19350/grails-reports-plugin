package tutorial.reports

import org.grails.plugins.reports.Report

class IndexController {

    def reportService

    def download() {
        Report reportInstance = Report.findByName("usersReport")
        def binding = reportInstance.evalBinding() // use sample params from the report
        reportService.renderReport(reportInstance, binding, response, null, false)
    }
}
