import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;


public class CardGame {
    /* A class which handles the whole game.
     * It handles the beginning of the game (dealing to the players and the decks), then runs the Player threads.
     * The Player threads then continue the game.
     */
    public static void main(String[] args) throws InterruptedException
    {
        // Create scanner for input
        Scanner scanner = new Scanner(System.in);

        int numPlayers;
        ArrayList<Card> cards;
        
        while (true) {
            // Ask for input on number of players
            System.out.println("Please enter the number of players:");
            try {
                numPlayers = scanner.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid number of players.");
                scanner.next();
                continue;
            }

            // Ask for input on file location
            System.out.println("Please enter location of the pack to load:");
            String path = scanner.next();

            // Load the deck from file given and check the file is valid
            try {
                List<String> lines = Files.readAllLines(Paths.get(path));
                cards = new ArrayList<Card>();
                while (lines.size() > 0)
                {
                    String line = lines.get(lines.size() - 1);
                    Card lineCard = new Card(Integer.valueOf(line));
                    lines.remove(line);
                    cards.add(lineCard);
                }

                if (numPlayers * 8 != cards.size())
                {
                    System.out.println("Invalid pack file length.");
                    scanner.next();
                    continue;
                }

                break;

            } catch (IOException e) {
                System.out.println("Couldn't open pack file.");
                continue;
            }
        }

        scanner.close();

        ArrayList<Player> players = new ArrayList<>();
        ArrayList<CardDeck> decks = new ArrayList<>();

        for (int i=1; i <= numPlayers; i++)
        {
            CardDeck deck = new CardDeck(i);
            decks.add(deck);
        }

        for (int i=1; i <= numPlayers; i++)
        {
            int playerIndex = i-1;
            int leftNum = i;
            int rightNum = i + 1;

            if (i == numPlayers)
            {
                rightNum = 1 ;
            }

            Player player = new Player(i, decks.get(leftNum - 1), decks.get(rightNum - 1));
            players.add(player);

            Path path = Paths.get(player.getName() + "_output.txt");
            player.setOutputFile(path);

            // Generate the files here
            try {
                Files.write(path, "".getBytes());
            }
            catch (IOException e)
            {
                System.out.println("Failed to make output file for " + player.getName());
            }
        }

        for (int i=1; i <= numPlayers; i++)
        {
            players.get(i-1).setOtherPlayers(players);
        }

        boolean playersDealt = false;
        int playerIndex = 0;
        int deckIndex = 0;
        int playerCardNumber = 0;
        boolean checkedPlayers = false;

        while (cards.size() > 0)
        {
            Random random = new Random();
            Card card = cards.get(random.nextInt(cards.size()));

            if (!playersDealt)
            {
                if (playerIndex < players.size())
                {
                    Player player = players.get(playerIndex);
                    player.drawCard(card);
                    playerIndex++;
                    cards.remove(card);
                }
                else
                {
                    playerIndex = 0;
                    playerCardNumber++;
                    if (playerCardNumber > 3)
                    {
                        playersDealt = true;
                    }
                }
            }
            else
            {
                if (!checkedPlayers)
                {
                    for (int i=0; i < numPlayers; i++)
                    {
                        Player player = players.get(i);

                        if (player.checkHasWon()){
                            player.handleWin();
                        };
    
                        System.out.println(player.getName() + " " + player.getHandValues());
                        System.out.println(player.getLeft().getName() + " " + player.getRight().getName());
                    }
                    checkedPlayers = true;
                }

                if (deckIndex < decks.size())
                {
                    CardDeck deck = decks.get(deckIndex);
                    deck.addCard(card);
                    deckIndex++;
                    cards.remove(card);
                }
                else
                {
                    deckIndex = 0;
                }
            }
        }

        for (CardDeck deck : decks)
        {
            System.out.println(deck.getName() + " " + deck.getDeckValues());
        }

        ArrayList<Thread> threads = new ArrayList<>();
        for (Player player : players) {
            Thread thread = new Thread(player);
            thread.start();
            threads.add(thread);
            System.out.println();
            System.out.println(player.getLeft().getDeckValues() + " " + player.getRight().getDeckValues());
        }

        for (Player player : players) {
            player.setThread(threads);
        }

        for (Thread thread : threads)
        {
            // Wait for every thread to complete
            thread.join();
        }
    }
}