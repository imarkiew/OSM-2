package db;

import tools.Validation;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.apache.derby.jdbc.EmbeddedDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mappedEntities.Experiment;
import mappedEntities.Patient;
/**
 * Uzyty wzorzec Singleton
 */
public class DbManager
{
	/**
	 * prywatna, statyczna instancja DbManager zainicjalizowana jako null
	 */
	private static DbManager instance = null;
	/**
	 * Obiekt sluzace do obslugi polaczenia z baza danych
	 */
	Connection conn;
    PreparedStatement pstmt;
    Statement stmt;
	ResultSet rs;
	/**
	 * lista z leczonymi pacjentami
	 */
    ObservableList<Patient> curedList = FXCollections.observableArrayList();
    /**
     * lista z pacjentami przyjmujacymi placebo
     */
    ObservableList<Patient> placeboList = FXCollections.observableArrayList();
    
    /**
     * funkcja zwracajaca instancje CuredList
     * @return curedList
     */
    public ObservableList<Patient> getCuredList() 
    {
		return curedList;
	}
    
    /**
     * funkcja ustawiajaca lokalna instancje CuredList
     * @param curedList
     */
	public void setCuredList(ObservableList<Patient> curedList) 
	{
		this.curedList = curedList;
	}
	
	/**
	 * funkcja zwracajaca instancje PlaceboList
	 * @return placeboList
	 */
	public ObservableList<Patient> getPlaceboList() 
	{
		return placeboList;
	}
	
	/**
	 * funkcja ustawiajaca lokalna instancje PlaceboList
	 * @param placeboList
	 */
	public void setPlaceboList(ObservableList<Patient> placeboList) 
	{
		this.placeboList = placeboList;
	}

    /**
     * funkcja main sluzaca do polaczenia sie z managerem, dodawania i testowania zawartosci
     * @param args
     */
    public static void main(String[] args) {
      DbManager manager = getInstance();
      manager.connect();
     // manager.addDrug("Lakroft", "Jaskra");
     // manager.addExperiment("Walka z jaskr�", 102);
     //manager.addPatient("Marek", "Prus", "76031403456", 102, false, null, null);
     //manager.addPatient("Adam", "Ptak", "75031403416", 102, true, null, null);
     // manager.addPatient("Krzysztof", "Oliwa", "86022403453", 1, false, null, null);
     //manager.addDoctorExperiment(1, 104);
     //manager.addDoctorExperiment(2, 104);
     /* try 
       {
    	  manager.stmt = manager.conn.createStatement();
    	  manager.stmt.execute("delete from patients where id > 5");
		} catch (SQLException ex) 
		{
			System.out.println(ex);
		}*/
      
      manager.printTables();
      manager.disconnect();
      
    }
    
    /**
     * prywatny konstruktor klasy
     */
    private DbManager()
    {
    	connect();
    }
    
    /**
     * funkcja zwracajaca singleton klasy
     * @return
     */
    public static DbManager getInstance()
    {
    	if(instance == null)
    		return new DbManager();
    	else
    		return instance;			
    }
   
    /**
     * funkcja sluzaca do laczenia sie z baza danych 
     */
    public void connect()
    {
	      
	      try
	      {
	         Driver derbyEmbeddedDriver = new EmbeddedDriver();
	         DriverManager.registerDriver(derbyEmbeddedDriver);
	         conn = DriverManager.getConnection("jdbc:derby:medicalDatabase; create = false");
	         conn.setAutoCommit(true);

	      } catch (SQLException ex)
	      {
	         System.out.println("in connection" + ex);
	      }
	   }
    
    /**
     * funkcja do rozlaczenia sie z baza danych 
     */
    public void disconnect()
    {
    	 try
    	 {
	         DriverManager.getConnection
	            ("jdbc:derby:;shutdown=true");
	     } 
    	 catch (SQLException ex)
    	 {
	         if (((ex.getErrorCode() == 50000) && ("XJ015".equals(ex.getSQLState())))) 
	         {
	               System.out.println("Derby shut down normally");
	         } 
	         else 
	         {
	            System.err.println("Derby did not shut down normally");
	            System.err.println(ex.getMessage());
	         }
	      }
    }
    
    /**
     * funkcja sluzaca do dodawnia lekarza
     * @param name
     * @param surname
     * @param PESEL
     * @param login
     * @param password
     */
    public void addDoctor(String name, String surname, String PESEL, String login, String password)
    {
    	try
    	{
            pstmt = conn.prepareStatement("insert into doctors (name, surname, PESEL, login, password)"
            		+ " values(?,?,?,?,?)");
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setString(3, PESEL);
            pstmt.setString(4, login);
            pstmt.setString(5, password);
            pstmt.executeUpdate();
		} 
    	catch (SQLException ex) 
    	{
			System.out.println(ex);
		}
    }
    
    /**
     * Funkcja dodajaca pacjenta do bazy danych. 
     * Zwraca prawde w przypadku pozytywnej walidacji i zapisu.
     * @param name
     * @param surname
     * @param PESEL
     * @param experiment_id
     * @param cured
     * @param dose
     * @param effect
     * @return response
     */
    public boolean addPatient(String name, String surname, String PESEL, int experiment_id, boolean cured, String dose, String effect)
    {
    	boolean isPeselInBase = false;
    	boolean response = true;
    	
		try 
		{
		    stmt = conn.createStatement();
	   		rs = stmt.executeQuery("select PESEL from patients");
	         while (rs.next()) 
	         {
	        	if(rs.getString(1).equals(PESEL))
	        	{
	        		isPeselInBase = true;
	        		break;
	        	}
	            
	         }
		} 
		catch (SQLException ex) 
		{
			response = false;
			System.out.println(ex);
		}
    	
        if(Validation.isName(name) && Validation.isName(surname) && Validation.isInt(PESEL) && PESEL.length() == 11 && isPeselInBase == false)
    	{
	    		
	    	try 
	    	{
	            pstmt = conn.prepareStatement("insert into patients (name, surname, PESEL,"
	            		+ " experiment_id, cured, dose, effect )"
	            		+ " values(?,?,?,?,?,?,?)");
	            pstmt.setString(1, name);
	            pstmt.setString(2, surname);
	            pstmt.setString(3, PESEL);
	            pstmt.setInt(4, experiment_id);
	            pstmt.setBoolean(5, cured);
	            pstmt.setString(6, dose);
	            pstmt.setString(7, effect);
	            pstmt.executeUpdate();
			} 
	    	catch (SQLException ex) 
	    	{
				response = false;
				System.out.println(ex);
			}
    	}
    	else
    	{
    		response = false;
    	}
    	
        return response;
    }
    
    /**
     * funkcja sluzaca do dodawania eksperymentu
     * @param name
     * @param drug_id
     */
    public void addExperiment(String name, int drug_id)
    { 
         try 
         {
        	pstmt = conn.prepareStatement("insert into experiments (name, drug_id )" + " values(?,?)");
            pstmt.setString(1, name);
            pstmt.setInt(2, drug_id);
            pstmt.executeUpdate();
 		} 
        catch (SQLException ex) 
        {
 			System.out.println(ex);
 		}
    }
    	
    /**
     * funkcja sluzaca do dodawania leku
     * @param name
     * @param disease
     */
    public void addDrug(String name, String disease)
    {
        try 
        {
        	 pstmt = conn.prepareStatement("insert into drugs (name, disease )" + " values(?,?)");
             pstmt.setString(1, name);
             pstmt.setString(2, disease);
             pstmt.executeUpdate();
		} 
        catch (SQLException ex) 
        {
			System.out.println(ex);
		}
   	}
    
    /**
     * funkcja sluzaca do dodawanie lekarza do eksperymentu
     * @param doctor_id
     * @param experiment_id
     */
    public void addDoctorExperiment(int doctor_id, int experiment_id)
    { 
        try
        {
       	    pstmt = conn.prepareStatement("insert into doctors_experiments (doctor_id, experiment_id )" + " values(?,?)");
            pstmt.setInt(1, doctor_id);
            pstmt.setInt(2, experiment_id); 
            pstmt.executeUpdate();
		} 
        catch (SQLException ex) 
        {
			System.out.println(ex);
		}
   	}
    
    /**
     * Funkcja do modyfikacji danych pacjenta w bazie danych
     * Zwraca prawde w przypadku pozytywnej walidacji i zapsiu
     * @return response
     */
    public boolean upadatePatientData(String name, String surname, String PESEL, String dose, String effect, int id)
    {
    	boolean response = true;
    	
        if(Validation.isName(name) && Validation.isName(surname) && Validation.isInt(PESEL) && PESEL.length() == 11)
    	{
        	try 
	    	{
	            pstmt = conn.prepareStatement("update patients set name = ?, surname = ?, PESEL = ?, dose = ?, effect = ? where id = ?");
	            pstmt.setString(1, name);
	            pstmt.setString(2, surname);
	            pstmt.setString(3, PESEL);
	            pstmt.setString(4, dose);
	            pstmt.setString(5, effect);
	            pstmt.setInt(6, id);
	            pstmt.executeUpdate();
			} 
	    	catch (SQLException ex) 
	    	{
				System.out.println(ex);
				response = false;
			}
    	}
    	else
    	{
    		response = false;
    	}
        	
        return response;
    }
    
    /**
     * funkcja sluzaca do wyswietlania danych z tabel
     */
    public void printTables()
    {
		try 
		{
			stmt = conn.createStatement();
			System.out.println("doctors");
   		 	rs = stmt.executeQuery("select * from doctors");
   		 	while (rs.next())
   		 	{
   	            System.out.printf("%d %s %s %s %s %s\n",
   	            rs.getInt(1), rs.getString(2),
   	            rs.getString(3), rs.getString(4),
   	            rs.getString(5), rs.getString(6));
   		 	}
        
   	        System.out.println("patients");
       		rs = stmt.executeQuery("select * from patients");
   	        while (rs.next())
   	        {
   	            System.out.printf("%d %s %s %s %d %b %s %s\n",
   	            rs.getInt(1), rs.getString(2),
   	            rs.getString(3), rs.getString(4),
   	            rs.getInt(5), rs.getBoolean(6),
   	            rs.getString(7), rs.getString(8));
   	        }
         
   	         System.out.println("drugs");
       		 rs = stmt.executeQuery("select * from drugs");
   	         while (rs.next()) 
   	         {
   	            System.out.printf("%d %s %s\n",
   	            rs.getInt(1), rs.getString(2),
   	            rs.getString(3));
   	         }
   	         
   	         System.out.println("experiments");
   	         rs = stmt.executeQuery("select experiments.id, experiments.name from experiments");
   	         while (rs.next())
   	         {
   	            System.out.printf("%d %s \n",
   	            rs.getInt(1), rs.getString(2)
   	            );
   	         }
         
   	         System.out.println("doctors - experiments");
   	         rs = stmt.executeQuery("select * from doctors_experiments");
   	         while (rs.next())
   	         {
   	            System.out.printf("%d %d\n",
   	            rs.getInt(1),
   	            rs.getInt(2));
   	         }
		} 
		catch (SQLException ex) 
		{
			System.out.println(ex);
		}
    }
    
    /**
     * funkcja sluzaca do zwracania listy pacjentow
     * uzywana pomocniczo do wyswietlania zawartosci bazy
     * @return patientList
     */
    public ObservableList<Patient> getPatientObservableList()
    {
    	ObservableList<Patient> patientList = FXCollections.observableArrayList();
    	
    	try 
    	{
    		stmt = conn.createStatement();
    		rs = stmt.executeQuery("select * from patients");
		    while (rs.next()) 
		    {
		       patientList.add(new Patient(
		       rs.getInt("id"), rs.getString("name"),
		       rs.getString("surname"), rs.getString("PESEL"),
		       rs.getInt("experiment_id"), rs.getBoolean("cured"),
		       rs.getString("dose"), rs.getString("effect")));
		    }
		} 
    	catch (SQLException ex) 
    	{
			System.out.println(ex);
		}
    	
    	return patientList;
    }
    
    /**
     * funkcja sluzaca do zwracania listy z eksperymentami
     * @param doctorID
     * @return
     */
    public ObservableList<Experiment> getExperimentObservableList(int doctorID)
    {
    	
    	ObservableList<Experiment> experimentList = FXCollections.observableArrayList();
    	try
    	{
    
    		pstmt = conn.prepareStatement("select  experiments.id as id, experiments.name as name, drugs.name as drug_name"
    				+ " from"
    				+ " (select * from experiments"
    				+ " inner join doctors_experiments on experiments.id = doctors_experiments.experiment_id"
    				+ " where doctors_experiments.doctor_id = ?) as experiments"
    				+ " inner join drugs on experiments.drug_id = drugs.id ");
    		pstmt.setInt(1, doctorID);
    		rs = pstmt.executeQuery();
  	        while (rs.next()) 
  	        {
  	            experimentList.add(new Experiment(
  	            rs.getInt("id"), rs.getString("name"),
  	            rs.getString("drug_name")));
  	        }
		} 
    	catch (SQLException ex)
    	{
			System.out.println(ex);
		}
    	
    	return experimentList;
    }
    
    /**
     * funkcja sluzaca do ustawiania listy z leczonymi pacjentami
     * @param experimentID
     */
    public void initCuredObservableList(int experimentID)
    {
    	curedList.clear();
    	
    	try
    	{
    		pstmt = conn.prepareStatement("select * from patients where experiment_id = ? and"
    				+ " cured = ?");
    		pstmt.setInt(1, experimentID);
    		pstmt.setBoolean(2, true);
    		rs = pstmt.executeQuery();
  	        while (rs.next())
  	        {
  	            curedList.add(new Patient(
  	            rs.getInt("id"), rs.getString("name"),
  	            rs.getString("surname"), rs.getString("PESEL"),
  	            rs.getInt("experiment_id"), rs.getBoolean("cured"), 
  	            rs.getString("dose"), rs.getString("effect")));
  	        }
		} 
    	catch (SQLException ex) 
    	{
			System.out.println(ex);
		}
    }
    
    /**
     * funkcja sluzaca do ustawiania listy z pacjentami przyjmujacymi placebo
     * @param experimentID
     */
    public void initPlaceboObservableList(int experimentID)
    {
    	
    	placeboList.clear();
    	try 
    	{
    
    		pstmt = conn.prepareStatement("select * from patients where experiment_id = ? and"
    				+ " cured = ?");
    		pstmt.setInt(1, experimentID);
    		pstmt.setBoolean(2, false);
    		rs = pstmt.executeQuery();
  	        while (rs.next())
  	        {
  	            placeboList.add(new Patient(
  	            rs.getInt("id"), rs.getString("name"),
  	            rs.getString("surname"), rs.getString("PESEL"),
  	            rs.getInt("experiment_id"), rs.getBoolean("cured"), 
  	            rs.getString("dose"), rs.getString("effect")));
  	        }
		} 
    	catch (SQLException ex)
    	{
			System.out.println(ex);
		}
    }
	
    /**
     * funkcja sluzaca dozwracania par logi-haslo 
     * @return
     */
    public Map<String,String> getCredentials()
    {
    	HashMap<String,String> credentialsMap = new HashMap<String,String>();
    	try 
    	{
    		stmt = conn.createStatement();
    		rs = stmt.executeQuery("select login, password from doctors");
  	        while (rs.next()) 
  	        {
  	        	credentialsMap.put(rs.getString("login"), rs.getString("password"));
  	        }
		} 
    	catch (SQLException ex)
    	{
			System.out.println(ex);
		}
    	
    	return credentialsMap;
    }
    
    /**
     * funkcja sluzaca do zwracania id na podstawie loginu
     * @param login
     * @return
     */
    public int getLoginID(String login)
    {
    	int doctorID = 0; 
    	try 
    	{
    		stmt = conn.createStatement();
    		rs = stmt.executeQuery("select id from doctors where login = '" + login + "'");
    		rs.next();
    		doctorID = rs.getInt("id");
		} 
    	catch (SQLException ex)
    	{
			System.out.println(ex);
		}
    	
    	return doctorID;
    }

/**
 * funkcja sluzaca do zwracania imienia i nazwiska na podstawie id doktora
 * @param doctorID
 * @return
 */
public String getFullName(int doctorID)
{
	String fullName = "";
	try 
	{
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select name, surname from doctors where ID = " + doctorID);
		rs.next();
		String name = rs.getString("name");
		String surname = rs.getString("surname");
		fullName = name + " " + surname;
	} 
	catch (SQLException ex)
	{
		System.out.println(ex);
	}
	
	return fullName;
}

}