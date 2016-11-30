/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import zuulFramework.worldofzuul.entities.Item;
import zuulFramework.worldofzuul.entities.ItemType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Christian
 */
public class HighScore {
	private Game game;

	public HighScore(Game game) {
		this.game = game;

	}

	/**
	 * Reads from a .txt file and prints the first five lines
	 *
	 * @return
	 */
	public static ObservableList<Integer> showScore() {
		ObservableList<Integer> scoreList = FXCollections.observableArrayList();
		//Tries to run the code below, if the code cannot be run,
		//then the catch line is run instead.
		try {
			File file = new File("score.txt");
			Scanner scanner = new Scanner(file);
			//If the there is a next line in the .txt file then it adds
			//the next line from the .txt file and converts the string to an
			//int and puts it in an ArrayList.
			while (scanner.hasNextLine()) {
				scoreList.add(Integer.parseInt(scanner.nextLine()));
			}
			for (int iteration = 0; iteration < scoreList.size(); iteration++) {
				int endOfArray = scoreList.size() - iteration;
				boolean swapped = false;
				//compares element indeks and index+1 in the array, if index+1 >index swap them.
				//This will sort the highest numbers in to the lowest indexes.
				for (int index = 0; index < endOfArray - 1; index++) {
					if (scoreList.get(index) < scoreList.get(index + 1)) {
						Integer temp = scoreList.get(index);
						scoreList.set(index, scoreList.get(index + 1));
						scoreList.set(index + 1, temp);
						swapped = true;
					}
				}
				//If the numbers don't swap then it means the numbers are in
				//order of size and then sorting is over.
				if (!swapped) {
					break;
				}
			}
		} catch (FileNotFoundException ex) {

		}
		return scoreList;
	}

	/**
	 * Calculates the score and prints it into a .txt file
	 *
	 * @param listOfItems
	 * @return Gives the calculated score.
	 */
	public int calcScore(ObservableList<ItemType> listOfItems) {
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
			if (s.contains(listOfItems.get(j))) {
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

}
