package org.grails.plugins.reports

import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

public class Report {

	/** Unique name */
	String name
	/** Title */
	String title
	/** Sample params maps groovy code */
	String sampleParams
	/** Grrovy code to build the bindling based on params */
	String bindingBuilder
	/** GSP template code */
	String templateDocument

	Date dateCreated
	Date lastUpdated

	static mapping = {
		templateDocument type: 'text'
		bindingBuilder type: 'text'
	}

    static constraints = {
    	name unique: true, maxSize: 255, matches: "[a-zA-Z]+", nullable: false, blank: false
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
        	return false;
        }

        if (this.is(other)) {
        	return true;
        }

        if(!DomainClassArtefactHandler.isDomainClass(other.getClass())) {
        	return false;
        }

        if (!(other.instanceOf(Report))) {
        	return false;
        }

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.name, other.name);
        return builder.equals();
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(name);
        return builder.hashCode();
    }

	String toString() {
		return "Report #${id} ${name} ${title}"
	}

	Map evalSampleParams() {
		GroovyShell shell = new GroovyShell()
		def result = shell.evaluate(this.sampleParams)
		shell = null
		return result
	}

	Map evalBinding() {
		def params = evalSampleParams()
		return evalBinding(params)
	}

	Map evalBinding(Map params) {
		GroovyShell shell = new GroovyShell()
		shell.setVariable("params", params)
		def result = shell.evaluate(this.bindingBuilder)
		shell = null
		return result
	}

}
