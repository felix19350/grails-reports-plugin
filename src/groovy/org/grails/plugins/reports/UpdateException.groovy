package org.grails.plugins.reports

class UpdateException extends DomainClassException {

  UpdateException(domainClass){
    super(domainClass, "Can't update ${domainClass.getClass().simpleName}")
  }
}
