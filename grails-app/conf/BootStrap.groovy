import org.grails.plugins.reports.ReportUtils

class BootStrap {
    def grailsApplication

    def init = { servletContext ->
        initReports(servletContext)
    }

    private void initReports(def servletContext) {
        log.debug ("init reports")
        def reports = [
            [name: "exampleReport", title: "Example report"]
        ]
        
        ReportUtils.updateReportsFromResources(servletContext, reports)
    }


    def destroy = {
        // nothing to destroy
    }
} 