package Cards;

import java.nio.file.*;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Base class for objects which hold cards.
 */
public class CardHolder {
    protected int id;
    protected String name;

    // An ArrayList storing all the cards held by this object
    public ArrayList<Card> hand = new ArrayList<>();

    public Path outputFile;

    public CardHolder() {}

    /**
     * Method to return outputFile, used for testing purposes.
     */
    public Path getOutputFile(){
        return outputFile;
    }
    /**
     * Adds a card to the hand.
     * Used when a player adds a card to a hand during their turn and
     * at the start of the game when dealing out cards.
     * @param drawnCard card that is drawn
     */
    public synchronized void addCard(Card drawnCard)
    {
        hand.add(0, drawnCard);
    }

    /**
     * Returns an array list of all number values of the cards in
     * the "hand".
     * @return an arraylist of all number values of the cards in a hand.
     */
    public synchronized ArrayList<Integer> getHandValues()
    {
        ArrayList<Integer> handValues = new ArrayList<>();
        for (Card card : hand) {
            handValues.add(card.getValue());
        }
        return handValues;
    }

    /**
     * Returns the hand values in a more readable format for file output
     * @return The hand values in a string format, starting with a space e.g. " 1 2 3 4"
     */
    public synchronized String getStringHandValues()
    {
        ArrayList<Integer> handValues = getHandValues();
        String strHandValues = "";
        for (Integer handValue : handValues) {
            strHandValues += " " + handValue;
        }
        return strHandValues;
    }

    /**
     * Output File setter method
     * @param outputFile The path of the output file
     */
    public synchronized void setOutputFile(Path outputFile)
    {
        this.outputFile = outputFile;
    }

    /**
     * Outputs a line to the output file
     * @param line The line to be written
     */
    public synchronized void outputLine(String line)
    {
        try {
            Files.writeString(outputFile, line + "\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Output to file failed.");
        }
    }
}
