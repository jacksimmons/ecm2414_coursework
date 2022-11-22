import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.nio.channels.spi.SelectorProvider;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PackGenerator {
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        // Create a scanner object
        Scanner scanner = new Scanner(System.in);

        // Ask for input from the player
        System.out.println("File name:");
        String filename = scanner.nextLine();

        System.out.println("Number of players:");
        int numPlayers = scanner.nextInt();

        scanner.close();

        // Get the inputted file
        Path file = Paths.get(filename);
        Files.write(file, "".getBytes());
        
        // All cards arraylist
        ArrayList<Card> cards = new ArrayList<Card>();
        
        for (int i=0; i < 8 * numPlayers; i++)
        {
            Card card = new Card(i % (numPlayers * 2) + 1);
            cards.add(card);
        }

        System.out.println(cards);

        // Write the cards one-by-one to the file
        while (cards.size() > 0)
        {
            Random random = new Random();
            Card card = cards.get(random.nextInt(cards.size()));
            Files.writeString(file, Integer.toString(card.getValue()) + "\n", StandardOpenOption.APPEND);
            cards.remove(card);
        }
    }
}
