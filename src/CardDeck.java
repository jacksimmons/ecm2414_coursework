import java.util.ArrayList;

public class CardDeck {
    ArrayList<Card> deck = new ArrayList<>();
    int id;
    String name;

    public CardDeck(int id)
    {
        this.id = id;
        this.name = "deck " + Integer.toString(id);
    }

    public String getName()
    {
        return name;
    }

    public synchronized void addCard(Card drawnCard)
    {
        deck.add(0, drawnCard);
    }

    public synchronized Card popCard()
    {
        if (deck.size() > 0) {
            Card card = deck.remove(deck.size() - 1);
            return card;
        } else {
            return null;
        }
    } 

    public synchronized ArrayList<Integer> getDeckValues()
    {
        ArrayList<Integer> deckValues = new ArrayList<>();
        for (int i=0; i < deck.size(); i++)
        {
            deckValues.add(deck.get(i).getValue());
        }
        return deckValues;
    }
}
