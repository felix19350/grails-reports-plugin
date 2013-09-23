class ReportsGrailsPlugin {
    def groupId = "org.grails.plugins"
    // the plugin version
    def version = "0.3.6"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.2 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "Grails Reports Plugin" // Headline display name of the plugin
    def author = "Bruno Félix"
    def authorEmail = "felix19350@gmail.com"
    def description = '''Tool to generate reports using a template in HTML. It has a UI tool to edit the template at runtime.'''

    // URL to the plugin's documentation
    def documentation = "http://felix19350.github.io/grails-reports-plugin/"
    def issueManagement = [system: 'GitHub', url: 'https://github.com/felix19350/grails-reports-plugin/issues']

    // Details of company behind the plugin (if there is one)
    // def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
    def developers = [
        [name: "Hugo Monteiro", email: "hugo.monteiro@gmail.com"],
        [name:'Nuno Luís', email:"nuno.lopes.luis@gmail.com"]
    ]

    // Location of the plugin's issue tracker.
    // def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/felix19350/grails-reports-plugin" ]

    def licence = [name: "MITLICENSE", url: "http://opensource.org/licenses/MIT"]
}
