import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class CardGame {
    public static void main(String[] args)
    {
        // Create scanner for input
        Scanner scanner = new Scanner(System.in);

        int numPlayers;
        ArrayList<Card> cards;
        
        while (true) {
            // Ask for input on number of players        
            System.out.println("Please enter the number of players:");
            numPlayers = scanner.nextInt();

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
                    continue;
                }

                break;

            } catch (IOException e) {
                System.out.println("Couldn't open pack file.");
                continue;
            } catch (NumberFormatException e) {
                System.out.println("Invalid type in pack file.");
            }
        }

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
                            System.out.println(player.getName() + " wins");
                            player.outputLine(player.getName() + " wins");
                            player.informPlayers(players);
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

        for (int i=0; i < decks.size(); i++)
        {
            CardDeck deck = decks.get(i);
            System.out.println(deck.getName() + " " + deck.getDeckValues());
        }

        System.out.println(cards);
    }
}