package db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.derby.jdbc.EmbeddedDriver;


public class DbCreator 
{
	   /**
	    * funkcja main z instancja klasy sluzaca do zalozenia bazy danych
	    * @param args
	    */
	   public static void main(String[] args) 
	   {
	      DbCreator e = new DbCreator();
	      e.createTables();
	   }
	   
	   /**
	    * Tworzenie tabel
	    */
	   public void createTables() 
	   {
	      Connection conn = null;
	      Statement stmt;
	      String createDoctorTable = "create table doctors ("
	    		  + " id integer not null generated always as identity (start with 1, increment by 1),"
			      + " name varchar(20) not null,"
			      + " surname varchar(30) not null,"
			      + " PESEL varchar(11) not null,"
			      + " login varchar(15) not null,"
			      + " password varchar(15) not null,"
			      
			      + " constraint unique_doctor_identifier primary key (id))";
	      
	      String createDrugTable = "create table drugs ("
	    		  + " id integer not null generated always as identity (start with 1, increment by 1),"
	    	      + " name varchar(20) not null,"
	    	      + " disease varchar(30) not null,"
	    	      
	    	      + " constraint unique_drug_identifier primary key (id)) ";
	      
	      String createExperimentTable = "create table experiments ("
	    		  + " id integer not null generated always as identity (start with 1, increment by 1),"
	    	      + " name varchar(30) not null,"
	    	      + " drug_id integer not null,"
	    	      
	    	      + " constraint unique_experiment_identifier primary key (id),"
	    	      + " constraint tested_drug foreign key (drug_id) references drugs(id))";
	      
	      String createPatientTable = "create table patients ("
	    		  + " id integer not null generated always as identity (start with 1, increment by 1),"
	    	      + " name varchar(20) not null,"
	    	      + " surname varchar(30) not null,"
	    	      + " PESEL varchar(11) not null,"
	    	      + " experiment_id integer not null,"
	    	      + " cured boolean,"
	    	      + " dose varchar(15),"
	    	      + " effect varchar(50),"
	    	      
	    	      + " constraint unique_patient_identifier primary key (id),"
	    	      + " constraint attend_experiment foreign key (experiment_id) references experiments(id))";
	      
	      String createDoctorsExperimentsTable = "create table doctors_experiments ("
	      		+ " doctor_id integer not null, "
	      		+ " experiment_id integer not null,"
	      		
	      		+ " constraint doctor_pointer foreign key (doctor_id) references doctors(id),"
	      		+ " constraint experiment_pointer foreign key (experiment_id) references experiments(id))";
	      
	      try 
	      {
	         Driver derbyEmbeddedDriver = new EmbeddedDriver();
	         DriverManager.registerDriver(derbyEmbeddedDriver);
	         conn = DriverManager.getConnection("jdbc:derby:medicalDatabase");
	         conn.setAutoCommit(true);
	         stmt = conn.createStatement();
	         stmt.execute(createDoctorTable);
	         stmt.execute(createDrugTable);
	         stmt.execute(createExperimentTable); 
	         stmt.execute(createPatientTable);
	         stmt.execute(createDoctorsExperimentsTable);
	      } 
	      catch (SQLException ex) 
	      {
	         System.out.println(ex);
	      }

	      try 
	      {
	         DriverManager.getConnection("jdbc:derby:;shutdown=true");
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
	}