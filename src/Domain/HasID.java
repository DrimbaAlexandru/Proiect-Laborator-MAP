package Domain;

import java.io.Serializable;

/**
 * Created by Alex on 04.01.2017.
 */
public class HasID<T> implements Serializable
{
    private T id;

    public HasID(T value) {
        id=value;
    }

    public T getId() {
        return id;
    }
    public void setIdUsed(T cod){};
}