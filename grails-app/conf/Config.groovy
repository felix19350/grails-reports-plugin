log4j = {
    error 'org.codehaus.groovy.grails',
          'org.springframework',
          'org.hibernate',
          'net.sf.ehcache.hibernate'

    debug 'grails.app.conf',
          'grails.app.filters',
          'grails.app.taglib',
          'grails.app.services',
          'grails.app.controllers',
          'grails.app.domain',
          'org.grails.plugins.reports.ReportUtils'

    error 'grails.app.taglib.org.grails.plugin.resource.ResourceTagLib'
}

grails.resources.processing.startup = "delayed"
grails.serverURL = "http://localhost:8080/${appName}"

/**
 * Docs
 **/

grails.doc.authors="Bruno Félix, Hugo Monteiro, Nuno Luís"
