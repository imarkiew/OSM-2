package tools;

import java.util.regex.Pattern;

public class Validation
{
	/**
	 * Funkcja zwraca prawde, gdy string jest liczba calkowita
	 * @param string
	 * @return
	 */
	public static boolean isInt(String string) 
	{
		boolean info = true;
		
		try
		{
			
			Long.parseLong(string);
		
		}
		catch(NumberFormatException e)
		{
			info = false;
		}
		
		return info;
	}
	
	/**
	 * Funkcja zwraca prawde gdy string zawiera tylko duze i male litery
	 * @param value
	 * @return
	 */
	public static boolean isName(String value)
	{
		boolean info = true;
		
		if(!Pattern.matches("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*", value))
			info = false;
			
			return info;
	}
	
}
