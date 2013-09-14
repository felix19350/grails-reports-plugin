package org.grails.plugins.reports

import org.springframework.validation.FieldError


public class DomainClassException extends RuntimeException {

    String errorsString
    String domainClassName
    String ident
    def domainClass

    public DomainClassException(def domainClass){
        this(domainClass, "An error occurred during an operation on " + domainClass.getClass().getSimpleName())
    }

    DomainClassException(def domainClass, String message) {
        super(message) // RuntimeException
        this.domainClass = domainClass
        extractData(domainClass)
    }

    public DomainClassException(def domainClass, Throwable throwable){
        super("An error occurred during an operation on " + domainClass.getClass().getSimpleName() + " caused by " +  throwable.getMessage(), throwable)
        this.domainClass = domainClass
        extractData(domainClass)
    }

    public DomainClassException(def domainClass, String message, Throwable throwable){
        super(message + " caused by " +  throwable.getMessage(), throwable)
        this.domainClass = domainClass
        extractData(domainClass)
    }

    private extractData(def domainClass) {
        this.domainClassName = domainClass.getClass().getSimpleName()
        this.ident = domainClass.ident()?.toString() ?: "null"
        this.errorsString = extractErrorsString(domainClass)
    }

    static extractErrorsString(def domainClass) {
        String result = ""
        if(domainClass) {
            def ident = domainClass.ident()
            if(ident) {
                result = "id = ${ident}\n"
            }
            def allErrors =  domainClass.errors.allErrors.findAll({it instanceof FieldError})
            def allErrorsString = allErrors.collect({"${it.field} = '${it.rejectedValue}' (${it.getCode()})"})
            result += allErrorsString.join("\n")
        }
        return result
    }

}
