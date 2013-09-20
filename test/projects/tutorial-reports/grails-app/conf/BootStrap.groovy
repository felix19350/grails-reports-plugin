import org.grails.plugins.reports.ReportUtils
import tutorial.reports.User

class BootStrap {
    def grailsApplication

    def init = { servletContext ->
        initReports(servletContext)
        initUsers()
    }

    private void initReports(def servletContext) {
        log.debug ("init reports")
        def reports = [
            [name: "usersReport", title: "List of Users"]
        ]
        
        ReportUtils.updateReportsFromResources(servletContext, reports)
    }

    private void initUsers() {
        log.debug("creating users")

        User u1 = new User(name: "Hugo Monteiro", address: "Street 33", city: "Sintra")
        User u2 = new User(name: "Bruno Félix", address: "Street 6", city: "Almada")
        User u3 = new User(name: "Nuno Luís", address: "Street 1", city: "Porto")

        u1.save()
        u2.save()
        u3.save()
    }


    def destroy = {
        // nothing to destroy
    }
} 