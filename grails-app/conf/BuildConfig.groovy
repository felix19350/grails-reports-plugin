grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        compile('org.xhtmlrenderer:core-renderer:R8') {
            excludes 'itext'
        }
        compile("net.sf.jtidy:jtidy:r938")

        compile("com.lowagie:itext:2.1.0") {
            excludes "bcmail-jdk14", "bcprov-jdk14", "bctsp-jdk14"
        }
    }

    plugins {
        build ':release:2.2.1', ':rest-client-builder:1.0.3', {
            export = false
        }

        runtime ":resources:1.2"
        runtime ":jquery:1.8.3"
        compile ":twitter-bootstrap:2.3.2"

        /*compile ":backbonejs:1.0.0" {
            excludes 'resources' // use new 1.2 version instead of RC2
        }*/

        compile ":lesscss-resources:1.3.3"
        compile(":rendering:0.4.4") {
            excludes 'itext'
        }
    }
}
