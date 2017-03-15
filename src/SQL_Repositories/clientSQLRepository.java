package SQL_Repositories;
/**
 * Created by Alex on 03.01.2017.
 */
import Domain.client;
import MemoryRepositories.CrudRepository;
import Validator.*;

import java.io.IOException;
import java.sql.*;
import java.util.Collection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by Alex on 02.01.2017.
 */

public class clientSQLRepository implements CrudRepository<client,Integer>, I_SQLRepository
{
    Validator<client> validator;
    private Connection con;
    private int size;
    private String[] last_filtre = new String[] {"",""};

    public clientSQLRepository(Validator<client> _validator, Connection _con) throws IOException
    {
        validator=_validator;
        con=_con;
        size=get_All_keys().size();
    }

    public client add (client c) throws IOException, ExceptieValidareInRepository
    {
        try {
            validator.Validate(c);
        } catch (ExceptieValidator exceptieValidator) {
            throw new ExceptieValidareInRepository(exceptieValidator.getMessage());
        }

        PreparedStatement stmt = null;
        int generatedID=-1;

        try {
            String query="declare @result int;\n" +
                    "declare @result_table table(nr int);\n" +
                    "execute insert_client @result output, ?, ?;\n" +
                    "insert into @result_table values(@result);\n" +
                    "Select nr from @result_table;";
            boolean hasMoreResultSets;

            stmt=con.prepareStatement(query);
            stmt.setString(1,c.getNume());
            stmt.setString(2,c.getAdresa());

            hasMoreResultSets=stmt.execute();
            con.commit();
            while ( hasMoreResultSets || stmt.getUpdateCount() != -1 )
            {
                if ( hasMoreResultSets )
                {
                    ResultSet rs = stmt.getResultSet();
                    if(rs!=null)
                        while (rs.next())
                            generatedID = rs.getInt("nr");
                }
                else
                if ( stmt.getUpdateCount() == -1 )
                    break;
                hasMoreResultSets = stmt.getMoreResults();
            }
        }
        catch (SQLException e1) {
            if(e1.getMessage().contains("UNIQUE KEY"))
                throw new ExceptieValidareInRepository("Elementul exista deja!");
            else
                e1.printStackTrace();
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        size=get_All_keys().size();
        return new client(c.getNume(),generatedID,c.getAdresa());
    }

    public void replace(client e) throws IOException, ExceptieValidareInRepository
    {
        try {
            validator.Validate(e);
        } catch (ExceptieValidator exceptieValidator) {
            throw new ExceptieValidareInRepository(exceptieValidator.getMessage());
        }

        PreparedStatement stmt = null;
        try {
            String query="execute update_client ?,?,?";

            stmt=con.prepareStatement(query);
            stmt.setInt(1,e.getId());
            stmt.setString(2,e.getNume());
            stmt.setString(3,e.getAdresa());
            stmt.execute();
        }
        catch (SQLException e1) {
            e1.printStackTrace();
            throw new IOException(e1.getMessage());
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        size=get_All_keys().size();
    }

    public void remove(Integer key) throws IOException
    {
        PreparedStatement stmt = null;
        try {
            String query="execute delete_client ?";
            stmt=con.prepareStatement(query);
            stmt.setInt(1,key);
            stmt.execute();
            con.commit();
        }
        catch (SQLException e1) {
            e1.printStackTrace();
            throw new IOException(e1.getMessage());
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        size=get_All_keys().size();
    }

    public client getByID(Integer key)
    {
        client c=null;
        PreparedStatement stmt = null;
        try {
            String query="select * from [dbo].get_client (?)";

            stmt=con.prepareStatement(query);
            stmt.setInt(1,key);
            ResultSet rs=stmt.executeQuery();
            con.commit();

            while (rs.next())
                c = new client(rs.getString("nume"),rs.getInt("ID_client"),rs.getString("adresa"));
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return c;
    }



    /**
     * @param filter string #1: nume, #2 adresa
     * @return
     */
    @Override
    public void setFilter(String... filter) {
        last_filtre=filter;
    }

    @Override
    public List<Integer> get_All_filtered_keys()
    {
        PreparedStatement stmt = null;
        List<Integer> result=new ArrayList<>();
        try {
            String query="select * from [dbo].get_filtered_keys_clienti (?,?)";

            stmt=con.prepareStatement(query);
            stmt.setString(1,last_filtre[0]);
            stmt.setString(2,last_filtre[1]);
            ResultSet rs=stmt.executeQuery();
            con.commit();

            while (rs.next())
                result.add(rs.getInt("ID_client"));
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result;
    }

    public int get_all_keys_count()
    {
        return size;
    }

    public List<Integer> get_All_keys()
    {
        PreparedStatement stmt = null;
        List<Integer> result=new ArrayList<>();
        try {
            String query="select * from [dbo].get_filtered_keys_clienti (?,?)";

            stmt=con.prepareStatement(query);
            stmt.setString(1,"");
            stmt.setString(2,"");
            ResultSet rs=stmt.executeQuery();
            con.commit();

            while (rs.next())
                result.add(rs.getInt("ID_client"));
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result;
    }

    public List<client> getByID(List<Integer> keys)
    {
        ArrayList<client> l=new ArrayList<>();
        client c=null;
        PreparedStatement stmt = null;
        try {
            String query="select * from [dbo].get_client (?)";

            stmt=con.prepareStatement(query);
            for(int k:keys)
            {
                stmt.setInt(1,k);
                ResultSet rs=stmt.executeQuery();
                //con.commit();
                while (rs.next())
                    l.add(new client(rs.getString("nume"),rs.getInt("ID_client"),rs.getString("adresa")));
            }
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return l;
    }
}
