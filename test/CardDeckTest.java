import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CardDeckTest {
    CardDeck cardDeck = new CardDeck(1);
    Card newCard = new Card(5);
    @Test
    public void testAddCard() {
        cardDeck.addCard(newCard);
        assert cardDeck.deck.size() == 1;
    }

    @Test
    public void testPopCard() {
        cardDeck.popCard();
        assert cardDeck.deck.size() == 0;

    }

    @Test
    public void testGetDeckValues() {
        cardDeck.addCard(newCard);
        ArrayList<Integer> testArray = new ArrayList<>();
        testArray.add(5);
        assert ( cardDeck.getDeckValues().equals(testArray));
    }
}