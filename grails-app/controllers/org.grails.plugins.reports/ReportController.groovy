package org.grails.plugins.reports

import exception.CreateException
import exception.DeleteException
import exception.NotFoundException
import exception.UpdateException
import grails.converters.JSON
//import grails.plugins.springsecurity.Secured
import grails.util.GrailsUtil
import groovy.ui.SystemOutputInterceptor
import org.springframework.dao.DataIntegrityViolationException

//@Secured(['ROLE_ADMIN'])
class ReportController {

	def reportService


    /**
     * Print logs
     */
    def beforeInterceptor = {
        log.debug("Called ${controllerName} controller with params: ${params}")
    }

	def list() {
		def jsonResults = Report.list().collect{ it.encodeAsJson() }
		render(view:"list", model:[reports: jsonResults])
	}


	/**
	* Sets the session variables used on the preview. returns the output of the evaluation of the 
	* bindings.
	*/
	def setPreviewParams(){
		//log.debug("Setting preview params: ${params}")
		
		//Setup the session variables
		def result = eval(params.sampleBinding, [:])
		session.report = [templateDocument: params.templateDocument]
		session?.report?.binding = result.result ?: [:]

		//Prepare the output so that the ui is updated
		def reportInstance = Report.get(params.long('id'))
		def json = reportInstance.encodeAsJson()
		json.templateDocument = params.templateDocument
		json.sampleBinding = params.sampleBinding
		json.output = result.output
		json.exception = result.exception

		render json as JSON
	}

	def preview(){
		log.debug("Calling preview with params: ${params}")
		def reportInstance = Report.get(params.long('id'))

		def dbBinding = eval(reportInstance.sampleBinding, [:]) 
		def binding = session?.report?.binding ?: dbBinding.result ?: [:]
		def templateDocument = session?.report?.templateDocument ?: reportInstance.templateDocument

		reportService.renderReportTemplate(templateDocument, binding, response, "${reportInstance.title}.pdf", true)
	}

	def save() {
		def reportParams = [title: params.title, templateDocument: "", reportParams: ""]
		
		def reportInstance = new Report(reportParams)
		if (!reportInstance.save(flush: true)) {
			throw new CreateException(reportInstance)
		}

		if(params.hook){
			def hookId = params.long("hook")
			def hook = ReportHook.get(hookId)
			if(!hook){
				throw new NotFoundException(hookId, ReportHook)
			}
			
			hook.report = reportInstance
			if(!hook.save()){
				throw new UpdateException(hook)
			}
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
		log.debug("called update with params: ${params}")
		def reportInstance = Report.get(params.long('id'))
		if (!reportInstance) {
			throw new NotFoundException(params.id, Report)    
		}

		def reportParams = [title: params.title, templateDocument: params.templateDocument, sampleBinding: params.sampleBinding]
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
		log.debug("called update with params: ${params}")
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
