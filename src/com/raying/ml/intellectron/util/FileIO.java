/***********************************************************
* Developer: Minhas Kamal (minhaskamal024@gmail.com)       *
* Date: 02-Feb-2014, 31-Dec-2014, 02-Apr-2016              *
* Website: https://github.com/MinhasKamal/Intellectron     *
* License: MIT License                                     *
***********************************************************/

package com.raying.ml.intellectron.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


/**
 * Contains file read & file write static operations.
 *
 * @author Minhas Kamal
 */
public class FileIO {
	/**
	 * Takes a file path & returns whole content of the file. Used for reading external
	 * files.
	 * @param filePath input file path
	 * @return full information of the file
	 * @throws Exception 
	 */
	public static String readWholeFile(String filePath) throws Exception{
		
		byte[] bytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(new File(filePath).toURI()));
		String Information = new String(bytes);	//contains the full file
			
		return Information; 
	}
	
	public static String readWholeFile(String filePath, String Encoding) throws Exception{
		
        return readWholeFile(new FileInputStream(filePath), Encoding);
	}
	
	public static String readWholeFile(InputStream inputStream, String Encoding) throws Exception{
		
		return readWholeFile(new InputStreamReader(inputStream, Encoding));
	}
	
	/**
	 * Takes a input stream & returns whole content of the file. Mainly used for reading
	 * files inside jar.
	 * @param filePath input file path
	 * @return full information of the file
	 * @throws Exception 
	 */
	public static String readWholeFile(InputStreamReader inputStreamReader) throws Exception{
		String string = "";	//for temporary data store
		String Information = "";	//#contains the full file 
		
		BufferedReader mainBR = new BufferedReader(inputStreamReader);
		
		string = mainBR.readLine();
		while(string!=null){	//reading step by step
			Information += string + "\n"; 
			
			string = mainBR.readLine();
		}
		
		mainBR.close();
		
		return Information;
	}
	
	
	
	/**
	 * Takes a file name, line number & returns specific line of the file 
	 * @param FileName
	 * @param lineNumber starts from 1
	 * @return
	 * @throws Exception 
	 */
	public static String readSpecificFileLine(String FileName, int lineNumber) throws Exception{
		String string = "";
		File mainFile = new File(FileName);
		
		
		/*file reading*/
		BufferedReader mainBR = new BufferedReader(new FileReader(mainFile));	//making buffered reader object
		
		string = mainBR.readLine();
		for(int i=1; i<lineNumber && string!=null; i++){
			string = mainBR.readLine();
		}
		
		mainBR.close();
		return string; 
	}
	
	/**
	 * Takes file path and information to be written as string and writes the whole string to the file at once.
	 * If file name already exists over-writes it, else creates new file. 
	 * @param FileName full file path
	 * @param Information information to be written
	 * @throws Exception 
	 */
	public static void writeWholeFile(String FileName, String Information) throws Exception{
		
		BufferedWriter mainBW = new BufferedWriter(new FileWriter(FileName));
		mainBW.write(Information);
		mainBW.close();
			
		return ;
	}
	
	public static void writeWholeFile(String FileName, String Information, String Encoding)  throws Exception{
		
		FileOutputStream outputStream = new FileOutputStream(FileName);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, Encoding);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        
        bufferedWriter.write(Information);
        bufferedWriter.close();
		
		return ;
	}
	
}
