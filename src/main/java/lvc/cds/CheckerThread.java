//my approach to concurrency is to run a thread for each file needed to be checked and having syncronized blocks when we change shared variables among threads
//serial code in the Checker.java file

//after timing the serial and concurrent version it is hard to tell which is faster (with little file sizes, trouble some phrases, and number to check), the concurrent version beats the serial version by a lot at times and at other times it does not or comes even with the serial version
//However, as we add more phrases that are trouble some and more bigger files it is safe to say that the concurrent version is faster and better than the serial version 

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


public class CheckerThread{
    ArrayList<String> troublesomePhrases= new ArrayList<String>(); //has all troublesome phrases
    HashMap<String,Integer> phraseCounter = new HashMap<>(); //trouble some phrases but with count
    HashMap<String,Integer> troublesomeFiles = new HashMap<>(); //files with trouble some phrases and its count
    ArrayList<File> moderate;
    int howmanyfileschecked=0; //this is used so we know when to print
    int numberoffiles=0; //this is used so we know when all files have been checked and we can print

    public CheckerThread(){
        try(Scanner sc = new Scanner(new FileInputStream("src/main/java/lvc/cds/TroublesomePhrases"))){
            while(sc.hasNextLine()){
                String word = sc.nextLine();

                //replace all periods, commas, and semicolons so we can check words and phrases correctly
                if(word.contains(".")){
                    word=word.replace(".","");
                }
                
                if(word.contains(",")){
                    word=word.replace(",","");
                }

                if(word.contains(";")){
                    word=word.replace(";","");
                }

                //this makes it so we can count phrases and words correctly
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
        int individualfilecount=0; //this counts how many trouble some phrases in a file
        File file = new File(filename);
        String filebutstring=""; //thos will have the whole file in one string
        
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
            //we go through all the trouble some phrases and if they are in the file we will count the phrase
            if (filebutstring.contains(t)){
                String counterString = filebutstring.replaceAll(t, "-");
                filebutstring=filebutstring.replaceAll(t, "");

                List ctrArray= Arrays.asList(counterString.split(" "));
                int numofthatphrase = Collections.frequency(ctrArray, "-");
                individualfilecount=individualfilecount+numofthatphrase;

                //System.out.println(numofthatphrase +" "+ t);


                //synchronize here since phraseCounter is a shared variable throughout threads 
                synchronized(this){
                    if(phraseCounter.containsKey(t)){
                        phraseCounter.put(t, phraseCounter.get(t)+ numofthatphrase);
                  } else{
                      phraseCounter.put(t, 1);
                  }
                }
            }
        }

        //if there are more than one phrase/word that are trouble some add to the hashmap of troublesome files with its count of the troublesome phrases and words
        //synchronized since troublesome files and howmanyfiles checked are shared among threads
        synchronized(this){
            if(individualfilecount>0){
                //assuming we do not have duplicate filenames
                troublesomeFiles.put(filename,individualfilecount);
            }
            howmanyfileschecked++;
            notifyAll();
        }
        System.out.println();
    }

    //prints results
    public void print(){
        System.out.println("THREAD TEST");
        System.out.println("Troublesome Files with amount of troublesome words or phrases");
        for(String k : troublesomeFiles.keySet()){
            System.out.println(k.replace("-","") + ": " + troublesomeFiles.get(k));
        }
        
        System.out.println();

        System.out.println("Occurances of each Troublesome phrases/words");
        for(String k : phraseCounter.keySet()){
            System.out.println(k.replace("-","")  + ": " + phraseCounter.get(k));
        }
    }

    public static void main(String[] args){
        long startTime = System.currentTimeMillis();
        CheckerThread check= new CheckerThread();
        try(Scanner sc = new Scanner(new FileInputStream("src/main/java/lvc/cds/filenamestocheck.txt"))){
            while(sc.hasNextLine()){
                check.numberoffiles++;
                String file = sc.nextLine();
                (new Thread(() -> check.checkPhrase(file))).start(); //here we make a thread for each file to check
            }
 
        } catch (FileNotFoundException e){
            System.out.println("File names not found.");
        }

        synchronized(check){
            while(check.numberoffiles!=check.howmanyfileschecked){
                try{
                    check.wait(); //wait and we only get out of the loop if we have gone through all of the files
                } catch(InterruptedException e){}
            }
            check.print();
        }

        System.out.println("Time for Concurretn Code in Milliseconds: " + (System.currentTimeMillis()-startTime)); //times

    }
    
}