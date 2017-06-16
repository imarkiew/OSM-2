package mappedEntities;

public class Drug
{
	/**
	 * Dane o leku 
	 */
	private int id;
	private String name;
	private String disease;
	
	/**
	 * konstruktor klasy
	 * @param id
	 * @param name
	 * @param disease
	 */
	public Drug(int id, String name, String disease) 
	{
		super();
		this.id = id;
		this.name = name;
		this.disease = disease;
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
	public String getDisease() 
	{
		return disease;
	}
	public void setDisease(String disease) 
	{
		this.disease = disease;
	}
}
