<%@ page import="grails.converters.JSON" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="developerConsole">
		<g:set var="entityName" value="${message(code: 'report.label', default: 'report')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
		
		<r:require module="reportsStyle"/>
		<r:require module="reportsCodemirror"/>
		<r:require module="reportsViews"/>
		
		<r:script>
			$(function(){
				var model = new AppModels.Report( ${reportInstance.encodeAsJson() as JSON} );
				var reportEditor = new AppViews.ReportView({ model: model });
			});
		</r:script>
	</head>
	<body>
		<div id="reportTemplateEditor" style="margin-left: 40px;">
			<div class="row">
			<!-- top editor buttons -->
			<div class="span12 editing">
				<div class="row default-margin-bottom">
					<div class="span12">
						<div class="pull-left">
							<a class="btn" id="backBtn" href="${g.createLink(mapping:'reportsList')}"><g:message code="ui.button.back"/></a>
						</div>
						<div class="pull-left default-margin-left">
							<div class="btn-group">
								
								<a class="btn" id="previewBtn">
									<i class="icon-play"></i>
									<g:message code="ui.button.preview"/>
								</a>
								<a class="btn" id="undoBtn">
                                    <g:message code="ui.button.undo"/>
								</a>
								<a class="btn" id="redoBtn">
                                    <g:message code="ui.button.redo"/>
								</a>
							</div>
						</div>
						<div class="pull-right">
							<div class="btn btn-primary" id="saveBtn">
								<i class="icon-ok icon-white"></i>	
								<g:message code="ui.button.save"/>
							</div>
						</div>
					</div>
				</div>

				<ui:property name="title" label="${g.message(code:'report.title.label')}" labelClass="span2 bold" viewable="${canView}" editable="${canEdit}" inputClass="span10" valueClass="span10"/>

				<div class="accordion" id="ide-input1">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle" data-toggle="collapse" data-parent="#ide-input1" href="#ide-template">Template</a>
						</div>
						<div class="accordion-body in collapse" id="ide-template">
							<ui:property name="templateDocument" type="textarea"  editable="${canEdit}" viewable="true" inputContainerClass="cleared default-margin-left" inputClass="span12" valueClass="span12"/>
						</div>
					</div>
				</div>
				
				<div class="accordion" id="ide-input2">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle" data-toggle="collapse" data-parent="#ide-input" href="#ide-binding">Binding</a>
						</div>
						<div class="accordion-body in collapse editing" id="ide-binding">
							<ui:property name="sampleBinding" type="textarea"  editable="${canEdit}" viewable="true" inputContainerClass="cleared default-margin-left" inputClass="span12" valueClass="span12"/>
						</div>
					</div>
				</div>
			</div>

			<div class="span11 default-margin-bottom">

				<ul class="nav nav-tabs" id="ide-output">
					<li class="active"><a href="#ide-preview" data-toggle="tab"><i class="icon-eye-open" style="margin-right: 10px;"></i>Preview</a></li>
					<li><a href="#ide-console" data-toggle="tab"><i class="icon-list-alt" style="margin-right: 10px;"></i>Console</a></li>
					<li><a href="#ide-exception" data-toggle="tab"><i class="icon-warning-sign" style="margin-right: 10px;"></i>Errors</a></li>
				</ul>

				<div class="tab-content">
					<div class="tab-pane active" id="ide-preview">
						<iframe  id="iframe" src="${g.createLink(controller:'report', action: 'preview', id: reportInstance?.id)}" style="width:100%;">
							<p><g:message code="ui.label.noSupportForIframes"/></p>
						</iframe>
					</div>
					<div class="tab-pane display" id="ide-console">
                            <pre class="span11 cleared default-margin-left" data-bind="output"></pre>
					</div>
					<div class="tab-pane display" id="ide-exception">
                        <pre class="span11 cleared default-margin-left" data-bind="exception"></pre>
					</div>
				</div>
			</div>
			</div>
			<hr/>
			<div class="row">
				<div style="margin:auto; text-align: center;">
					<div class="btn btn-danger" id="deleteBtn"><g:message code="ui.button.delete"/></div>
				</div>
			</div>

		</div>

		<g:render template="reportListItemTpl"/>
		<g:render template="defaultDeleteDialogTpl"/>

	</body>
</html>