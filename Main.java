import java.io.*;  // Import the File class
import java.util.Scanner; //  Scanner class  is needed to read text files
public class Main {
   public static void main(String[] args)throws IOException {
      Scanner myReader = new Scanner(new File(args[0]));
      Program program = new Program();

      //read data on file line by line
      while (myReader.hasNextLine()) {
         String data = myReader.nextLine();
         program.newAssignment(data);
      }      
     program.printData();
     myReader.close(); 
   }
}