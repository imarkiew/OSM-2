package mappedEntities;

public class Experiment
{
	/**
	 * Dane eksperymentu
	 */
	private int id;
	private String name;
	private String drugName;
	
	/**
	 * konstruktor klasy
	 * @param id
	 * @param name
	 * @param drugName
	 */
	public Experiment(int id, String name, String drugName) 
	{
		super();
		this.id = id;
		this.name = name;
		this.drugName = drugName;
	}

	/**
	 * gettery i settery
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

	public String getDrugName() 
	{
		return drugName;
	}

	public void setDrugName(String drugName) 
	{
		this.drugName = drugName;
	}
}
