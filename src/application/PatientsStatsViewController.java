package application;


import java.util.Random;
import db.DbManager;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import mappedEntities.Patient;


public class PatientsStatsViewController extends Application 
{
	/**
	 * id eksperymentu
	 */
	private int experimentID;
	/**
	 * nazwa eksperymentu
	 */
	private String experimentName;
	/**
	 * id wybranego obecnie pacjenta
	 */
	private int chosenPatientID;
	/**
	 * editedPatientGroup przechowuje grupe
	 * zainicjalizowana, zeby uniknac nullPointerException przy wywolaniu funkcji updatePatient
	 * bez wyboru edytowanego pacjenta
	 */
	private String editedPatientGroup = "";
	/**
	 * Label z nazwa eksperymentu
	 */
	@FXML
	private Label experimentNameLabel;
	/**
	 * TabPane grupujacy Tab do edycji oraz dodawania
	 */
	@FXML
	private TabPane editableTabPane;
	
	
	/**
	 * Tab do edycji
	 */
	@FXML
	private TextField efectEditField;
	@FXML
	private TextField patientSurnameEditField;
	@FXML
	private TextField peselEditField;
	@FXML
	private Button confirmButton;
	@FXML
	private TextField doseEditField;
	@FXML
	private TextField patientNameEditField;
	@FXML
	private Label editInfoLabel;
	/**
	 * Tab do dodawania
	 */
	@FXML
	private TextField patientNameField;
	@FXML
	private TextField patientSurnameField;
	@FXML
	private TextField peselField;
	@FXML
	private Button addPatientButton;
	@FXML
	private Label addInfoLabel;
	/**
	 * TabPane grupujacy Tab z leczonymi pacjentami i Tab z dostajacymi placebo
	 */
	@FXML
	private TabPane statsTabPane;
	
	/**
	 * Tab z leczonymi pacjentami
	 */
	@FXML
	private Tab curedTab;
	@FXML
	private TableView<Patient> curedTable;
	@FXML
	private TableView<Patient> placeboTable;
	@FXML
	private TableColumn<Patient, String> curedNameColumn;
	@FXML
	private TableColumn<Patient, String> curedSurnameColumn;
	@FXML
	private TableColumn<Patient, String> curedPeselColumn;
	@FXML
	private TableColumn<Patient, String> curedDoseColumn;
	@FXML
	private TableColumn<Patient, String> curedEffectColumn;
	/**
	 * Tab z pacjentami dostajacymi placebo
	 */
	@FXML
	private Tab placeboTab;
	@FXML
	private TableColumn<Patient, String> placeboNameColumn;
	@FXML
	private TableColumn<Patient, String> placeboSurnameColumn;
	@FXML
	private TableColumn<Patient, String> placeboPeselColumn;
	/**
	 * singleton klasy DbManager
	 */
	DbManager manager = DbManager.getInstance();
	
	/**
	 * konstruktor klasy 
	 * @param experimentID
	 */
	public PatientsStatsViewController(int experimentID, String experimentName)
	{
		super();
		this.experimentID = experimentID;
		this.experimentName = experimentName;
	}

	@Override
	/**
	 * funkcja start, wymagana do nadpisania
	 */
	public void start(Stage primaryStage) throws Exception 
	{}
	
	/**
	 * Funkcja do inicjalizacji tabel
	 */
	public void initialize()
	{
		manager.initCuredObservableList(experimentID);
		ObservableList<Patient> curedList= manager.getCuredList();
		curedNameColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
		curedSurnameColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("surname"));
		curedPeselColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("PESEL"));
		curedDoseColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("dose"));
		curedEffectColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("effect"));
		curedTable.setItems(curedList);
		manager.initPlaceboObservableList(experimentID);
		ObservableList<Patient> placeboList= manager.getPlaceboList();
		placeboNameColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
		placeboSurnameColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("surname"));
		placeboPeselColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("PESEL"));
		placeboTable.setItems(placeboList);
		editableTabPane.getSelectionModel().select(0);
		statsTabPane.getSelectionModel().select(0);
		experimentNameLabel.setText("Eksperyment : " + experimentName);
	}
	
	/**
	 * Funkcja odswieza tabele z pacjentami przyjmujacymi lek
	 */
	public void refreshCuredTable()
	{
		 manager.initCuredObservableList(experimentID);
		 ObservableList<Patient> curedList= manager.getCuredList();
		 curedTable.getSelectionModel().select(null);
		 curedTable.setItems(curedList);
		 curedTable.refresh();
	}
	
	/**
	 * Funkcja odswieza tabele z pacjentami przyjmujacymi placebo 
	 */
	public void refreshPlaceboTable()
	{
		 manager.initPlaceboObservableList(experimentID);
		 ObservableList<Patient> placeboList= manager.getPlaceboList();
		 placeboTable.getSelectionModel().select(null);
		 placeboTable.setItems(placeboList);
		 placeboTable.refresh();
	}
	
	
	/**
	 * Funkcja zapisujaca do bazy danych pacjenta.
	 */
	public void addPatient()
	{
		addInfoLabel.setText("");
		String patientName = patientNameField.getText();
		String patientSurname = patientSurnameField.getText();
		String patientPesel = peselField.getText();
		boolean response = false;
		Random r = new Random();
		int x = r.nextInt(2);
		
		if(x == 0)
		{
			response = manager.addPatient(patientName, patientSurname, patientPesel, experimentID, false, null, null);
	
		}
		else
		{
			response = manager.addPatient(patientName, patientSurname, patientPesel, experimentID, true, null, null);
		}
		
		if(response && x == 0)
		{
			statsTabPane.getSelectionModel().select(1);
			refreshPlaceboTable();
			clearAddForm();
		}
		else if(response && x == 1)
		{
			statsTabPane.getSelectionModel().select(0);
			refreshCuredTable();
			clearAddForm();
		}
		else
		{
			addInfoLabel.setText("B³¹d wprowadzania danych !");
		}
	}
	
	/**
	 * Czyszczenie formatki edycji
	 */
	public void clearEditForm()
	{
		patientNameEditField.clear();
		patientSurnameEditField.clear();
		peselEditField.clear();
		doseEditField.clear();
		efectEditField.clear();
		editInfoLabel.setText("");
	}
	
	/**
	 * Czyszczenie formatki dodawania
	 */
	public void clearAddForm()
	{
		patientNameField.clear();
		patientSurnameField.clear();
		peselField.clear();
		addInfoLabel.setText("");
	}
	
	/**
	 * Obsluga klikniecia na tabeli, powodujaca wypelnienie sie formatki edycji
	 */
	public void actionOnTable()
	{
		clearAddForm();
		editInfoLabel.setText("");
		String selectedTab = statsTabPane.getSelectionModel().getSelectedItem().getText();
		if(selectedTab.equals("Leczeni"))
		{	
			Patient patient = curedTable.getSelectionModel().getSelectedItem();
			if(patient != null)
			{	
				editedPatientGroup = "cured";
				editableTabPane.getSelectionModel().select(1);
				chosenPatientID = manager.getCuredList().get(curedTable.getSelectionModel().getSelectedIndex()).getId();
				clearEditForm();
				patientNameEditField.setText(patient.getName());
				patientSurnameEditField.setText(patient.getSurname());
				peselEditField.setText(patient.getPESEL());
				doseEditField.setDisable(false);
				efectEditField.setDisable(false);
				doseEditField.setText(patient.getDose());
				efectEditField.setText(patient.getEffect());
				
			}
			
		}
		else
		{	
			Patient placebo = placeboTable.getSelectionModel().getSelectedItem();
	
			if(placebo != null)
			{
				editedPatientGroup = "placebo";
				editableTabPane.getSelectionModel().select(1);
				chosenPatientID = manager.getPlaceboList().get(placeboTable.getSelectionModel().getSelectedIndex()).getId();
				clearEditForm();
				patientNameEditField.setText(placebo.getName());
				patientSurnameEditField.setText(placebo.getSurname());
				peselEditField.setText(placebo.getPESEL());
				doseEditField.setDisable(true);
				efectEditField.setDisable(true);
			}
		}
	}
	
	/**
	 * Funkcja aktualizujaca pacjenta
	 */
	public void updatePatient()
	{
		editInfoLabel.setText("");
		boolean response = false;
		String selectedTab = statsTabPane.getSelectionModel().getSelectedItem().getText();
		String patientName;
		String patientSurname;
		String patientPESEL;
		String patientDose;
		String patientEdit;
		
		if(editedPatientGroup.equals("cured"))
		{
			patientName = patientNameEditField.getText();
			patientSurname = patientSurnameEditField.getText();
			patientPESEL = peselEditField.getText();
			patientDose = doseEditField.getText();
			patientEdit = efectEditField.getText();
			response = manager.upadatePatientData(patientName, patientSurname, patientPESEL, patientDose, patientEdit, chosenPatientID);
			refreshCuredTable();
			statsTabPane.getSelectionModel().select(0);
		}
		else
		{
			patientName = patientNameEditField.getText();
			patientSurname = patientSurnameEditField.getText();
			patientPESEL = peselEditField.getText();
			response = manager.upadatePatientData(patientName, patientSurname, patientPESEL, null, null, chosenPatientID);
			refreshPlaceboTable();
			statsTabPane.getSelectionModel().select(1);
		}
		
		if(response && selectedTab.equals("Leczeni"))
		{
			refreshCuredTable();
			clearEditForm();
		}
		else if(response && selectedTab.equals("Placebo"))
		{
			refreshPlaceboTable();
			clearEditForm();
		}
		else
		{
			editInfoLabel.setText("B³¹d edycji danych !");
		}
	}
}
