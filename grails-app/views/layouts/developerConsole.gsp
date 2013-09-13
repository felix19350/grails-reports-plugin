<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">

    <script id="defaultDeleteDialog" type="text/template">
        <div class="modal hide fade" id="{{= modalId }}">
            <div class="modal-header">
                <a href="#" class="close" data-dismiss="modal">&times;</a>
                <h3><g:message code="ui.label.deleteConfirm"/></h3>
            </div>
            <div class="modal-body">
                <p><g:message code="ui.label.deleteConfirmBody"/></p>
            </div>
            <div class="modal-footer">
                <a href="#" class="btn btn-danger delete-btn-confirm"><g:message code="ui.button.delete"/></a>
            </div>
        </div>
    </script>


    <script id="reportListItemTpl" type="text/template">
        <td>{{= title }}</td>
        <td>{{= hook }}</td>
    </script>


    <r:script disposition='head'>
		App = typeof App === "undefined" ? {} : App;
		App.lang = '<%= org.springframework.web.servlet.support.RequestContextUtils.getLocale(request) %>';
        App.serverURL = '<%=org.codehaus.groovy.grails.commons.ConfigurationHolder.config.grails.serverURL%>';

        AppViews = {};
        AppModels = {};
    </r:script>

    <style>
    #reportConsoleContainer { margin-top: 80px; margin-bottom: 10px; }
    </style>


    <g:layoutHead/>
    <r:require module="reportsApplication" />
    <r:layoutResources />

</head>
<body>

<!-- Header for the desktop version -->
<div class="navbar navbar-fixed-top visible-desktop">
    <div class="navbar-inner" id="app-button-holder">
        <div class="clearfix">
            <div class="pull-left" style="margin-top: 6px;">
                <h1>Report console</h1>
            </div>
        </div>
    </div>
</div>

<!-- contents -->
<div id="reportConsoleContainer">

    <!-- Div that displays error message information -->
    <div id="serverErrorMsg" class="hide"></div>

    <!-- Div that displays success messages -->
    <div id="serverSuccessMsg" class="hide"></div>

    <g:layoutBody/>
</div>

<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>
<r:layoutResources />
</body>
</html>