package org.grails.plugins.reports

class ReportMacro {

	String name
	String payload

	static mapping = {
		payload type:'text'
	}

    static constraints = {
    	name unique:true, maxSize:255
    	payload nullable: true
    }

    String toString(){
    	return name
    }

    int hashCode(){
    	return name.hashCode()
    }

    boolean equals(Object o){
    	return o instanceof ReportMacro ? this.name == o.name : false
    }

    def encodeAsJson(){
    	return [
    		id: id,
    		name: name,
    		payload: payload
    	]
    }
}
