import java.nio.file.Files;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.*;

public class Player {
    ArrayList<Card> hand = new ArrayList<>();
    int id;
    String name;
    Path outputFile;

    CardDeck left;
    CardDeck right;

    public Player(int playerId, CardDeck leftDeck, CardDeck rightDeck)
    {
        id = playerId;
        name = "player " + Integer.toString(playerId);
        left = leftDeck;
        right = rightDeck;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void drawCard(Card drawnCard)
    {
        if (hand.size() <= 3)
        {
            hand.add(drawnCard);
            outputLine(getName() + " draws a " + Integer.toString(drawnCard.value));
        }
    }

    public void discardCard(Card discardedCard)
    {
        
    }

    public ArrayList<Card> getHand()
    {
        return hand;
    }

    public ArrayList<Integer> getHandValues()
    {
        ArrayList<Integer> handValues = new ArrayList<>();
        for (int i=0; i < hand.size(); i++)
        {
            handValues.add(hand.get(i).getValue());
        }
        return handValues;
    }

    public void setOutputFile(Path file)
    {
        outputFile = file;
    }

    public void outputLine(String line)
    {
        try {
            Files.writeString(outputFile, line + "\n", StandardOpenOption.APPEND);
        } catch (IOException e)
        {
            System.out.println("Output to file failed.");
        }
    }

    public void informPlayers(ArrayList<Player> players)
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
}
