package Test;

import Cards.*;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TestPlayer {

    public TestPlayer() {

    }
    int numPlayers = 4;
    CardDeck leftDeck = new CardDeck(1);
    CardDeck rightDeck = new CardDeck(2);
    ArrayList<Player> players = new ArrayList<>();

    // Creates 4 players and adds them to a list of players.
    public void createPlayers () {
        for (int i=1; i <= numPlayers; i++)
        {
            Player player = new Player(i, leftDeck, rightDeck);
            players.add(player);
        }
        for (Player player: players) {
            player.setOtherPlayers(players);
        }
    }

    // Used for testing methods that only need 1 player.
    Player testPlayer = new Player(5, leftDeck, rightDeck);

    ArrayList<Thread> threads = new ArrayList<>();

    /*
     * Creates and starts threads for all players in the list players.
     * @throws InterruptedException
     */
    public void setUpThreads() {

        // Create a thread for every player
        for(Player player : players) {
            Thread thread = new Thread(player);
            threads.add(thread);
        }

        // Set the threads variable for every player
        for (Player player : players) {
            player.setThreads(threads);
        }

        // Start all the threads
        for (Thread thread : threads) {
            thread.start();
            System.out.println(thread);
        }
    }

    @Test
    public void testGetId() {
        assert testPlayer.getId() == 5;
    }

    @Test
    public void testGetName() {
        assert testPlayer.getName().equals("player " + testPlayer.getId());
    }

    @Test
    public void testGetLeft() {
        assert testPlayer.getLeft().equals(leftDeck);
    }

    @Test
    public void testGetRight() {
        assert testPlayer.getRight().equals(rightDeck);
    }

    @Test
    public void testGetThreads() {
        TestPlayer test = new TestPlayer();
        test.createPlayers();
        test.setUpThreads();
        assert test.players.get(1).getThreads().size() == numPlayers;
    }

    @Test
    public void testSetThreads() {
        TestPlayer test = new TestPlayer();
        test.createPlayers();
        test.setUpThreads();
        testPlayer.setThreads(test.threads);
        assert testPlayer.getThreads().equals(test.threads);
    }

    @Test
    public void testSetOtherPlayers() {
        TestPlayer test = new TestPlayer();
        test.createPlayers();
        for (int i=1; i <= numPlayers; i++)
        {
            ArrayList<Player> tempOtherPlayers = new ArrayList<>(test.players);
            tempOtherPlayers.remove(i-1);
            test.players.get(i-1).setOtherPlayers(tempOtherPlayers);
        }
        assert test.players.get(0).getOtherPlayers().size() == numPlayers - 1;
    }

    @Test
    public void testCheckHasWon() {
        // 4 different cards therefore the player should not win
        ArrayList<Card> cards = new ArrayList<>();
        for (int i=1; i<=4; i++){
            cards.add(new Card(i));
        }
        for (int i=0; i<=3; i++){
            testPlayer.hand.add(cards.get(i));
        }
        assert !testPlayer.checkHasWon();

        // 4 of the same card of the players preferred card therefore the player should win
        cards = new ArrayList<>();
        testPlayer.hand = new ArrayList<>();
        for (int i=1; i<=4; i++){
            cards.add(new Card(5));
        }
        for (int i=0; i<=3; i++){
            testPlayer.hand.add(cards.get(i));
        }
        assert testPlayer.checkHasWon();

        // 4 of the same card which is not of the players preferred card. This can happen at the
        // start or (very rarely) during the game. In this case, the player should win.
        cards = new ArrayList<>();
        testPlayer.hand = new ArrayList<>();
        for (int i=1; i<=4; i++){
            cards.add(new Card(2));
        }
        for (int i=0; i<=3; i++){
            testPlayer.hand.add(cards.get(i));
        }
        assert testPlayer.checkHasWon();
    }

    @Test
    public void testDrawCard() {
        /*
         * First test is to see if the method works in adding a card to a
         * players hand.
         */
        Card card = new Card(1);
        testPlayer.drawCard(card);
        assert testPlayer.hand.size() == 1;
        assert testPlayer.hand.get(0).equals(card);
        /*
         * This test is to check whether more than 4 cards can be added to
         * a players hand, as a player can only have 4 cards at one time.
         */
        testPlayer.hand = new ArrayList<>();
        ArrayList<Card> cards = new ArrayList<>();
        for (int i=1; i<=4; i++){
            cards.add(new Card(i));
        }
        for (int i=0; i<=3; i++){
            testPlayer.hand.add(cards.get(i));
        }
        testPlayer.drawCard(card);
        assert !(testPlayer.hand.size() > 4);

    }

    @Test
    public void testDiscardCard() throws InterruptedException {
        /*
         * Testing whether the method works with just 1 card.
         */
        Card card = new Card(1);
        testPlayer.hand.add(card);
        testPlayer.discardCard(rightDeck);
        System.out.println(testPlayer.getHandValues());
        assert testPlayer.hand.size() == 0;
        assert rightDeck.getHandValues().size() == 1;

        /*
         * Trying to remove all 4 cards from the hand whilst there is a preferred card (5) in the hand.
         * Testing to see how the method reacts to trying to remove a preferred card when it's the only
         * card left.
         * It should not throw an error and keep the card in its hand.
         */

        rightDeck = new CardDeck(2);
        ArrayList<Card> cards = new ArrayList<>();
        for (int i=2; i<=5; i++){
            cards.add(new Card(i));
        }
        for (int i=0; i<=3; i++){
            testPlayer.hand.add(cards.get(i));
        }

        testPlayer.discardCard(rightDeck);
        testPlayer.discardCard(rightDeck);
        testPlayer.discardCard(rightDeck);
        testPlayer.discardCard(rightDeck);
        assert testPlayer.hand.size() > 0;
        assert testPlayer.getHandValues().contains(5);
        assert rightDeck.getHandValues().size() == 3;
        assert !rightDeck.getHandValues().contains(5);
    }

    /*
     * This test can be seen working in the /test file with the output files of each player.
     */
    @Test
    public void testInformPlayers() {
        TestPlayer test = new TestPlayer();
        test.createPlayers();

        for (Player player : test.players) {
            // Get and set the output file path
            Path path = Paths.get(player.getName() + "_output.txt");
            player.setOutputFile(path);

            // Generate the output file
            try {
                Files.write(path, "".getBytes());
            }
            catch (IOException e)
            {
                System.out.println("Failed to make output file for " + player.getName());
            }
            player.setOutputFile(path);
        }
        testPlayer.informPlayers(test.players);
    }

    @Test
    public void testHandleWin() throws InterruptedException{
        TestPlayer test = new TestPlayer();
        test.createPlayers();
        for (Player player : test.players) {
            // Get and set the output file path
            Path path = Paths.get(player.getName() + "_output.txt");
            player.setOutputFile(path);

            // Generate the output file
            try {
                Files.write(path, "".getBytes());
            }
            catch (IOException e)
            {
                System.out.println("Failed to make output file for " + player.getName());
            }
            player.setOutputFile(path);
        }

        test.setUpThreads();
        test.players.get(3).handleWin();
    }
}