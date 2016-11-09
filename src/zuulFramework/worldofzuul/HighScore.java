/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul;

import zuulFramework.worldofzuul.entities.Item;
import zuulFramework.worldofzuul.entities.ItemType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Christian
 */
public class HighScore {
    private Game game;
    
    public HighScore (Game game){
        this.game = game;
        
    }
    
    /**
     * Calculates the score and prints it into a .txt file
     * @param listOfItems
     * @return Gives the calculated score. 
     */
    public int calcScore(ItemType[] listOfItems) {
        
        int score = 0;
        // The HashSet prevent cases of duplicate item types being bought.
        Set<ItemType> s = new HashSet<ItemType>();
        List<Item> items = game.getPlayer().getBoughtItems();
        //Loops through the items the player has bought and adds them to a HashSet.  
        for (int i = 0; i < game.getPlayer().getBoughtItems().size(); i++) {
            s.add(items.get(i).getType());
        }
        //Loops through the HashSet and checks for unique item types and adds 10 to score for each unique item type. 
        for (int j = 0; j < s.size(); j++) {
            if (s.contains(listOfItems[j])) {
                score += 10;
            }
        }
        //Creating a multiplier that rewards the player for completing the game faster.
        for (int i = 12; i > 0; i--) {
            score = (int) (score * ((0.083 * i) + 1));
        }
        return score;
    }
/**
 * 
 * @param score The score that was calculated then gets written in a .txt file. 
 * @throws IOException  
 */
    public void printScoreToFile(int score) throws IOException {
        FileWriter fileWriter = null;
        //Creates a score.txt file and appends true so that new text is written
        //below existing text.
        fileWriter = new FileWriter("score.txt", Boolean.TRUE);
        //The score is converted into a string by adding an int with a string.
        String stringToWrite = score + "";
        //Writes the score in to the file and seperates the score with a line.
        fileWriter.write(stringToWrite + System.lineSeparator());
        //Checks that the score is written in the file.
        fileWriter.flush();
        //Closes the file.
        fileWriter.close();
    }
    

    /**
     * Reads from a .txt file and prints the first five lines
     */
    public void showScore() {
        ArrayList<Integer> score = new ArrayList<>();
        //Tries to run the code below, if the code cannot be run, 
        //then the catch line is run instead.  
        try {
            File file = new File("score.txt");
            Scanner scanner = new Scanner(file);
            //If the there is a next line in the .txt file then it adds 
            //the next line from the .txt file and converts the string to an
            //int and puts it in an ArrayList. 
            while (scanner.hasNextLine()) {
                score.add(Integer.parseInt(scanner.nextLine()));
            }
            for (int iteration = 0; iteration < score.size(); iteration++) {
                int endOfArray = score.size() - iteration;
                boolean swapped = false;
                //compares element indeks and index+1 in the array, if index+1 >index swap them.
                //This will sort the highest numbers in to the lowest indexes.
                for (int index = 0; index < endOfArray - 1; index++) {
                    if (score.get(index) < score.get(index + 1)) {
                        Integer temp = score.get(index);
                        score.set(index, score.get(index + 1));
                        score.set(index + 1, temp);
                        swapped = true;
                    }
                }
                //If the numbers don't swap then it means the numbers are in 
                //order of size and then sorting is over.
                if (!swapped) {
                    break;
                }
            }
            //Then it loops over the first 5 index numbers and prints them. 
            for (int count = 0; count < 5 && count < score.size(); count++) {
                System.out.println(score.get(count));
            }
        } catch (FileNotFoundException ex) {

        }
    }
    
    public void printScore(ItemType[] itemList) {
            try {
            int score = calcScore(itemList);
            System.out.println("Your score was " + score);
            printScoreToFile(score);
            System.out.println("Top 5 scores were");
            showScore();
        } catch (IOException ex) {
            System.out.println("IOException caught");
        }
    }
}
