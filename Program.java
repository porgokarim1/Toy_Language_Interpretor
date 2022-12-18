import java.util.ArrayList;
import java.util.regex.*;
/*The program class will be the main brain of all computation*/

public class Program {

//the assignment container will accumulate each statement one by one
   public static ArrayList<Assignment> assignments = new ArrayList<>() ;

   //function to store the stament as long as it is a valid expression, otherwise throw error
   public void newAssignment(String str){
      Assignment assignment = new Assignment(str);
      if(!assignment.isValidAssignment()){
        throw new RuntimeException("Not valid assignment");
      }
      assignments.add(assignment);  
   }

   //Function to read the assignment data charactere by character and return the integer value
   public static int getExpression(String identifier){
      int x=0;
      if(assignments.size()> 0){
         for(int i=0; i< assignments.size(); i++){
       
            if(assignments.get(i).getIdentifier().equals(identifier)){
               x = assignments.get(i).getExpValue();
               break;
            }
         }
         return x;
      }
      else{
         throw new RuntimeException("error can't find identifier");
      }
   }
   // print function to display the computed data
   public void printData(){
      for(int i=0; i< assignments.size(); i++){
         assignments.get(i).printData();
      }
   }
  
}// end of program class

//Assignment class will handle all statement on each line
class Assignment{
    private String assignment;
    private Identifier identifier;
    private Parser expression;
    public Assignment(String assignment){
        this.assignment = assignment;
    }
    // check if the stament is valid, for example x=2; return true
    public boolean isValidAssignment(){
        if(!assignment.endsWith(";")){
           return false;
        }
        if( !assignment.contains("=")){
            return false;
         }
         // In cas of good statement, split the statement in 2. left side is identifiyer, right side is assignee 
        String[] array = assignment.split("=");
            
        if(array.length != 2){
           return false;
        }

        //check if lefter side is a valid identifier
        identifier= new Identifier(array[0].trim()) ;
        if(!identifier.isValidIdentifier()){
            return false;
        }
        expression = new Parser(array[1].replace(";", "").trim());
    
        return true;
    }

     int getExpression(){
        return expression.eval();
     }
     int getExpValue(){
        return expression.getExpression();
     }
     String getIdentifier(){
        return identifier.getIdentifier();
    }
    public void printData(){
        System.out.println(getIdentifier() + " = "+getExpression());
     }

}
/*
   This class take a character(a.k.a assignment) to validate if an identifier or return identifier value
*/
class Identifier{
   private String s;

   public Identifier(String str){
      s= str;
   }
   boolean isValidIdentifier(){
      String pattern = "^([a-zA-Z]|_)(([a-zA-Z]|_)|[0-9])*";
      //check for validation
      return Pattern.matches(pattern, s);
   
   }
   
   String getIdentifier(){
      if(isValidIdentifier()){
         return s;
      }
      else{
         throw new RuntimeException("error");
      }
     
   }
}// end identifier class

/*Parser class will be crucial into reading and tabling the data */
class Parser{
    private String s;
    private int expValue;
    private int currIndex;
    private int n;
    private char inputToken;
    
    public Parser(String s){
       this.s = s;
       currIndex = 0;
       n = s.length();
       nextToken();
    }
 
    void nextToken(){
       char c;
       do {
          if (currIndex == n){
             inputToken = '$';
             return;
          }
          c = s.charAt(currIndex++);
       } while (Character.isWhitespace(c));
       inputToken = c;
    }
   
    void match(char token){
       if (inputToken == token){
          nextToken();
       } else {
          throw new RuntimeException("syntax error");
       }
    }
    int getExpression(){
       return expValue;
    }
    int eval(){
    
       int x = exp();
       if (inputToken == '$'){
          expValue = x;
          return x;
       } else {
          throw new RuntimeException("syntax error");
       }
    }
   
    int exp(){
       int x = term();
       while (inputToken == '+' || inputToken == '-'){
          char op = inputToken;
          nextToken();
          int y = term();
          x = apply(op, x, y);
       }
       return x;
    }
   
    int term(){
       int x = factor();
       while (inputToken == '*' || inputToken == '/'){
          char op = inputToken;
          nextToken();
          int y = factor();
          x = apply(op, x, y);
       }
       return x;
    }
   
    int factor(){
       int x;
       if(inputToken == '('){
      
          nextToken();
          x = exp();
          match(')');
          return x;
       }
       // if there a negative sign return the opposite value.
       else if(inputToken == '-'){
      
          nextToken();
          x= factor();
          if(x == 0){
             throw new RuntimeException("error");
          }
          return -x;
       }
       else if(inputToken == '+'){
          nextToken();
          x= factor();
          return x;
       }
       // if current char is digit it might be a literal
       else if(Character.isDigit(inputToken)){
     
          x= literal();
          return x;
       }
       // if current char is a letter or '_' it might be an identifier
       else if(Character.isLetter(inputToken) || inputToken == '_'){
          x =getValue();
          
          return x;
       }
       
       else{
          throw new RuntimeException("error");
        
       }
       
       
    }
    //This function accumlated digits and return the corresponding integer
    int literal(){
       String str= "";
       String patern = "0|^[1-9]\\d*";
       int x;
       do{
          str += inputToken;
          nextToken();
       }while(Character.isDigit(inputToken));
        //Always check for validation
       boolean validated =Pattern.matches(patern, str);
       if(validated){
          x = Integer.parseInt(str);
       }
       else{
          throw new RuntimeException("error");
       }
        
       return x;
    }

    //fetch the value assigned to the identifier and return it
    int getValue(){
       String str= "";
       char  c =inputToken;
       int x;
       do{
         
          str += c;
          nextToken();
          c =inputToken;
            
       }while(c !='+' && c !='-' && c !=')' && c !='$' && c !='*' && c !='/');
       
        //check for validation
       Identifier id = new Identifier(str);
       String var = id.getIdentifier();
       x = Program.getExpression(var);
       return x;
    }
    // This is the calculator function
    int apply(char op, int x, int y){
       int z = 0;
       switch (op){
          case '+': z = x + y; 
             break;
          case '-': z = x - y; 
             break;
          case '*': z = x * y; 
             break;
          case '/': z = x / y;
             break;
       }
       return z;
    }
}// end parser function
