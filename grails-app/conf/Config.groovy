// configuration for plugin testing - will not be included in the plugin zip

grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = true
grails.mime.types = [
        html: [
                'text/html',
                'application/xhtml+xml'
        ],
        xml: [
                'text/xml',
                'application/xml'
        ],
        text: 'text/plain',
        js: 'text/javascript',
        rss: 'application/rss+xml',
        atom: 'application/atom+xml',
        css: 'text/css',
        csv: 'text/csv',
        all: '*/*',
        json: [
                'application/json',
                'text/json'
        ],
        form: 'application/x-www-form-urlencoded',
        multipartForm: 'multipart/form-data',
        ods: 'application/vnd.oasis.opendocument.spreadsheet',
        pdf: 'application/pdf'
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = [
        '/images/*',
        '/css/*',
        '/js/*',
        '/plugins/*'
]

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart = false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true


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

    warn   'org.mortbay.log',
           'org.apache.catalina'

    debug 'grails.app.conf',
          'grails.app.filters',
          'grails.app.taglib',
          'grails.app.services',
          'grails.app.controllers',
          'grails.app.domain'

    error 'grails.app.taglib.org.grails.plugin.resource.ResourceTagLib'  

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



