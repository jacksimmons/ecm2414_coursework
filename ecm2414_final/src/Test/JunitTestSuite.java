package Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestPlayer.class,
        TestCardDeck.class,
        TestCardHolder.class
})

public class JunitTestSuite {
}