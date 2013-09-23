package org.grails.plugins.reports

class CreateException extends DomainClassException {

    CreateException(domainClass){
        super(domainClass, "Can't create new ${domainClass.getClass().simpleName}")
    }
}
