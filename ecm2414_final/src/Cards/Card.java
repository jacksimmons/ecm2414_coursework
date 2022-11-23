package Cards;
/**
 * This class creates a card object to be used in the game.
 */
public class Card {
    private int value; //The number value of the card

    // Constructor for a card object.x
    public Card(int cardValue)
    {
        value = cardValue;
    }

    /**
     * Getter method for value
     * @return Value of the card
     */
    public int getValue()
    {
        return value;
    }
}
