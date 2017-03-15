package SQL_Repositories;

import Domain.inchiriere;
import MemoryRepositories.CrudRepository;
import Validator.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by Alex on 02.01.2017.
 */

public class inchiriereSQLRepository implements CrudRepository<inchiriere,Integer>,I_SQLRepository
{
    Validator<inchiriere> validator;
    private Connection con;
    private int size;
    private String[] last_filtre = new String[] {"",""};

    public inchiriereSQLRepository(Validator<inchiriere> _validator, Connection _con) throws IOException
    {
        validator=_validator;
        con=_con;
        size=get_All_keys().size();
    }

    public inchiriere add (inchiriere i) throws IOException, ExceptieValidareInRepository
    {
        try {
            validator.Validate(i);
        } catch (ExceptieValidator exceptieValidator) {
            throw new ExceptieValidareInRepository(exceptieValidator.getMessage());
        }

        PreparedStatement stmt = null;
        int generatedID=-1;

        try {
            String query="declare @result int;\n" +
                    "declare @result_table table(nr int);\n" +
                    "execute insert_inchiriere @result output, ?, ?;\n" +
                    "insert into @result_table values(@result);\n" +
                    "Select nr from @result_table;";
            boolean hasMoreResultSets;

            stmt=con.prepareStatement(query);
            stmt.setInt(1,i.getCod_c());
            stmt.setInt(2,i.getCod_f());

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
        return new inchiriere(generatedID,i.getCod_f(),i.getCod_c());
    }

    public void replace(inchiriere i) throws IOException, ExceptieValidareInRepository
    {
    }

    public void remove(Integer key) throws IOException
    {
        PreparedStatement stmt = null;
        try {
            String query="execute delete_inchiriere ?";

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

    public inchiriere getByID(Integer key)
    {
        inchiriere i=null;
        PreparedStatement stmt = null;
        try {
            String query="select * from [dbo].get_inchiriere (?)";

            stmt=con.prepareStatement(query);
            stmt.setInt(1,key);
            ResultSet rs=stmt.executeQuery();
            con.commit();

            while (rs.next())
                i = new inchiriere(rs.getInt("ID_inchiriere"),rs.getInt("id_f"),rs.getInt("id_c"));
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
        return i;
    }

    /**
     * @param filter string #1: nume client, #2 titlu film
     * @return
     */
    @Override
    public void setFilter(String... filter) {
        last_filtre=filter;
    }

    public List<Integer> get_All_filtered_keys()
    {
        PreparedStatement stmt = null;
        List<Integer> result=new ArrayList<>();
        try {
            String query="select * from [dbo].get_filtered_keys_inchirieri (?,?)";

            stmt=con.prepareStatement(query);
            stmt.setString(1,last_filtre[0]);
            stmt.setString(2,last_filtre[1]);
            ResultSet rs=stmt.executeQuery();
            con.commit();

            while (rs.next())
                result.add(rs.getInt("ID_inchiriere"));
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
            String query="select * from [dbo].get_filtered_keys_inchirieri (?,?)";

            stmt=con.prepareStatement(query);
            stmt.setString(1,"");
            stmt.setString(2,"");
            ResultSet rs=stmt.executeQuery();
            con.commit();

            while (rs.next())
                result.add(rs.getInt("ID_inchiriere"));
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

    public List<inchiriere> getByID(List<Integer> keys)
    {
        ArrayList<inchiriere> l=new ArrayList<>();

        PreparedStatement stmt = null;
        try {
            String query="select * from [dbo].get_inchiriere (?)";

            stmt=con.prepareStatement(query);
            for(int k:keys)
            {
                stmt.setInt(1,k);
                ResultSet rs=stmt.executeQuery();
                //con.commit();
                while (rs.next())
                    l.add(new inchiriere(rs.getInt("ID_inchiriere"),rs.getInt("id_f"),rs.getInt("id_c")));
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
