package com.shaic.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public final class Utilities
{
  private static final String TOKEN = "#";
  
  /**
   * Checks the validity of a String Object.
   * @param a_value String object that has to be validated
   * @return boolean
   */
  public static boolean checkString(String a_value)
  {
    boolean result = true;
    if (a_value == null)
      result = false;
    else
    {
      a_value = a_value.trim();
      if (a_value.length() <= 0)
      {
        result = false;
      }
    }
    return result;
  }

 

  /**
   * This method will create one string by concatenating the list of values using
   * a token as value separator
   * @param a_values A List of objects 
   * @param a_token for stringing - will use default if null
   * @return String
   */
  public static String string(List a_values, String a_token)
  {
    String result = null;
    boolean flag = true;
    String token = null;
    if (checkString(a_token))
    {
      // token is valid
      token = a_token;
    }
    else
    {
      // no token supplied - use default
      token = TOKEN;
    }
    if (a_values.size() > 0)
    {
      StringBuffer buf = new StringBuffer();
      for(Object obj : a_values)
      {
        String value = obj.toString();
        if (checkString(value))
        {
          if (flag)
          {
            // should be first loop
            buf.append(value);
            flag = true;
          }
          else
          {
            buf.append(token);
            buf.append(obj);
          }
        }
      }
      result = buf.toString();
    }
    else
    {
      // do nothing - return null
    }
    return result;
  }

  /**
   * This method will parse the given string and extract the values
   * @param a_value String that has to be parsed
   * @param a_token for stringing - will use default if null
   * @return List
   */
  public static List<String> destring(String a_value, String a_token)
  {
    List<String> result = new ArrayList<String>();
    String token = null;
    if (checkString(a_token))
    {
      // token is valid
      token = a_token;
    }
    else
    {
      // no token supplied - use default
      token = TOKEN;
    }
    if (checkString(a_value))
    {
      StringTokenizer st = new StringTokenizer(a_value, token, false);
      while (st.hasMoreTokens())
      {
        String value = st.nextToken();
        if (checkString(value))
        {
          result.add(value);
        }
      }
    }
    else
    {
      // do nothing - return empty list
    }
    return result;
  }

  
}
