package org.grails.plugins.reports

class IndexController {

	def index() {
        println "index()"
        redirect([uri: "/reports"])
    }


}
