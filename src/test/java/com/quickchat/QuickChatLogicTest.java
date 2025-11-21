package com.quickchat;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class QuickChatLogicTest {

    QuickChatGUI qc;

    Message m1, m2, m3, m4, m5;

    @Before
    public void setUp() {

        qc = new QuickChatGUI();

        // Message 1 - Sent
        m1 = new Message("Developer", "+27834557896", "Did you get the cake?");
        m1.setFlag("Sent");
        qc.processMessage(m1);

        // Message 2 - Stored
        m2 = new Message("Developer", "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        m2.setFlag("Stored");
        qc.processMessage(m2);

        // Message 3 - Disregard
        m3 = new Message("Developer", "+27834484567", "Yohoooo, I am at your gate.");
        m3.setFlag("Disregard");
        qc.processMessage(m3);

        // Message 4 - Sent
        m4 = new Message("Developer", "+0838884567", "It is dinner time !");
        m4.setFlag("Sent");
        qc.processMessage(m4);

        // Message 5 - Stored
        m5 = new Message("Developer", "+27838884567", "Ok, I am leaving without you.");
        m5.setFlag("Stored");
        qc.processMessage(m5);

        // load stored_messages from resources for one test
        qc.loadStoredMessages();
    }

    @Test
    public void testSentMessagesPopulated() {
        assertEquals("Did you get the cake?", QuickChatGUI.sentMessages[0].getMessage());
        assertEquals("It is dinner time !", QuickChatGUI.sentMessages[1].getMessage());
    }

    @Test
    public void testLongestMessage() {
        String expected = "Where are you? You are late! I have asked you to be on time.";
        assertEquals(expected, m2.getMessage());
    }

    @Test
    public void testStoredMessagesLoaded() {
        // stored_messages.json has two stored messages; ensure they loaded into storedMessages array
        assertNotNull(QuickChatGUI.storedMessages[0]);
        assertNotNull(QuickChatGUI.storedMessages[1]);
    }

    @Test
    public void testDeleteMessage() {
        String hash = m1.getMessageHash();
        QuickChatGUI qc2 = new QuickChatGUI();
        qc2.processMessage(m1);
        qc2.deleteMessageUsingHash(hash);

        for (int i = 0; i < QuickChatGUI.sentCount; i++) {
            assertNotEquals(hash, QuickChatGUI.messageHashes[i]);
        }
    }
}
