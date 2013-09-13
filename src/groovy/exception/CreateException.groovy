package exception

public class CreateException extends DomainClassException {

    public CreateException(domainClass){
        super(domainClass, "Can't create new " + domainClass.getClass().getSimpleName())
    }

}
