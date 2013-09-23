package org.grails.plugins.reports

import org.springframework.validation.FieldError

class DomainClassException extends RuntimeException {

    String errorsString
    String domainClassName
    String ident
    def domainClass

    DomainClassException(domainClass){
        this(domainClass, "An error occurred during an operation on ${domainClass.getClass().simpleName}")
    }

    DomainClassException(domainClass, String message) {
        super(message) // RuntimeException
        this.domainClass = domainClass
        extractData(domainClass)
    }

    DomainClassException(domainClass, Throwable throwable){
        super("An error occurred during an operation on ${domainClass.getClass().simpleName} caused by $throwable.message", throwable)
        this.domainClass = domainClass
        extractData(domainClass)
    }

    DomainClassException(domainClass, String message, Throwable throwable){
        super("$message caused by $throwable.message", throwable)
        this.domainClass = domainClass
        extractData(domainClass)
    }

    private extractData(domainClass) {
        this.domainClassName = domainClass.getClass().simpleName
        this.ident = domainClass.ident()?.toString() ?: "null"
        this.errorsString = extractErrorsString(domainClass)
    }

    static extractErrorsString(domainClass) {
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
