/*
 * Made by Emilio Kartono
 * Purpose: To read and write data to a file
 * Date modified: June 8, 2015
*/

package com.javaphysicsengine.utils;

import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class File
{
  /*
    Post-condition: Returns the content of a file as an array of strings when given
    @param fileName The file name / path
    @return Returns the content of a file as an array of strings when given
  */
  public static String[] readAllLines(String fileName)
  {
    try
    {
      // Creating a new Scanner so I can read in a file
      Scanner myScanner = new Scanner(new FileReader(fileName));
      
      // Getting the number of lines in the file
      int numLines = 0;
      while(myScanner.hasNextLine())
      {
        String line = myScanner.nextLine();
        numLines ++;
      }
      myScanner.close();
      
      // Getting the content of file
      String[] lines = new String[numLines];
      myScanner = new Scanner(new FileReader(fileName));
      for (int i = 0; i < lines.length; i++)
        lines[i] = myScanner.nextLine();
      
      myScanner.close();
      
      return lines;
    }
    catch(FileNotFoundException error) 
    { 
      JOptionPane.showMessageDialog(null, "There was a problem with accessing \"" + 
                                    fileName + "\" \nTry using a different file name");
    }
    
    return new String[0];
  }
  
  /*
    Post-condition: Write lines of data to a file
    @param contents An array of lines containing data (String type)
    @param filePath The file path
  */
  public static void write(String[] contents, String filePath)
  {
    try
    {
      // Create a new print writter so that the contents can be saved in a file
      PrintWriter myFile = new PrintWriter(filePath);
      
      // Writting the content to the file
      for (int i = 0; i < contents.length; i++)
        myFile.println(contents[i]);
      
      // Need to close the file so that it can save in a file
      myFile.close();
    }
    catch(FileNotFoundException error)
    {
      JOptionPane.showMessageDialog(null, "There was a problem in saving file to \"" + 
                                    filePath + "\" \nTry using a different file name");
    }    
  }
}