package org.grails.plugins.reports

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler

class Report {

    def grailsApplication

    /** Unique name */
	String name
	/** Title */
	String title
	/** Sample params maps groovy code */
	String sampleParams = DEFAULT_SAMPLE_PARAMS_SOURCE
	/** Grrovy code to build the bindling based on params */
	String bindingBuilder = DEFAULT_BINDING_BUILDER_SOURCE
	/** GSP template code */
	String templateDocument = DEFAULT_TEMPLATE_SOURCE

	Date dateCreated
	Date lastUpdated

    public static final String DEFAULT_SAMPLE_PARAMS_SOURCE = "[:]"

    public static final String DEFAULT_BINDING_BUILDER_SOURCE = "[:]"

    public static final String DEFAULT_TEMPLATE_SOURCE = """
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<body>
    <!-- body -->
</body>
</html>"""

    static mapping = {
		templateDocument type: 'text'
		bindingBuilder type: 'text'
	}

    static constraints = {
    	name unique: true, maxSize: 255, matches: "[a-zA-Z0-9]+", blank: false
    	bindingBuilder nullable: true
	}

	def beforeValidate() {
		if(title == null) {
			title = name
		}
	}

	def encodeAsJson(){
		return [
			id: id,
			name: name,
			title: title,
			sampleParams: sampleParams,
			bindingBuilder: bindingBuilder,
			templateDocument: templateDocument
		]
	}

	boolean equals(other) {
        if (other == null) {
        	return false
        }

        if (this.is(other)) {
        	return true
        }

        if(!DomainClassArtefactHandler.isDomainClass(other.getClass())) {
        	return false
        }

        if (!(other.instanceOf(Report))) {
        	return false
        }

        EqualsBuilder builder = new EqualsBuilder()
        builder.append(name, other.name)
        return builder.equals()
    }

    int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
        builder.append(name)
        return builder.hashCode()
    }

	String toString() {
		return "Report #${id} ${name} ${title}"
	}

	Map evalSampleParams() {
		GroovyShell shell = new GroovyShell(grailsApplication.classLoader)
		return shell.evaluate(sampleParams)
	}

	Map evalBinding() {
		def params = evalSampleParams()
		return evalBinding(params)
	}

	Map evalBinding(Map params) {
		GroovyShell shell = new GroovyShell(grailsApplication.classLoader)
		shell.setVariable("params", params)
		return shell.evaluate(bindingBuilder)
	}
}
