package application;

import db.DbManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import tools.Loger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class StartViewController extends Application 
{
	/*
	 *Kontrolki widoku startowego
	 */
	@FXML
	private Button logButton;
	@FXML 
	private TextField loginField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label infoLabel;
	
	/**
	 * Singleton klasy DbManager
	 */
	DbManager manager = DbManager.getInstance();
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	public void start(Stage primaryStage)
	{
		try 
		{
			FXMLLoader loaderStart = new FXMLLoader(getClass().getResource("StartView.fxml"));
			Parent root = loaderStart.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Program wspomagaj¹cy testowanie leków");
			primaryStage.show();
			primaryStage.setResizable(false);
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
		primaryStage.setOnCloseRequest(e-> manager.disconnect());
	}
	
	/**
	 * Funkcja startowa
	 * @param args
	 */
	public static void main(String[] args) 
	{
		launch(args);
	}
	
	/**
	 * Funkcja do logowania
	 */
	public void SignIn()
	{
		String login = loginField.getText();
		String password = passwordField.getText();
		if(Loger.authenticate(login, password)){
			int doctorID = manager.getLoginID(login);
			switchToChooseView(doctorID);
			
		}
		else
		{
			infoLabel.setVisible(true);
			infoLabel.setText("B³êdny login lub has³o");
		}
		
	
	}
	
	/**
	 * Przelaczenie sie do wybranego widoku eksperymentu
	 * @param doctorID
	 */
	public void switchToChooseView(int doctorID)
	{
		try
		{
			Stage primaryStage = (Stage)logButton.getScene().getWindow();
			ExperimentViewController experimentViewController = new ExperimentViewController(doctorID);
			FXMLLoader loaderExperimentView = new FXMLLoader(getClass().getResource("ChooseExperimentView.fxml"));
			loaderExperimentView.setController(experimentViewController);
			Parent root = loaderExperimentView.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Program wspomag¹jacy testowanie leków");
			primaryStage.show();
			primaryStage.setOnCloseRequest(e-> manager.disconnect());
			
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
}
