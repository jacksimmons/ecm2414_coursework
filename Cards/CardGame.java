package Cards;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;

/** A class which handles the whole game.
    * It handles the beginning of the game (dealing to the players and the decks), then runs the Player threads.
    * The Player threads then continue the game.
    */
public class CardGame {
    public static void main(String[] args) throws InterruptedException
    {
        // Create scanner for input
        Scanner scanner = new Scanner(System.in);

        int numPlayers;

        // Card objects read from the input file
        ArrayList<Card> cards;
        
        while (true) {
            scanner = new Scanner(System.in);

            // Ask for input on number of players
            System.out.println("Please enter the number of players:");
            try {
                numPlayers = scanner.nextInt();

                if (numPlayers <= 0)
                {
                    System.out.println("Number of players must be greater than zero.");
                    continue;
                }
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid number of players.");
                scanner.next();
                continue;
            }

            // Ask for input on file location
            System.out.println("Please enter location of the pack to load:");
            scanner.nextLine();
            String path = scanner.nextLine();

            // Load the deck from file given and check the file is valid; if not,
            // display an error message to console.
            try {
                List<String> lines = Files.readAllLines(Paths.get(path));
                cards = new ArrayList<>();

                // Convert every line in the file into a card
                while (lines.size() > 0)
                {
                    String line = lines.get(lines.size() - 1);
                    Card lineCard = new Card(Integer.valueOf(line));
                    lines.remove(line);
                    cards.add(lineCard);
                }

                // The pack must have 8n cards, where n is the number of players
                if (numPlayers * 8 != cards.size())
                {
                    System.out.println("Invalid pack file length.");
                    continue;
                }

                break;

            } catch (IOException e) {
                System.out.println("Couldn't open pack file.");
            } catch (NumberFormatException e) {
                System.out.println("Couldn't read pack file: it has a non-integer value in it.");
            }
        }

        scanner.close();

        // ArrayLists storing the player and deck objects
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<CardDeck> decks = new ArrayList<>();

        // Populate decks list with empty decks
        for (int i=1; i <= numPlayers; i++)
        {
            // Create the deck
            CardDeck deck = new CardDeck(i);
            decks.add(deck);

            // Get and set the output file path
            Path path = Paths.get(deck.getName() + "_output.txt");
            deck.setOutputFile(path);

            // Generate the output file
            try {
                Files.write(path, "".getBytes());
            }
            catch (IOException e)
            {
                System.out.println("Failed to make output file for " + deck.getName());
            }
        }

        // Create the players and assign them their Left and Right decks (the decks have no cards yet)
        for (int i=1; i <= numPlayers; i++)
        {
            // Calculate the left and right deck IDs
            int leftNum = i;
            int rightNum = i + 1;

            if (i == numPlayers)
            {
                rightNum = 1;
            }

            // Create the player
            Player player = new Player(i, decks.get(leftNum - 1), decks.get(rightNum - 1));
            players.add(player);

            // Get and set the player output path
            Path path = Paths.get(player.getName() + "_output.txt");
            player.setOutputFile(path);

            // Generate the output file
            try {
                Files.write(path, "".getBytes());
            }
            catch (IOException e)
            {
                System.out.println("Failed to make output file for " + player.getName());
            }
        }

        // Set the otherPlayers variable for every player
        for (int i=1; i <= numPlayers; i++)
        {
            players.get(i-1).setOtherPlayers(players);
        }

        boolean playersDealt = false;
        int playerIndex = 0;
        int deckIndex = 0;
        int playerCardNumber = 0;
        boolean checkedPlayers = false;

        // Deal the cards, first to the players, and then to the decks
        while (cards.size() > 0)
        {
            // Get a random card from the pack
            Random random = new Random();
            Card card = cards.get(random.nextInt(cards.size()));

            // Until every player has had their cards dealt, continue this block
            if (!playersDealt)
            {
                // Iterate over every player until the end of the players list is reached,
                // then start again, incrementing playerCardNumber.
                // When playerCardNumber == 3 (4th card) and every player has been dealt
                // their card, then every player has been dealt their cards.
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
                // Check whether a player has already won from their starting cards, and output their starting hand
                if (!checkedPlayers)
                {
                    for (int i=0; i < numPlayers; i++)
                    {
                        Player player = players.get(i);

                        if (player.checkHasWon()){
                            Thread thread = new Thread(player);
                            thread.start();
                            ArrayList<Thread> threads = new ArrayList<>();
                            threads.add(thread);
                            player.setThreads(threads);
                            player.handleWin();
                            return;
                        }
                        
                        player.outputLine(player.getName() + " initial hand" + player.getStringHandValues());
                        System.out.println(player.getName() + " " + player.getHandValues());
                        System.out.println(player.getLeft().getName() + " " + player.getRight().getName());
                    }
                    checkedPlayers = true;
                }

                // Deal the remaining cards to the decks
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

        // Console information regarding cards in each deck
        for (CardDeck deck : decks)
        {
            System.out.println(deck.getName() + " " + deck.getHandValues());
        }

        // Create a thread for every player
        ArrayList<Thread> threads = new ArrayList<>();
        for (Player player : players) {
            Thread thread = new Thread(player);
            threads.add(thread);
        }

        // Set the threads variable for every player
        for (Player player : players) {
            player.setThreads(threads);
        }

        // Start all the threads
        for (Thread thread : threads) {
            thread.start();
        }

        // Wait for every thread to complete
        for (Thread thread : threads)
        {
            thread.join();
        }

        // Now finish the game by outputting the final contents of every deck to their corresponding
        // output files.
        for (CardDeck deck : decks)
        {
            deck.outputLine(deck.getName() + " contents:" + deck.getStringHandValues());
        }
    }
}