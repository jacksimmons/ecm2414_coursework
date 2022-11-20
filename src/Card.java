/**
 * This class creates a card object to be used in the game.
 */
public class Card {
    private int value; //The number value of the card

    public Card(int cardValue)
    {
        value = cardValue;
    } //Constructor for a card object.

    /**
     * Getter method for value
     * @return Value of the card
     */
    public int getValue()
    {
        return value;
    }
}
