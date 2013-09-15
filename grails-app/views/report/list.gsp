<%@ page import="grails.converters.JSON" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="developerConsole">
		<g:set var="entityName" value="${message(code: 'report.label', default: 'Report')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>

        <r:require module="reportsStyle"/>
        <r:require module="reportsViews"/>

		<r:script>
			$(function(){
				var collection = new AppModels.ReportCollection(${reports as JSON});
				var reportListView = new AppViews.ReportListView ({collection: collection});	
			});
		</r:script>

	</head>
	<body>
		<div id="reportListView" style="margin-top: 20px;">
		<hr/>
		<div class="row">
			<div class="span12">
				<table class="table table-main table-striped table-bordered">
					<thead>
						<tr>
							<ui:sortableColumn property="title" label="report.title.label"/>
							<th>Hooks</th>
						</tr>
					</thead>
					<tbody id="reportTable" class="links">
					</tbody>
				</table>
                <div class="pull-right">
                    <div class="btn" id="createReportBtn"><g:message code="ui.button.create"/></div>
                </div>

            </div>
		</div>

		<div class="modal hide" id="create-report-dialog">
			<div class="modal-header">
				<button class="close" data-dismiss="modal">×</button>
				<h3><g:message code='ui.label.newReport'/></h3>
			</div>
			<div class="modal-body">
				<div class="editing">
					<ui:property name="title" label="${g.message(code:'report.title.label')}" labelClass="span2 bold" viewable="false" editable="true" inputClass="span3" valueClass="span3"/>
					<ui:property name="hook" type="select" label="Hook" labelClass="span2 bold" options="${ org.grails.plugins.reports.ReportHook.list() }"  viewable="false" editable="true" optionKey="id" optionValue="name" noSelection="${ [null: 'None'] }"/>
				</div>
			</div>
			<div class="modal-footer">
				<div class="btn btn-success" id="saveReport"><g:message code="ui.button.create"/></div>
			</div>
		</div>

		</div>

		<g:render template="reportListItemTpl"/>
		<g:render template="defaultDeleteDialogTpl"/>

    </body>
</html>
