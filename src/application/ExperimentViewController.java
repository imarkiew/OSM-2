package application;

import db.DbManager;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import mappedEntities.Experiment;

public class ExperimentViewController extends Application
{
	/**
	 * identyfikator doktora
	 */
	private int doctorID;
	/**
	 * etykieta z wyswietlajaca imie i nazwisko zalogowanego lekarza
	 */
	@FXML
	private Label loggedAsLabel;
	/*
	 * Kontrolki widoku z wyborem eksperymentu
	 */
	@FXML
	private TableView<Experiment> experimentTable;
	@FXML
	private TableColumn<Experiment, String> nameColumn;
	@FXML
	private TableColumn<Experiment, String> drugColumn;
	@FXML
	private Label chooseExperimentlabel;
	@FXML
	private Button chooseExperimentButton;
	/**
	 * singleton klasy DbManager
	 */
	DbManager manager = DbManager.getInstance();
	/**
	 * Lista eksperymentow
	 */
	ObservableList<Experiment> experimentList;
	
	/**
	 * Konstruktor klasy
	 * @param doctorID
	 */
	public ExperimentViewController(int doctorID)
	{
		super();
		this.doctorID = doctorID;
	}

	@Override
	/**
	 * Funkcja start, wymagana do nadpisania 
	 */
	public void start(Stage primaryStage) throws Exception 
	{}
	
	/**
	 * Funkcja do inicjalizacji tabeli z danymi o eksperymentach
	 */
	public void initialize()
	{
		experimentList = manager.getExperimentObservableList(doctorID);
		nameColumn.setCellValueFactory(new PropertyValueFactory<Experiment, String>("name"));
		drugColumn.setCellValueFactory(new PropertyValueFactory<Experiment, String>("drugName"));
		experimentTable.setItems(experimentList);
		String doctorFullName = manager.getFullName(doctorID);
		loggedAsLabel.setText("Zalogowano jako:\n" + doctorFullName);
	}
	
	/**
	 * Wywolanie przejscia do statystyk eksperymentu z odpowiednimi danymi
	 */
	public void goToExperiment()
	{
		int chosenExperimentIndex = experimentTable.getSelectionModel().getSelectedIndex();
		int experimentID = experimentList.get(chosenExperimentIndex).getId();
		String experimentName = experimentList.get(chosenExperimentIndex).getName();
		switchToPatientStatsView(experimentID, experimentName) ;
		
	}
	
	/**
	 * zmiana widoku na widok statystyk odpowiedniego eksperymentu
	 * @param experimentID
	 */
	public void switchToPatientStatsView(int experimentID, String experimentName)
	{
		try
		{
			Stage primaryStage = (Stage)chooseExperimentButton.getScene().getWindow();
			PatientsStatsViewController patientsStatsViewController = new PatientsStatsViewController(
					experimentID, experimentName);
			FXMLLoader loaderPatientsStatsView = new FXMLLoader( getClass().getResource("PatientsStatsView.fxml"));
			loaderPatientsStatsView.setController(patientsStatsViewController);
			Parent root = loaderPatientsStatsView.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Program wspomagaj¹cy testowanie leków");
			primaryStage.show();
			primaryStage.setOnCloseRequest(e-> manager.disconnect());	
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	public void tableClicked(){
		int chosenExperimentIndex = experimentTable.getSelectionModel().getSelectedIndex();
		if (chosenExperimentIndex != -1){
			chooseExperimentButton.setDisable(false);
		}
		else{
			chooseExperimentButton.setDisable(true);
		}
	}
}
