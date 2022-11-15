import java.nio.file.Files;
import java.util.ArrayList;
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
    private boolean isRunning = true;
    public void setRunning(boolean TorF) {
        isRunning = TorF; //true of false
    }

    public Player(int playerId, CardDeck leftDeck, CardDeck rightDeck)
    {
        id = playerId;
        name = "player " + Integer.toString(playerId);
        left = leftDeck;
        right = rightDeck;
    }

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
    public void setThread(ArrayList<Thread> newThread) {
        thread = newThread;
    }
    public synchronized void setOtherPlayers(ArrayList<Player> players)
    {
        otherPlayers = players;
    }

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

    private synchronized void takeTurn() throws InterruptedException
    // An atomic action - before this method, player has 4 cards;
    // after this method, they still have 4.
    {
        //System.out.println(getName() + " takes a turn.");

        Card leftCard = left.popCard();
        if (leftCard != null) {
            Card rightCard = discardCard();
            right.addCard(rightCard);
            drawCard(leftCard);
            System.out.println(getName() + ": " + getHandValues());
            if (checkHasWon()){
                handleWin();
            };
        }

    }

    public synchronized void drawCard(Card drawnCard)
    {
        if (hand.size() <= 3)
        {
            hand.add(drawnCard);
            outputLine(getName() + " draws a " + Integer.toString(drawnCard.getValue()));
        }
    }

    // Remove the 4th card.
    // If the 4th card is preferred, go down to the 3rd, etc...
    // If we get through all the cards, then we can say the player has won.
    public synchronized Card discardCard() throws InterruptedException {
        Card card = null;
        boolean cardDiscarded = false;
        for (int i=3; i >= 0; i--)
        {
            if (hand.get(i).getValue() == getId())
            {
                continue;
            }
            else
            {
                cardDiscarded = true;
                card = hand.get(i);
                hand.remove(i);
                break;
            }
        }

        if (!cardDiscarded)
        {
            handleWin();
        }

        return card;
    }

    public synchronized ArrayList<Card> getHand()
    {
        return hand;
    }

    public synchronized ArrayList<Integer> getHandValues()
    {
        ArrayList<Integer> handValues = new ArrayList<>();
        for (int i=0; i < hand.size(); i++)
        {
            handValues.add(hand.get(i).getValue());
        }
        return handValues;
    }

    public synchronized void setOutputFile(Path file)
    {
        outputFile = file;
    }

    public synchronized void outputLine(String line)
    {
        try {
            Files.writeString(outputFile, line + "\n", StandardOpenOption.APPEND);
        } catch (IOException e)
        {
            System.out.println("Output to file failed.");
        }
    }

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
