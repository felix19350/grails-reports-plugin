package exception

public class DeleteException extends DomainClassException {

    public DeleteException(domainClass){
        super(domainClass, "Can't delete " + domainClass.getClass().getSimpleName())
    }

    public DeleteException(domainClass, Throwable throwable) {
        super(domainClass, "Can't delete " + domainClass.getClass().getSimpleName(), throwable)
    }

	String getErrorsString() {
	      String result = "id = ${domainClass.ident()}\n" 
	      return result
	  }


}
