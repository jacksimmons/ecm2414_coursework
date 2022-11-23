package Test;

import org.junit.Test;
import Cards.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TestCardHolder {
    CardHolder cardHolder = new CardHolder();

    @Test
    public void testAddCard() {
        Card card = new Card(1);
        cardHolder.addCard(card);
        assert cardHolder.getHandValues().size() == 1;
        assert cardHolder.hand.contains(card);
    }

    @Test
    public void testGetHandValues() {
        /*
        Tests with empty hand
         */
        cardHolder.hand = new ArrayList<>();
        assert cardHolder.getHandValues().size() == 0;

        cardHolder.addCard(new Card(0));
        assert cardHolder.getHandValues().get(0) == 0;
    }

    @Test
    public void testGetStringHandValues() {
        cardHolder.addCard(new Card(0));
        cardHolder.addCard(new Card(1));
        cardHolder.addCard(new Card(2));
        cardHolder.addCard(new Card(3));

        System.out.println(cardHolder.getStringHandValues());
        assert cardHolder.getStringHandValues().equals(" 3 2 1 0");
    }

    @Test
    public void testSetOutputFile() throws IOException {
        Path path = Paths.get("CardHolderTest_output.txt");
        Files.write(path, "".getBytes());
        cardHolder.setOutputFile(path);

        Files.write(cardHolder.outputFile, "Output 0".getBytes());
        assert cardHolder.outputFile.equals(path);
    }

    @Test
    public void testOutputLine() throws IOException {
        Path path = Paths.get("CardHolderTest_output.txt");
        Files.write(path, "".getBytes());
        cardHolder.setOutputFile(path);
        cardHolder.outputLine("Output 1");
    }
}