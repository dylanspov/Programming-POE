package com.quickchat;

import javax.swing.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class QuickChatGUI {

    public static Message[] sentMessages = new Message[50];
    public static Message[] disregardedMessages = new Message[50];
    public static Message[] storedMessages = new Message[50];

    public static String[] messageHashes = new String[50];
    public static String[] messageIDs = new String[50];

    public static int sentCount = 0;
    public static int disregardedCount = 0;
    public static int storedCount = 0;

    public QuickChatGUI() {}

    public void loadStoredMessages() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("stored_messages.json");
            if (is == null) {
                // no resource found, nothing to load
                return;
            }
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(new InputStreamReader(is, "UTF-8"));

            for (Object obj : array) {
                JSONObject jsonObj = (JSONObject) obj;

                String sender = (String) jsonObj.get("sender");
                String recipient = (String) jsonObj.get("recipient");
                String message = (String) jsonObj.get("message");
                String flag = (String) jsonObj.get("flag");

                Message loaded = new Message(sender, recipient, message);
                loaded.setFlag(flag);

                storedMessages[storedCount] = loaded;
                storedCount++;
            }

        } catch (Exception e) {
            // ignore for now in GUI
        }
    }

    public void processMessage(Message msg) {

        String f = msg.getFlag();
        if (f == null) f = "";
        if (f.equalsIgnoreCase("Sent")) {
            sentMessages[sentCount] = msg;
            messageHashes[sentCount] = msg.getMessageHash();
            messageIDs[sentCount] = msg.getMessageID();
            sentCount++;
        } else if (f.equalsIgnoreCase("Disregard")) {
            disregardedMessages[disregardedCount] = msg;
            disregardedCount++;
        } else if (f.equalsIgnoreCase("Stored")) {
            storedMessages[storedCount] = msg;
            storedCount++;
        }
    }

    // Option C: separate dialog per step
    public void showMainMenu() {
        String[] options = {
            "Send Message",
            "Display Sender & Recipient",
            "Display Longest Message",
            "Search by ID",
            "Search by Recipient",
            "Delete by Hash",
            "Display Report",
            "Exit"
        };

        while (true) {
            String choice = (String) JOptionPane.showInputDialog(null, "Choose action:", "QuickChat Menu",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice == null || choice.equals("Exit")) break;

            switch (choice) {
                case "Send Message": sendMessageDialog(); break;
                case "Display Sender & Recipient": displaySendersAndRecipients(); break;
                case "Display Longest Message": displayLongestMessage(); break;
                case "Search by ID": {
                    String id = JOptionPane.showInputDialog(null, "Enter Message ID:");
                    if (id!=null) searchByMessageID(id);
                    break;
                }
                case "Search by Recipient": {
                    String rec = JOptionPane.showInputDialog(null, "Enter Recipient:");
                    if (rec!=null) searchByRecipient(rec);
                    break;
                }
                case "Delete by Hash": {
                    String hash = JOptionPane.showInputDialog(null, "Enter Message Hash to delete:");
                    if (hash!=null) deleteMessageUsingHash(hash);
                    break;
                }
                case "Display Report": displayReport(); break;
            }
        }
    }

    private void sendMessageDialog() {
        String sender = JOptionPane.showInputDialog(null, "Enter sender:");
        if (sender==null) return;
        String recipient = JOptionPane.showInputDialog(null, "Enter recipient:");
        if (recipient==null) return;
        String message = JOptionPane.showInputDialog(null, "Enter message:");
        if (message==null) return;

        Message msg = new Message(sender, recipient, message);

        String[] actions = {"Send", "Disregard", "Store"};
        String act = (String) JOptionPane.showInputDialog(null, "Choose action:", "Action", JOptionPane.PLAIN_MESSAGE, null, actions, actions[0]);
        if (act==null) return;
        if (act.equals("Send")) msg.setFlag("Sent");
        else if (act.equals("Disregard")) msg.setFlag("Disregard");
        else msg.setFlag("Stored");

        processMessage(msg);
        JOptionPane.showMessageDialog(null, "Processed: " + msg.getMessage() + " (" + msg.getFlag() + ")");
    }

    public void displaySendersAndRecipients() {
        StringBuilder sb = new StringBuilder();
        sb.append("==== SENT MESSAGES ====\n");
        for (int i = 0; i < sentCount; i++) {
            sb.append("Sender: ").append(sentMessages[i].getSender()).append("\n");
            sb.append("Recipient: ").append(sentMessages[i].getRecipient()).append("\n");
            sb.append("Message: ").append(sentMessages[i].getMessage()).append("\n");
            sb.append("------------------------\n");
        }
        if (sentCount==0) sb.append("No sent messages.\n");
        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new java.awt.Dimension(500,300));
        JOptionPane.showMessageDialog(null, sp, "Senders & Recipients", JOptionPane.INFORMATION_MESSAGE);
    }

    public void displayLongestMessage() {
        Message longest = null;
        for (int i = 0; i < sentCount; i++) {
            if (longest == null || sentMessages[i].getMessage().length() > longest.getMessage().length()) {
                longest = sentMessages[i];
            }
        }
        if (longest != null) {
            JOptionPane.showMessageDialog(null, "Longest Message:\n" + longest.getMessage());
        } else {
            JOptionPane.showMessageDialog(null, "No sent messages yet.");
        }
    }

    public void searchByMessageID(String id) {
        for (int i = 0; i < sentCount; i++) {
            if (messageIDs[i].equals(id)) {
                JOptionPane.showMessageDialog(null, "Recipient: " + sentMessages[i].getRecipient() + "\nMessage: " + sentMessages[i].getMessage());
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Message ID not found.");
    }

    public void searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        sb.append("Messages for ").append(recipient).append(":\n\n");
        boolean found=false;
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i].getRecipient().equals(recipient)) {
                sb.append(sentMessages[i].getMessage()).append("\n\n");
                found=true;
            }
        }
        if (!found) sb.append("No messages for recipient.\n");
        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new java.awt.Dimension(500,300));
        JOptionPane.showMessageDialog(null, sp, "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }

    public void deleteMessageUsingHash(String hash) {

        for (int i = 0; i < sentCount; i++) {
            if (messageHashes[i].equals(hash)) {
                String msgText = sentMessages[i].getMessage();
                for (int x = i; x < sentCount - 1; x++) {
                    sentMessages[x] = sentMessages[x + 1];
                    messageHashes[x] = messageHashes[x + 1];
                    messageIDs[x] = messageIDs[x + 1];
                }
                sentMessages[sentCount-1]=null;
                messageHashes[sentCount-1]=null;
                messageIDs[sentCount-1]=null;
                sentCount--;
                JOptionPane.showMessageDialog(null, "Message '" + msgText + "' successfully deleted.");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Hash not found.");
    }

    public void displayReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("======= SENT MESSAGE REPORT =======\n\n");
        for (int i = 0; i < sentCount; i++) {
            sb.append("Message Hash: ").append(messageHashes[i]).append("\n");
            sb.append("Recipient: ").append(sentMessages[i].getRecipient()).append("\n");
            sb.append("Message: ").append(sentMessages[i].getMessage()).append("\n");
            sb.append("-----------------------------------\n");
        }
        if (sentCount==0) sb.append("No sent messages.\n");
        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new java.awt.Dimension(500,400));
        JOptionPane.showMessageDialog(null, sp, "Full Report", JOptionPane.INFORMATION_MESSAGE);
    }
}
