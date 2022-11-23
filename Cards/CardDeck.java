package Cards;
/**
 * This class creates a card deck that holds cards and can be
 * interacted with by the players and the game.
 */
public class CardDeck extends CardHolder {
    public CardDeck(int id) //Constructor for the deck.
    {
        this.id = id;
        this.name = "deck " + Integer.toString(this.id);
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
     * Removes a card from the deck and returns the card.
     * Used in the player class when they take their turn and
     * pick up a card from the deck to the left.
     * @return The card that is removed.
     */
    public synchronized Card popCard()
    {
        if (hand.size() > 0) {
            return hand.remove(hand.size() - 1);
        } else {
            return null;
        }
    }
}
