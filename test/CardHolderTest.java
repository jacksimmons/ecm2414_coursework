import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CardHolderTest {
    CardHolder cardHolder = new CardHolder();
    CardDeck leftDeck = new CardDeck(1);
    CardDeck rightDeck = new CardDeck(2);
    Player testPlayer = new Player(1,leftDeck,rightDeck);

    @Test
    public void addCard() {
        Card card = new Card(1);
        cardHolder.addCard(card);
        assert cardHolder.getHandValues().size() == 1;
        assert cardHolder.hand.contains(card);
    }

    @Test
    public void getHandValues() {
        /*
        Tests with empty hand
         */
        cardHolder.hand = new ArrayList<>();
        assert cardHolder.getHandValues().size() == 0;

        cardHolder.addCard(new Card(0));
        assert cardHolder.getHandValues().get(0) == 0;
    }

    @Test
    public void getStringHandValues() {
        cardHolder.addCard(new Card(0));
        cardHolder.addCard(new Card(1));
        cardHolder.addCard(new Card(2));
        cardHolder.addCard(new Card(3));

        assert cardHolder.getStringHandValues().equals(" 0 1 2 3");
    }

    @Test
    public void setOutputFile() {
        Path path = Paths.get("test/CardHolderTest_output.txt");
        Files.write(path, "".getBytes());
        cardHolder.setOutputFile(path);

        Files.write(cardHolder.outputFile, "Output 0".getBytes());
        assert cardHolder.outputFile.equals(path);
    }

    @Test
    public void outputLine() {
        Path path = Paths.get("test/CardHolderTest_output.txt");
        Files.write(path, "".getBytes());
        cardHolder.setOutputFile(path);
        cardHolder.outputLine("Output 1");
    }
}