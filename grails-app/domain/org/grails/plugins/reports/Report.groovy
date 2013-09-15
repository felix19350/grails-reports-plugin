package org.grails.plugins.reports

class Report {

	String title
	String sampleBinding
	String templateDocument

	static mapping = {
		templateDocument type: 'text'
		sampleBinding type: 'text'
	}

    static constraints = {
    	title unique: true, maxSize: 255
    	sampleBinding nullable: true
	}

	def encodeAsJson(){
		def hooks = ReportHook.findAllByReport(this)
		
		return [
			id: id,
			title: title,
			hook: hooks.collect{ it.name },
			templateDocument: templateDocument,
			sampleBinding: sampleBinding
		]
	}
}
