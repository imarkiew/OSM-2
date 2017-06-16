package mappedEntities;

public class Doctor
{	
	/**
	 * danek lekarza
	 */
	private int id;
	private String name;
	private String surname;
	private String PESEL;
	private String login;
	private String password;
	
	/**
	 * konstruktor klasy
	 * @param id
	 * @param name
	 * @param surname
	 * @param pESEL
	 * @param login
	 * @param password
	 */
	public Doctor(int id, String name, String surname, String pESEL, String login, String password)
	{
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		PESEL = pESEL;
		this.login = login;
		this.password = password;
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
	
	public String getLogin()
	{
		return login;
	}
	
	public void setLogin(String login)
	{
		this.login = login;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}
}
