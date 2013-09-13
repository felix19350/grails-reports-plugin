import exception.NotFoundException
import org.springframework.mock.web.MockHttpServletResponse

import pt.org.frp.reports.ReportHook
import pt.org.frp.clinical.dialysis.*
import org.grails.plugins.defaultExceptionHandler.exception.*


class ReportServiceSpec extends grails.plugin.spock.IntegrationSpec {
	
	def reportService
	
	/*def "Render report by hoock name"(){
		
		given: 
			def hookName = "dialysisPlan"
			def hook = ReportHook.findByName(hookName)
			def dialysisPlan = DialysisPlan.one
			def patient = dialysisPlan.dialysisProfile.patient
			def binding = [patient: patient, plan:dialysisPlan]
			def response = new MockHttpServletResponse()
			def expectedFilename = (filesubname == null ? hook.report.title : hook.report.title + " - " + filesubname) + ".pdf"
			def expectedContentDisposition = "${isInline ? 'inline' : 'attachment'}; filename=${expectedFilename}"
		
		when:
			reportService.renderHookReport(hookName, binding, response, filesubname, isInline)
		
		then:
			assert response.getContentType() == "application/pdf"
			assert response.getContentAsByteArray().size() > 0
			assert response.getHeader("Content-Disposition") == expectedContentDisposition
			
		where:
			isInline 	| filesubname
			true 		| "teste - 1"
			false 		| "teste - 2"
			true 		| null
			false 		| null
			
	}
	
	def "Render report by hoock name - fail"(){
		
		given:
			def newReportHook = new ReportHook(name:"newReportHook").save()
		
		and:
			def hookName = fakeReportHook ? newReportHook.name : "xpto"
			def dialysisPlan = DialysisPlan.one
			def patient = dialysisPlan.dialysisProfile.patient
			def binding = [patient: patient, plan:dialysisPlan]
			def filesubname = "${dialysisPlan.id} - ${dialysisPlan.dialysisProfile.patient.shortName}"
			def response = new MockHttpServletResponse()
		
		when:
			reportService.renderHookReport(hookName, binding, response, filesubname, true)
		
		then:
			thrown expectedException
			
		where:
			fakeReportHook 	| expectedException
			true 			| NotFoundException
			false 			| NotFoundException
	}*/
}