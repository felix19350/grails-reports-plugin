package org.grails.plugins.reports

public class UpdateException extends DomainClassException {

  public UpdateException(domainClass){
    super(domainClass, "Can't update " + domainClass.getClass().getSimpleName())
  }
}
