package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.DeathNoteImpl;
import it.unibo.deathnote.api.DeathNote;

class TestDeathNote {

    private static String DEAFULT_DEATH_CAUSE = "heart attack";

    private DeathNote deathNote;

    @BeforeEach
    public void setUp() {
        deathNote = new DeathNoteImpl();
    }

    /*
     * 1. Rule number 0 and negative rules do not exist in the DeathNote rules.
    */
    @Test
    public void testInvalidRules() {
        // check that the exceptions are thrown correctly, that their type is 
        // the expected one, and that the message is not null, empty, or blank.
        try {
            deathNote.getRule(0);
            fail("Rule 0 exists");
        } catch(final IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }

        try {
            deathNote.getRule(-1);
            fail("Negative indexed rules exists");
        } catch(final IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    /*
     * 2. No rule is empty or null in the DeathNote rules.
    */
    @Test
    public void testAllRules() {
        // for all the valid rules, check that none is null or blank
        for (int i = 1; i < DeathNote.RULES.size()+1; i++) {
            assertNotNull(deathNote.getRule(i));
        }
    }

    /*
     * 3. The human whose name is written in the DeathNote will eventually die.
    */
    @Test
    public void testIsNameWritten() {
        final String name = "human";
        // verify that the human has not been written in the notebook yet
        assertTrue(!deathNote.isNameWritten(name));
        // write the human in the notebook
        deathNote.writeName(name);
        // verify that the human has been written in the notebook
        assertTrue(deathNote.isNameWritten(name));
        // verify that another human has not been written in the notebook
        assertTrue(!deathNote.isNameWritten("another name"));
        // verify that the empty string has not been written in the notebook
        assertTrue(!deathNote.isNameWritten(""));
    }

    /*
     * 4. Only if the cause of death is written within the next 40 milliseconds of
     * writing the person's name, it will happen.
    */
    @Test
    public void testCause() throws InterruptedException {
        // check that writing a cause of death before writing a name throws the correct exception
        try {
            deathNote.writeDeathCause("random cause");
            fail("Can write death cause without name");
        } catch(final IllegalStateException e) {
            assertNotNull(e.getMessage());
        }
        // write the name of a human in the notebook
        final String name = "human";
        deathNote.writeName(name);
        // verify that the cause of death is a heart attack
        assertEquals(DEAFULT_DEATH_CAUSE, deathNote.getDeathCause(name));
        // write the name of another human in the notebook
        final String anothername = "another human";
        deathNote.writeName(anothername);
        // set the cause of death to "karting accident"
        final String cause = "karting accident";
        assertTrue(deathNote.writeDeathCause(cause));
        // verify that the cause of death has been set correctly (returned true, and the cause is indeed "karting accident")
        assertEquals(cause, deathNote.getDeathCause(anothername));
        // sleep for 100ms
        Thread.sleep(100);
        // try to change the cause of death
        final String newCause = "new random cause";
        deathNote.writeDeathCause(newCause);
        // verify that the cause of death has not been changed
        assertEquals(cause, deathNote.getDeathCause(anothername));
    }
    
    /*
     * 5. Only if the cause of death is written within the next 6 seconds and 40
     * milliseconds of writing the death's details, it will happen.
     */
    @Test
    public void testDescription() throws InterruptedException {
        // check that writing the death details before writing a name throws 
        // the correct exception
        try {
            deathNote.writeDetails("some details");
            fail("Can write details without a name");
        } catch(final IllegalStateException e) {
            assertNotNull(e.getMessage());
        }
        // write the name of a human in the notebook
        final String name = "human";
        deathNote.writeName(name);
        // verify that the details of the death are currently empty
        assertNull(deathNote.getDeathDetails(name));
        // set the details of the death to "ran for too long"
        final String details = "ran for too long";
        assertTrue(deathNote.writeDetails(details));
        // verify that death details have been set correctly (returned true, and the
        // details are indeed "ran for too long")
        assertEquals(details, deathNote.getDeathDetails(name));
        // write the name of another human in the notebook
        final String anothername = "anothername";
        deathNote.writeName(anothername);
        // sleep for 6100ms
        Thread.sleep(6100);
        // try to change the details
        deathNote.writeDetails("other details");
        // verify that the details have not been changed
        assertNull(deathNote.getDeathDetails(anothername));
    }
}