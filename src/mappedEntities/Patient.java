package mappedEntities;

public class Patient 
{
	/**
	 * dane pacjenta
	 */
	private int id;
	private String name;
	private String surname;
	private String PESEL;
	private int experiment_id;
	private boolean cured;
	private String dose;
	private String effect;
	
	/**
	 * konstruktor klasy
	 * @param id
	 * @param name
	 * @param surname
	 * @param pESEL
	 * @param experiment_id
	 * @param cured
	 * @param dose
	 * @param effect
	 */
	public Patient(int id, String name, String surname, String pESEL, int experiment_id, boolean cured, String dose, String effect) 
	{
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		PESEL = pESEL;
		this.experiment_id = experiment_id;
		this.cured = cured;
		this.dose = dose;
		this.effect = effect;
	}
	
	/**
	 * gettery i settery
	 * @return
	 */
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getSurname() 
	{
		return surname;
	}
	
	public void setSurname(String surname)
	{
		this.surname = surname;
	}
	
	public String getPESEL() 
	{
		return PESEL;
	}
	
	public void setPESEL(String pESEL) 
	{
		PESEL = pESEL;
	}
	
	public int getExperiment_id() 
	{
		return experiment_id;
	}
	
	public void setExperiment_id(int experiment_id)
	{
		this.experiment_id = experiment_id;
	}
	
	public boolean isCured() 
	{
		return cured;
	}
	
	public void setCured(boolean cured) 
	{
		this.cured = cured;
	}
	
	public String getDose()
	{
		return dose;
	}
	
	public void setDose(String dose)
	{
		this.dose = dose;
	}
	
	public String getEffect()
	{
		return effect;
	}
	
	public void setEffect(String effect) 
	{
		this.effect = effect;
	}
	
	@Override
	/**
	 * przeciazona metoda toString
	 */
	public String toString() 
	{
		return "Patient [id=" + id + ", name=" + name + ", surname=" + surname + ", PESEL=" + PESEL + ", experiment_id="
				+ experiment_id + ", cured=" + cured + ", dose=" + dose + ", effect=" + effect + "]";
	}
}
