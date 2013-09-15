modules = {


    // LIBS //

    // external jquery

    reportsUnderscore {
        resource url: 'js/libs/underscore.js'
        resource url: 'js/libs/underscore.init.js'
    }

    reportsBackbone {
        resource url: 'js/libs/backbone.js'
    }

    reportsLog4javascript {
        resource url: 'js/libs/log4javascript.js'
        resource url: 'js/libs/log4javascript.init.js'
    }

    reportsSynapse {
        resource url: 'js/libs/synapse.js'
        resource url: 'js/libs/synapse/object.js'
        resource url: 'js/libs/synapse/jquery.js'
        resource url: 'js/libs/synapse/backbone-model.js'
        resource url: 'js/libs/synapse/backbone-view.js'
        resource url: 'js/libs/synapse-init.js'
    }

    reportsCodemirror {
        resource url: 'js/libs/codemirror.js'
        resource url: 'css/codemirror/codemirror.css'
        resource url: 'js/libs/codemirror/mode/javascript/javascript.js'
        resource url: 'js/libs/codemirror/mode/css/css.js'
        resource url: 'js/libs/codemirror/mode/xml/xml.js'
        resource url: 'js/libs/codemirror/mode/htmlmixed/htmlmixed.js'
        resource url: 'js/libs/codemirror/mode/htmlembedded/htmlembedded.js'
        resource url: 'js/libs/codemirror/mode/groovy/groovy.js'
    }

    reportsBackboneMixins {
        dependsOn 'reportsBackbone'
        resource url: 'js/ext/backbone-collection-extensions.js'
        resource url: 'js/ext/backbone-validation-extensions.js'
        resource url: 'js/ext/backbone-model-extensions.js'
    }


    /* Models */

    reportsModels {
        dependsOn 'reportsBackboneMixins', 'reportsLog4javascript'
        resource url: 'js/models/Report.js'
    }

    /* Views */ 

    reportsViews {
        dependsOn 'reportsUnderscore', 'jquery', 'reportsModels', 'reportsSynapse'
        resource url: 'js/views/BaseView.js'
        resource url: 'js/views/ReportViews.js'
    }

    /* Reports plugin */

    reportsStyle {
        dependsOn 'bootstrap'
        resource url:[plugin: 'reports', dir: 'less', file: 'reports.less'], attrs:[rel: "stylesheet/less", type:'css']
    }


}