package Cards;

import java.util.ArrayList;
import java.util.Random;

/** A class used as a thread in CardGame.
 * This class handles any player input and card dealing after the initial hands are dealt.
 */
public class Player extends CardHolder implements Runnable {
    private ArrayList<Player> otherPlayers = new ArrayList<>();

    private final CardDeck left;
    private final CardDeck right;
    private ArrayList<Thread> threads = new ArrayList<>();

    public Player(int id, CardDeck left, CardDeck right)
    {
        this.id = id;
        this.name = "player " + id;
        this.left = left;
        this.right = right;
    }

    /* This method is called when the thread for this player starts.
     * It repeatedly calls takeTurn, and then waits a short amount of time.
     * The sleeping makes the simulation more readable and allows InterruptedException to be caught.
     */
    public void run()
    {
        System.out.println(name + " has started");
        while (!Thread.currentThread().isInterrupted())
        {
            try {
                takeTurn();
                //Random random = new Random();
                //Thread.sleep(random.nextInt(1000));
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println(name + " has exited.");
        outputLine(name + " has exited.");
        outputLine(name + " final hand" + getStringHandValues());
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

    public ArrayList<Thread> getThreads() {
        return threads;
    }

    public void setThreads(ArrayList<Thread> threads) {
        this.threads = threads;
    }

    public ArrayList<Player> getOtherPlayers() {
        return otherPlayers;
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
     * @return Whether the player has won
     */
    public synchronized boolean checkHasWon() {

        ArrayList<Integer> handValues = getHandValues();
        int firstValue = handValues.get(0);

        boolean playerHasWon = false;

        // Check all other cards are the same as the first card
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
     * @throws InterruptedException because handleWin throws this
     */
    private synchronized void takeTurn() throws InterruptedException
    {
        // Get a card from the deck to the left
        Card leftCard = left.popCard();

        // Check if leftCard is null, as popCard returns null if the deck has no cards
        if (leftCard != null) {

            // Discard to the right
            Card discardedCard = discardCard(right);
            if (discardedCard != null)
            {
                outputLine(name + " discards a " + discardedCard.getValue() + " to " + right.getName());
            }

            // Add leftCard to the deck and output this to the file
            drawCard(leftCard);
            outputLine(getName() + " draws a " + leftCard.getValue() + " from " + left.getName());

            // Output hand to file
            outputLine(name + " current hand is" + getStringHandValues());

            // Output player hand to the console
            System.out.println(getName() + ": " + getHandValues());

            // Output left and right decks to console
            // System.out.println(left.getName() + ": " + left.getHandValues());
            // System.out.println(right.getName() + ": " + right.getHandValues());
            
            // Check if the player has won
            if (checkHasWon()) {
                handleWin();
            }
        }
    }

    /**
     * Adds the given card to the player's hand
     * @param card The given card
     */
    public synchronized void drawCard(Card card)
    {
        if (hand.size() <= 3)
        {
            hand.add(card);
        }
    }

    /**
     * Randomly discards and returns a card from the hand
     * @param deck The deck to discard to
     * @return The Card, if successfully discarded to {@code}deck{@code} or not
     */
    public synchronized Card discardCard(CardDeck deck) {
        Card card = null;
        Random random = new Random();
        ArrayList<Integer> possibleIndices = new ArrayList<>();

        for (int i=0; i < hand.size(); i++)
        {
            possibleIndices.add(i);
        }

        // Randomly select a card from the deck.
        while (hand.size() > 0 && possibleIndices.size() > 0)
        {
            int randIndex = random.nextInt(possibleIndices.size());
            int randValue = possibleIndices.get(randIndex);

            // If randValue is the index of a preferred denomination
            if (hand.get(randValue).getValue() == getId())
            {
                possibleIndices.remove(randIndex);
            }
            else
            {
                card = hand.get(randValue);
                hand.remove(card);
                break;
            }
        }

        if (card != null)
        {
            deck.addCard(card);
        }

        return card;
    }

    /**
     * Handles file output for other players after winning
     * @param players The players to inform
     */
    public synchronized void informPlayers(ArrayList<Player> players)
    {
        for (Player player : players) {
            if (!player.equals(this)) {
                player.outputLine(getName() + " has informed " + player.getName() + " that " + getName() + " has won");
            }
        }
    }

    /**
     * This method is called once a player wins. It handles outputting to their
     * text file, informing other people that they have won and stopping other player's threads,
     * in order to stop the game.
     */
    public synchronized void handleWin() throws InterruptedException {
        System.out.println(getName() + " wins");
        outputLine(getName() + " wins");
        informPlayers(otherPlayers);
        for (Thread playerThread : threads) {
            playerThread.interrupt();
        }
    }
}
