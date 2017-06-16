package mappedEntities;

public class DoctorsExperiments 
{	
	/**
	 * Dane bindujace 
	 */
	private int doctor_id;
	private int experiment_id;
	
	/**
	 * konstruktor klasy 
	 * @param doctor_id
	 * @param experiment_id
	 */
	public DoctorsExperiments(int doctor_id, int experiment_id)
	{
		super();
		this.doctor_id = doctor_id;
		this.experiment_id = experiment_id;
	}
	
	/**
	 * gettery i settery
	 */
	public int getDoctor_id()
	{
		return doctor_id;
	}
	
	public void setDoctor_id(int doctor_id) 
	{
		this.doctor_id = doctor_id;
	}
	
	public int getExperiment_id() 
	{
		return experiment_id;
	}
	
	public void setExperiment_id(int experiment_id) 
	{
		this.experiment_id = experiment_id;
	}
}
