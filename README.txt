Card Game Simulation

-- Simulation --
PackGenerator can be used to generate a valid pack, but it was made for testing purposes only.
CardGame runs the simulation.

-- Testing --
The test is contained within the TestRunner class.
Almost every test is completed using asserts, however some tests (those involving CardHolder.outputLine())
output their result to a file.
This output is located in the "test" folder.

To run the test suite, you will need JUnit 4. (JUnit 4.13.2 was used)
The test suite is located in test/JunitTestSuite.
The executable (runner) class is located in test/TestRunner.