import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;


public class CardGame {
    public static void main(String[] args)
    {
        // Create scanner for input
        Scanner scanner = new Scanner(System.in);

        // Ask for input on number of players
        System.out.println("Please enter the number of players:");
        int numPlayers = scanner.nextInt();

        // Ask for input on file location
        System.out.println("Please enter location of the pack to load:");
        String path = scanner.next();

        // Load the deck from file given and check the file is valid
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            ArrayList<Card> cards = new ArrayList<Card>();
            while (lines.size() > 0)
            {
                String line = lines.get(lines.size() - 1);
                Card lineCard = new Card(Integer.valueOf(line));
                lines.remove(line);
                cards.add(lineCard);
            }
        } catch (IOException e) {
            System.out.println("Invalid pack file.");
        }
    }
}