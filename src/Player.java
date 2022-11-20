import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.nio.file.*;

public class Player implements Runnable {
    /* A class used as a thread in CardGame.
     * This class handles any player input and card dealing after the initial hands are dealt.
     */
    private ArrayList<Card> hand = new ArrayList<>();
    private int id;
    private String name;
    private Path outputFile;
    private ArrayList<Player> otherPlayers = new ArrayList<>();

    private CardDeck left;
    private CardDeck right;
    private ArrayList<Thread> thread = new ArrayList<>();

    public Player(int id, CardDeck left, CardDeck right)
    {
        this.id = id;
        this.name = "player " + Integer.toString(id);
        this.left = left;
        this.right = right;
    }

    /* This method is called when the thread for this player starts.
     * It repeatedly calls takeTurn, and then waits a short amount of time.
     * The sleeping makes the simulation more readable and allows InterruptedException to be caught.
     */
    public void run()
    {
        System.out.println(name + "has started");
        while (!Thread.currentThread().isInterrupted())
        {
            try {
                takeTurn();
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(name + " has exited.");
    }

    /* Getters and Setters */
    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public CardDeck getLeft() {
        return left;
    }

    public CardDeck getRight() {
        return right;
    }
    public ArrayList<Thread> getThread() {
        return thread;
    }
    public void setThread(ArrayList<Thread> thread) {
        this.thread = thread;
    }

    /**
     * Setter method for otherPlayers
     * @param otherPlayers Every other player in the game
     */
    public synchronized void setOtherPlayers(ArrayList<Player> otherPlayers)
    {
        this.otherPlayers = otherPlayers;
    }

    /**
     * A method to check if the player has won, called at the end of every turn.
     * @return Whether or not the player has won
     */
    public synchronized boolean checkHasWon() {

        ArrayList<Integer> handValues = getHandValues();
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
        return playerHasWon;
    }

    /**
     * Method to handle every player turn.
     * An atomic action - before this method, player has 4 cards;
     * after this method, they still have 4.
     * @throws InterruptedException
     */
    private synchronized void takeTurn() throws InterruptedException
    {
        // Get a card from the deck to the left
        Card leftCard = left.popCard();

        // Check if leftCard is null, as popCard returns null if the deck has no cards
        if (leftCard != null) {

            // Discard to the right
            Card rightCard = discardCard();
            right.addCard(rightCard);

            // Add leftCard to the deck
            drawCard(leftCard);

            // Output player hand to the console
            System.out.println(getName() + ": " + getHandValues());

            // Check if the player has won
            if (checkHasWon()){
                handleWin();
            };
        }

    }

    /**
     * Adds the given card to the player's hand
     * @param drawnCard The given card
     */
    public synchronized void drawCard(Card drawnCard)
    {
        if (hand.size() <= 3)
        {
            hand.add(drawnCard);
            outputLine(getName() + " draws a " + Integer.toString(drawnCard.getValue()));
        }
    }

    /**
     * Randomly discards and returns a card from the hand
     * @return The card
     * @throws InterruptedException
     */
    public synchronized Card discardCard() throws InterruptedException {

        Card card = null;
        Random random = new Random();

        while (hand.size() > 0)
        {
            int randIndex = random.nextInt(hand.size());

            if (hand.get(randIndex).getValue() == getId())
            {
                continue;
            }
            else
            {
                card = hand.get(randIndex);
                hand.remove(card);
                return card;
            }
        }

        return card;
    }

    /**
     * Getter method for the hand
     * @return The hand
     */
    public synchronized ArrayList<Card> getHand()
    {
        return hand;
    }

    /**
     * Returns the hand as a list storing the values of each card
     * @return The list of card values
     */
    public synchronized ArrayList<Integer> getHandValues()
    {
        ArrayList<Integer> handValues = new ArrayList<>();
        for (int i=0; i < hand.size(); i++)
        {
            handValues.add(hand.get(i).getValue());
        }
        return handValues;
    }

    /**
     * Output File setter method
     * @param outputFile The path of the output file
     */
    public synchronized void setOutputFile(Path outputFile)
    {
        this.outputFile = outputFile;
    }

    /**
     * Outputs a line to the output file
     * @param line The line to be written
     */
    public synchronized void outputLine(String line)
    {
        try {
            Files.writeString(outputFile, line + "\n", StandardOpenOption.APPEND);
        } catch (IOException e)
        {
            System.out.println("Output to file failed.");
        }
    }

    /**
     * Handles file output for other players after winning
     * @param players The players to inform
     */
    public synchronized void informPlayers(ArrayList<Player> players)
    {
        for (int i=0; i < players.size(); i++)
        {
            Player player = players.get(i);
            if (!player.equals(this))
            {
                player.outputLine(getName() + " has informed " + player.getName() + " that " + getName() + " has won");
            }
        }
    }

    public synchronized void handleWin() throws InterruptedException {
        System.out.println(getName() + " wins");
        outputLine(getName() + " wins");
        informPlayers(otherPlayers);
        for (Thread playerThread : thread) {
            playerThread.interrupt();
        }
    }
}
