import java.util.ArrayList;

public class CardDeck {
    ArrayList<Card> deck = new ArrayList<>();
    int id;
    String name;

    public CardDeck(int deckId)
    {
        id = deckId;
        name = "deck " + Integer.toString(deckId);
    }

    public String getName()
    {
        return name;
    }

    public void addCard(Card drawnCard)
    {
        deck.add(drawnCard);
    }

    public ArrayList<Integer> getDeckValues()
    {
        ArrayList<Integer> deckValues = new ArrayList<>();
        for (int i=0; i < deck.size(); i++)
        {
            deckValues.add(deck.get(i).getValue());
        }
        return deckValues;
    }
}
