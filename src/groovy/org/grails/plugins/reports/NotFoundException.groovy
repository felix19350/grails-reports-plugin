package org.grails.plugins.reports

public class NotFoundException extends RuntimeException {

    def id
    String className

    public NotFoundException(id){
        this(id, "someting")
    }

    public NotFoundException(id, Class clazz){
        this(id, clazz.getSimpleName())
    }

    public NotFoundException(id, String className) {
        super("Can't find " + className + "with id " + id)
        this.id = id
        this.className = className
    }

    def getId(){
        return id
    }

    def getClassName() {
        return className
    }

}
