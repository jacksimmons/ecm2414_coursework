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
    public static void main(String[] args) throws InterruptedException, IOException {
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
            int leftIndex;
            int rightIndex;

            if (playerIndex - 1 < 0)
            {
                leftIndex = decks.size() - 1;
            }
            else 
            {
                leftIndex = playerIndex - 1;
            }

            if (playerIndex + 1 >= players.size())
            {
                rightIndex = 0;
            }
            else
            {
                rightIndex = playerIndex + 1;
            }

            Player player = new Player(i, decks.get(leftIndex), decks.get(rightIndex));
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
    
                        ArrayList<Integer> handValues = player.getHandValues();
                        int firstValue = handValues.get(0);
    
                        boolean playerHasWon = false;
                        for (int j=0; j < 4; j++)
                        {
                            if (handValues.get(j) != firstValue)
                            {
                                break;
                            }
                            else if (j == 3)
                            {
                                playerHasWon = true;
                            }
                        }
    
                        if (playerHasWon)
                        {
                            player.handleWin();
                        }
    
                        System.out.println(player.getName() + " " + player.getHandValues());
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

            if (!(numPlayers * 8  == lines.size())) {
            	throw new IOException();
            }
        } catch (IOException e) {
            System.out.println("Invalid pack file.");
        }

        ArrayList<Thread> threads = new ArrayList<>();
        for (Player player : players) {
            Thread thread = new Thread(player);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads)
        {
            // Wait for every thread to complete
            thread.join();
        }

    }
}