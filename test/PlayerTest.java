import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    public PlayerTest() {

    }
    int numPlayers = 4;
    CardDeck leftDeck = new CardDeck(1);
    CardDeck rightDeck = new CardDeck(2);
    ArrayList<Player> players = new ArrayList<>();
    public void createPlayers () {
        for (int i=1; i <= numPlayers; i++)
        {
            Player player = new Player(i, leftDeck, rightDeck);
            players.add(player);
        }
    }

    Player testPlayer = new Player(1, leftDeck, rightDeck);

    ArrayList<Thread> threads = new ArrayList<>();

    public void setUpThreads() throws InterruptedException {
        PlayerTest test = new PlayerTest();

        // Create a thread for every player
        for(Player player : players) {
            Thread thread = new Thread(player);
            threads.add(thread);
        }

        // Set the threads variable for every player
        for (Player player : players) {
            player.setThread(threads);
        }

        // Start all the threads
        for (Thread thread : threads) {
            thread.start();
            System.out.println(thread);
            thread.interrupt();
        }

        // Wait for every thread to complete
        for (Thread thread : threads)
        {
            thread.join();
        }
    }


    @Test
    public void testRun() {
//        ArrayList<Thread> threads = new ArrayList<>();
//        Thread thread = new Thread(player);
//        threads.add(thread);
//        player.setThread(threads);
//        thread.start();
//        player.run();



    }

    @Test
    public void getId() {
        assert players.get(0).getId() == 1;
    }

    @Test
    public void getName() {
        assert players.get(0).getName() == "player " + Integer.toString(players.get(0).getId());
    }

    @Test
    public void getLeft() {
        assert players.get(0).getLeft().equals(leftDeck);
    }

    @Test
    public void getRight() {
        assert players.get(0).getRight().equals(rightDeck);
    }

    @Test
    public void getThread() throws InterruptedException {
//        PlayerTest test = new PlayerTest();
//        test.setUpThreads();
//        System.out.println(player1.getThread());

    }

    @Test
    public void setThread() {
    }

    @Test
    public void setOtherPlayers() {
        PlayerTest test = new PlayerTest();
        test.createPlayers();
        for (int i=1; i <= numPlayers; i++)
        {
            test.players.get(i-1).setOtherPlayers(test.players);
        }
        assert test.players.get(0).getOtherPlayers().size() == numPlayers;
    }

    @Test
    public void checkHasWon() {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i=1; i<=4; i++){
            cards.add(new Card(i));
        }
        for (int i=0; i<=3; i++){
            testPlayer.hand.add(cards.get(i));
        }
        assert testPlayer.checkHasWon() == false;

        cards = new ArrayList<>();
        testPlayer.hand = new ArrayList<>();
        for (int i=1; i<=4; i++){
            cards.add(new Card(1));
        }
        for (int i=0; i<=3; i++){
            testPlayer.hand.add(cards.get(i));
        }
        assert testPlayer.checkHasWon();

    }
    @Test
    public void drawCard() {
        Card card = new Card(1);
        testPlayer.drawCard(card);
        assert testPlayer.hand.size() == 1;
        assert testPlayer.hand.get(0).equals(card);
    }

    @Test
    public void discardCard() throws InterruptedException {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i=1; i<=4; i++){
            cards.add(new Card(i));
        }
        for (int i=0; i<=3; i++){
            testPlayer.hand.add(cards.get(i));
        }
        /**
         * Trying to remove all 4 cards from the hand whilst there is a preferred card in the hand.
         * Testing to see how the method reacts to trying to remove a preferred card.
         * It should not throw an error and keep the card in its hand.
         */
        testPlayer.discardCard(rightDeck);
        System.out.println(testPlayer.getHandValues());
        testPlayer.discardCard(rightDeck);
        System.out.println(testPlayer.getHandValues());
        testPlayer.discardCard(rightDeck);
        System.out.println(testPlayer.getHandValues());
        testPlayer.discardCard(rightDeck);
        System.out.println(testPlayer.getHandValues());
        assert testPlayer.hand.size() > 0;
        assert testPlayer.getHandValues().contains(1);
        assert rightDeck.getHandValues().size() == 3;
        assert !rightDeck.getHandValues().contains(1);


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