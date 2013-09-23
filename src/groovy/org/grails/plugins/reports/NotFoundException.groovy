package org.grails.plugins.reports

class NotFoundException extends RuntimeException {

    def id
    String className

    NotFoundException(id){
        this(id, "someting")
    }

    NotFoundException(id, Class clazz){
        this(id, clazz.getSimpleName())
    }

    NotFoundException(id, String className) {
        super("Can't find $className with id $id")
        this.id = id
        this.className = className
    }
}
