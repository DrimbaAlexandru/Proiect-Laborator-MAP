package Validator;

/**
 * Created by Alex on 04.01.2017.
 */
public class ExceptieValidator extends Exception
{
    public ExceptieValidator()
    {
        super();
    }
    public ExceptieValidator(String msg) {super(msg);}
}