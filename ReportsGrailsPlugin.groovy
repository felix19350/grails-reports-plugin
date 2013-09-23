class ReportsGrailsPlugin {
    def groupId = "org.grails.plugins"
    def version = "0.3.6"
    def grailsVersion = "2.0 > *"

    def title = "Grails Reports Plugin"
    def author = "Bruno Félix"
    def authorEmail = "felix19350@gmail.com"
    def description = '''Tool to generate reports using a template in HTML. It has a UI tool to edit the template at runtime.'''

    def documentation = "http://felix19350.github.io/grails-reports-plugin/"
    def issueManagement = [system: 'GitHub', url: 'https://github.com/felix19350/grails-reports-plugin/issues']

    def developers = [
        [name: "Hugo Monteiro", email: "hugo.monteiro@gmail.com"],
        [name:'Nuno Luís', email:"nuno.lopes.luis@gmail.com"]
    ]

    def issueManagement = [system: "GITHUB", url: "https://github.com/felix19350/grails-reports-plugin/issues"]
    def scm = [ url: "https://github.com/felix19350/grails-reports-plugin" ]

    def licence = [name: "MITLICENSE", url: "http://opensource.org/licenses/MIT"]
}
