import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    CardDeck leftDeck = new CardDeck(1);
    CardDeck rightDeck = new CardDeck(2);
    Player player = new Player(1,leftDeck,rightDeck);


//    @Test
//    public void run() {
//    }

    @Test
    public void getId() {
        assert player.getId() == 1;
    }

    @Test
    public void getName() {
        assert player.getName() == "player " + Integer.toString(player.getId());
    }

    @Test
    public void getLeft() {
        assert player.getLeft().equals(leftDeck);
    }

    @Test
    public void getRight() {
        assert player.getRight().equals(rightDeck);
    }

    @Test
    public void getThread() {
    }

    @Test
    public void setThread() {
    }

    @Test
    public void setOtherPlayers() {
    }

    @Test
    public void checkHasWon() {
    }

    @Test
    public void drawCard() {
    }

    @Test
    public void discardCard() {
    }

    @Test
    public void getHand() {
    }

    @Test
    public void getHandValues() {
    }

    @Test
    public void setOutputFile() {
    }

    @Test
    public void outputLine() {
    }

    @Test
    public void informPlayers() {
    }

    @Test
    public void handleWin() {
    }
}