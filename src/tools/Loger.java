package tools;

import java.util.Map;

import db.DbManager;

public class Loger 
{
	/**
	 * Funkcja do autentykacji uzytkownika
	 * @param login
	 * @param password
	 * @return
	 */
	public static boolean authenticate(String login, String password)
	{
		DbManager manager = DbManager.getInstance();
		Map<String,String> credentials = manager.getCredentials();
		return credentials.containsKey(login) && credentials.get(login).equals(password);
	}
}
