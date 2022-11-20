import java.util.ArrayList;

/**
 * This class creates a card deck that holds cards and can be
 * interacted with by the players and the game.
 */
public class CardDeck {
    ArrayList<Card> deck = new ArrayList<>(); //An arraylist to store the cards held by the deck.
    int id;
    String name;

    public CardDeck(int deckId) //Constructor for the deck.
    {
        id = deckId;
        name = "deck " + Integer.toString(deckId);
    }

    /**
     * Getter method for the name of the player
     * @return The name of the player
     */
    public String getName()
    {
        return name;
    }

    /**
     * Adds a card to the deck.
     * Used when a player adds a card to a deck during their turn and
     * at the start of the game when dealing out cards.
     * @param drawnCard
     */
    public synchronized void addCard(Card drawnCard)
    {
        deck.add(0, drawnCard);
    }

    /**
     * Removes a card from the deck and returns the card.
     * Used in the player class when they take their turn and
     * pick up a card from the deck to the left.
     * @return The card that is removed.
     */
    public synchronized Card popCard()
    {
        if (deck.size() > 0) {
            Card card = deck.remove(deck.size() - 1);
            return card;
        } else {
            return null;
        }
    }

    /**
     * Returns an array list of all number values of the cards in
     * a deck.
     * @return an arraylist of all number values of the cards in a deck.
     */
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
