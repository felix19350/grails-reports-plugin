package org.grails.plugins.reports

class DeleteException extends DomainClassException {

    DeleteException(domainClass){
        super(domainClass, "Can't delete ${domainClass.getClass().simpleName}")
    }

    DeleteException(domainClass, Throwable throwable) {
        super(domainClass, "Can't delete ${domainClass.getClass().simpleName}", throwable)
    }

	String getErrorsString() {
	      "id = ${domainClass.ident()}\n"
	  }
}
