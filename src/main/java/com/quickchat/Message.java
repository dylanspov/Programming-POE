package com.quickchat;

public class Message {
    private String messageID;
    private String sender;
    private String recipient;
    private String message;
    private String messageHash;
    private String flag; // Sent, Stored, Disregard

    private static int totalMessages = 0;

    public Message(String sender, String recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
        this.flag = "";
    }

    private String generateMessageID() {
        long id = System.currentTimeMillis() % 10000000000L;
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public boolean checkRecipientCell() {
        return recipient != null && recipient.length() <= 13 && (recipient.startsWith("+") || recipient.startsWith("0"));
    }

    public String createMessageHash() {
        String firstTwo = messageID.length() >= 2 ? messageID.substring(0,2) : messageID;
        int number = totalMessages;
        String[] words = message.split(" ");
        String firstWord = words.length>0?words[0].toUpperCase():"";
        String lastWord = words.length>0?words[words.length-1].toUpperCase():"";
        return firstTwo + ":" + number + ":" + firstWord + lastWord;
    }

    public String sendMessage() {
        flag = "Sent";
        totalMessages++;
        messageHash = createMessageHash();
        return "Message successfully sent.";
    }

    public String storeMessage() {
        flag = "Stored";
        return "Message stored successfully.";
    }

    public String discardMessage() {
        flag = "Disregard";
        return "Message discarded.";
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public String printMessages() {
        return "ID: " + messageID + " | Hash: " + messageHash + " | To: " + recipient + " | Msg: " + message;
    }

    public String getMessageID(){return messageID;}
    public String getMessageHash(){return messageHash;}
    public String getRecipient(){return recipient;}
    public String getSender(){return sender;}
    public String getMessage(){return message;}
    public String getFlag(){return flag;}
    public void setFlag(String f){ this.flag = f; if ("Sent".equalsIgnoreCase(f)) { sendMessage(); } }
}
