// configuration for plugin testing - will not be included in the plugin zip

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
}


/*******************************
 * GORM constraints
 *******************************/

grails.gorm.default.constraints = {
    notEmptyCollection(nullable: false, minSize: 1)
    uniqueOptionalString(nullable: true, blank: true, maxSize: 255, unique: true)
    optionalString(nullable: true, blank: true, maxSize: 255)
    requiredString(nullable: false, blank: false, maxSize: 255)
    optionalText(nullable: true, blank: true)
    requiredText(nullable: false, blank: false)
    uniqueString(unique: true, nullable: false, blank: false, maxSize: 255)
    username(nullable: false, blank: false, unique: true)
    password(nullable: false, blank: false)
}

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = [
        '/images/*',
        '/css/*',
        '/js/*',
        '/plugins/*'
]

/*******************************
 * Resources settings
 *******************************/

grails.resources.processing.startup = "delayed"
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"

grails.serverURL = "http://localhost:8080/${appName}"
