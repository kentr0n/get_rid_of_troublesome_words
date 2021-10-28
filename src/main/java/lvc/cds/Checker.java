//this code is exactly the same as the CheckerThread but this codee does not have synchronized blocks
//check CheckerThread for comments about each method and what not

package lvc.cds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Checker{
    ArrayList<String> troublesomePhrases= new ArrayList<String>();
    HashMap<String,Integer> phraseCounter = new HashMap<>();
    HashMap<String,Integer> troublesomeFiles = new HashMap<>();
    ArrayList<File> moderate;

    public Checker(){
        try(Scanner sc = new Scanner(new FileInputStream("src/main/java/lvc/cds/TroublesomePhrases"))){
            while(sc.hasNextLine()){
                String word = sc.nextLine();

                if(word.contains(".")){
                    word=word.replace(".","");
                }
                
                if(word.contains(",")){
                    word=word.replace(",","");
                }

                if(word.contains(";")){
                    word=word.replace(";","");
                }

                if(word.contains(" ")){
                    troublesomePhrases.add("-"+ word.toLowerCase().replaceAll(" ","- -")+"-");
                } else{
                    troublesomePhrases.add("-"+ word.toLowerCase()+"-");
                }
                //System.out.println(troublesomePhrases);
            }
        } catch (FileNotFoundException e){
            System.out.println("Trouble some phrases file not found.");
            
        }
    }


    public void checkPhrase(String filename){
        int individualfilecount=0;
        File file = new File(filename);
        String filebutstring="";
        
        try(Scanner sc2 = new Scanner(new FileInputStream(file))){
            while(sc2.hasNext()){
                String word = sc2.next();

                if(word.contains(".")){
                    word=word.replace(".","");
                }
                
                if(word.contains(",")){
                    word=word.replace(",","");
                }

                if(word.contains(";")){
                    word=word.replace(";","");
                }


                filebutstring=filebutstring+"-"+word.toLowerCase()+ "- ";
            }

     
            //System.out.println(filebutstring);
        } catch (FileNotFoundException e){
            System.out.println(filename+" not found.");
            
        }

        for(String t: troublesomePhrases){
            if (filebutstring.contains(t)){
                String counterString = filebutstring.replaceAll(t, "-");
                filebutstring=filebutstring.replaceAll(t, "");

                List ctrArray= Arrays.asList(counterString.split(" "));
                int numofthatphrase = Collections.frequency(ctrArray, "-");
                individualfilecount=individualfilecount+numofthatphrase;

                //System.out.println(numofthatphrase +" "+ t);

                    if(phraseCounter.containsKey(t)){
                        phraseCounter.put(t, phraseCounter.get(t)+ numofthatphrase);
                  } else{
                      phraseCounter.put(t, 1);
                  }
            }
        }

        //if there are more than one phrase/word that are trouble some add to the hashmap of troublesome files with its count of the troublesome phrases and words
            if(individualfilecount>0){
                //assuming we do not have duplicate filenames
                troublesomeFiles.put(filename,individualfilecount);
            }
    
        System.out.println();
    }

    //prints results
    public void print(){
        System.out.println("SERIAL TEST");
        System.out.println("Troublesome Files with amount of troublesome words or phrases");
        for(String k : troublesomeFiles.keySet()){
            System.out.println(k.replace("-","")  + ": " + troublesomeFiles.get(k));
        }
        
        System.out.println();

        System.out.println("Occurances of each Troublesome phrases/words");
        for(String k : phraseCounter.keySet()){
            System.out.println(k.replace("-","")  + ": " + phraseCounter.get(k));
        }

        System.out.println();
    }

    public static void main(String[] args){
        long startTime = System.currentTimeMillis();
        Checker check= new Checker();
        try(Scanner sc = new Scanner(new FileInputStream("src/main/java/lvc/cds/filenamestocheck.txt"))){
            while(sc.hasNextLine()){
                String file = sc.nextLine();
                check.checkPhrase(file);
            }
        
        } catch (FileNotFoundException e){
            System.out.println("File names not found.");
        }

        check.print();
        System.out.println("Time for Serial Code in Milliseconds: " + (System.currentTimeMillis()-startTime));
    }


    
}