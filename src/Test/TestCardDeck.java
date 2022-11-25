package Test;

import Cards.*;
import org.junit.Test;
import java.util.ArrayList;

public class TestCardDeck {
    CardDeck cardDeck = new CardDeck(1);
    Card newCard = new Card(5);
    @Test
    public void testAddCard() {
        cardDeck.addCard(newCard);
        assert cardDeck.hand.size() == 1;
    }

    @Test
    public void testPopCard() {
        cardDeck.popCard();
        assert cardDeck.hand.size() == 0;
    }

    @Test
    public void testGetDeckValues() {
        cardDeck.addCard(newCard);
        ArrayList<Integer> testArray = new ArrayList<>();
        testArray.add(5);
        assert ( cardDeck.getHandValues().equals(testArray));
    }
}