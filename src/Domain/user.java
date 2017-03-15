package Domain;

import java.io.Serializable;

/**
 * Created by Alex on 05.01.2017.
 */
public class user extends HasID<String> implements Serializable
{
    private int passwordHash;
    private boolean is_super_user;
    public user(String _username,int _pass, boolean _is_super_user)
    {
        super(_username);
        passwordHash=_pass;
        is_super_user=_is_super_user;
    }

    public int getPasswordHash() { return passwordHash; }
    public String getUsername() { return super.getId(); }
    public boolean Is_super_user(){return is_super_user;}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof user)
            return ((user) obj).passwordHash==passwordHash && ((user) obj).getId()==getId();
        else
            return false;
    }
}
