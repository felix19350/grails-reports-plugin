modules = {

    reportsStyle {
        //defaultBundle 'reportsCore'
        resource url:[plugin: 'reports', dir: 'less', file: 'reportsDefaults.less'], attrs:[rel: "stylesheet/less", type:'css']
    }

    reportsApplication {
        dependsOn 'reportsCore', 'reportsLibs', 'reportsBackbone'
    }

    reportsCore {
        //dependsOn 'mainStyle', 'mobileStyle', 'customBootstrap', 'underscore'
        dependsOn 'reportsUnderscore', 'bootstrap', 'reportsStyle'
    }

    reportsLibs {
        dependsOn 'reportsLog4javascript'
    }

    reportsBackbone {
        defaultBundle 'reportsCore'
        dependsOn 'reportsUnderscore'
        resource url: '/js/libs/backbone-min.js'
    }

    reportsUnderscore {
        defaultBundle 'reportsCore'
        resource url: '/js/libs/underscore-min.js'
        resource url: '/js/backbone/ext/underscore.init.js'
    }

    reportsCodemirror {
        resource url: 'js/libs/CodeMirror-2.25/lib/codemirror.js'
        resource url: 'js/libs/CodeMirror-2.25/lib/codemirror.css'
        resource url: 'js/libs/CodeMirror-2.25/mode/javascript/javascript.js'
        resource url: 'js/libs/CodeMirror-2.25/mode/css/css.js'
        resource url: 'js/libs/CodeMirror-2.25/mode/xml/xml.js'
        resource url: 'js/libs/CodeMirror-2.25/mode/htmlmixed/htmlmixed.js'
        resource url: 'js/libs/CodeMirror-2.25/mode/htmlembedded/htmlembedded.js'
        resource url: 'js/libs/CodeMirror-2.25/mode/groovy/groovy.js'
    }

    reportsLog4javascript {
        defaultBundle 'reportsLibs'
        dependsOn 'jquery'
        resource url: 'js/libs/log4javascript/log4javascript.js'
        resource url: 'js/libs/log4javascript/log4javascript.init.js'
    }

    /**
     * Backbone Resources
     */
    reportsBackboneCore {
        defaultBundle 'reportsBackbone'
        dependsOn 'reportsBackbone'
    }

    reportsBackboneModels {
        defaultBundle 'reportsBackbone'
        dependsOn 'reportsBackboneMixins'
        resource url: '/js/backbone/models/Report.js'
    }

    reportsBackboneMixins {
        defaultBundle 'reportsBackbone'
        dependsOn 'reportsBackboneCore'
        resource url: '/js/backbone/ext/backbone-collection-extensions.js'
        resource url: '/js/backbone/ext/backbone-validation-extensions.js'
        resource url: '/js/backbone/ext/backbone-model-extensions.js'
    }


    reportsBackboneViews {
        defaultBundle 'reportsBackbone'
        dependsOn 'reportsBackboneModels', 'reportsBackboneMixins', 'reportsSynapse'
        resource url: 'js/backbone/views/BaseView.js'
        resource url: 'js/backbone/views/ReportViews.js'
        //dependsOn 'backboneModels', 'backboneMixins', 'synapse'
    }

    reportsSynapse {
        defaultBundle 'reportsBackbone'
        dependsOn 'reportsBackboneCore'
        resource url: '/js/libs/synapse/synapse-iefix.js'
        resource url: '/js/libs/synapse/core.js'
        resource url: '/js/libs/synapse/object.js'
        resource url: '/js/libs/synapse/jquery.js'
        resource url: '/js/libs/synapse/backbone-model.js'
        resource url: '/js/libs/synapse/backbone-view.js'
        resource url: '/js/libs/synapse/synapse-init.js'
    }



}