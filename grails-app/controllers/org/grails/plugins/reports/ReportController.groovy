package org.grails.plugins.reports

import grails.converters.JSON
import grails.util.GrailsUtil

import groovy.ui.SystemOutputInterceptor
import org.springframework.dao.DataIntegrityViolationException

class ReportController {

	def reportService

    /**
     * Print logs
     */
    //def beforeInterceptor = {
    //    log.debug("Called ${controllerName} controller with params: ${params}")
    //}

	def list() {
		def jsonResults = Report.list().collect{ it.encodeAsJson() }
		render(view:"list", model:[reports: jsonResults])
	}


	/**
	* Sets the session variables used on the preview. returns the output of the evaluation of the
	* bindings.
	*/
	def setPreviewParams(){
		log.debug("Setting preview params: ${params.id}")

		//Setup the session variables
		def sampleParamsEval = eval(params.sampleParams, [:]) ?: [:]
		def bindingBuilderEval = eval(params.bindingBuilder, [params: sampleParamsEval.result]) ?: [:]

		session.report = [
			templateDocument: params.templateDocument,
			sampleParams: sampleParamsEval.result,
			binding : bindingBuilderEval.result
		]

		//Prepare the output so that the ui is updated
		def reportInstance = Report.get(params.long('id'))
		def json = reportInstance.encodeAsJson()
		json.templateDocument = params.templateDocument
		json.sampleParams = params.sampleParams
		json.bindingBuilder = params.bindingBuilder

		json.output = bindingBuilderEval.output
		json.exception = bindingBuilderEval.exception

		render json as JSON
	}

	def preview(){
		log.debug("Call preview report #${params.id}")

		def reportInstance = Report.get(params.long('id'))
		if (!reportInstance) {
			throw new NotFoundException(params.id, Report)
		}

		if(!session.report) {
			def sampleParamsEval = eval(reportInstance.sampleParams, [:]) ?: [:]
			def bindingBuilderEval = eval(reportInstance.bindingBuilder, [params: sampleParamsEval.result]) ?: [:]
			session.report = [
				templateDocument: reportInstance.templateDocument,
				sampleParams: sampleParamsEval.result,
				binding : bindingBuilderEval.result
			]
		}

		def binding = session.report.binding ?: [:]
		def templateDocument = session.report.templateDocument ?: reportInstance.templateDocument

		reportService.renderReportTemplate(templateDocument, binding, response, "${reportInstance.title}.pdf", true)
	}

    def download() {
        log.debug("Call download report #${params.id}")
        def reportInstance = Report.get(params.long('id'))
		if (!reportInstance) {
			throw new NotFoundException(params.id, Report)
		}

		def binding = reportInstance.evalBinding() // use sample params from the report
		reportService.renderReport(reportInstance, binding, response, null, false)
    }



	def save() {
        log.debug("Creating a new report #${params.name}")
        def reportParams = [name: params.name, title: params.title]

		def reportInstance = new Report(reportParams)
		if (!reportInstance.save(flush: true)) {
			throw new CreateException(reportInstance)
		}

		def json = reportInstance.encodeAsJson()
		render json as JSON
	}


	def show() {
		def reportInstance = Report.get(params.long('id'))
		if (!reportInstance) {
			throw new NotFoundException(params.id, Report)
		}

		//Each time this view is loaded the session is cleared of the variable that holds the preview
		session.removeAttribute("report")
		[reportInstance: reportInstance, canView: true, canEdit: true]
	}


	def update() {
		log.debug("Go to update report #${params.id}")
		def reportInstance = Report.get(params.long('id'))
		if (!reportInstance) {
			throw new NotFoundException(params.id, Report)
		}

		def reportParams = [title: params.title, templateDocument: params.templateDocument, bindingBuilder: params.bindingBuilder, sampleParams: params.sampleParams]
		reportInstance.properties = reportParams

		if (!reportInstance.save(flush: true)) {
			throw new UpdateException(reportInstance)
		}

		//When the item is saved the attribute is removed
		session.removeAttribute("report")

		def json = reportInstance.encodeAsJson()
		render json as JSON
	}


	def delete(){
		log.debug("Go to delete report #${params.id}")
		def reportInstance = Report.get(params.long('id'))
		if (!reportInstance) {
			throw new NotFoundException(params.id, Report)
		}
		try {
		   reportInstance.delete()
		} catch( DataIntegrityViolationException e) {
			throw new DeleteException(reportInstance, e)
		}

		render(status: '200', contentType: "text/json") { ["message": g.message(code: 'default.delete.message')] }
	}


	def renderReport() {
		def reportInstance = Report.get(params.long('id'))
		if (!reportInstance) {
			throw new NotFoundException(params.id, Report)
		}

		def binding = reportInstance.evalBinding(params)
		reportService.renderReport(reportInstance, binding, response, null, true)
	}



    @SuppressWarnings("CatchThrowable")
    private Map eval(String code, Map bindingValues) {

		def map = [:]
		def output = new StringBuilder()
		def sysoutInterceptor = new SystemOutputInterceptor({ String s ->
			output.append s
			return false
		})
		sysoutInterceptor.start()

		try {
			map.result = createShell(bindingValues).evaluate(code)
		} catch(Throwable t) {

			def sw = new StringWriter()
			new PrintWriter(sw).withPrintWriter { GrailsUtil.deepSanitize(t).printStackTrace(it) }

			map.exception = sw.toString()
		} finally {
			sysoutInterceptor.stop();
		}

		map.output = output.toString()
		return map
	}

	private GroovyShell createShell(Map bindingValues) {
		bindingValues.ctx = grailsApplication.mainContext
		bindingValues.grailsApplication = grailsApplication
		bindingValues.config = grailsApplication.config
		new GroovyShell(grailsApplication.classLoader, new Binding(bindingValues))
	}


}
