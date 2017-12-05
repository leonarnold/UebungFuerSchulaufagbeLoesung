package com.sabel.ufsa;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        String sentence;
        String modifiedSentence;
        User currentUser = null;

        UserService userService = new UserService();
        MessageService messageService = new MessageService();

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        boolean done = false;
        while (!done) {
            Boolean waitForAnswer = true;
            System.out.println("u :: neuen User anlegen -- l :: user login -- a :: alle Nachrichten auflisten -- n :: Nachricht eintragen -- m :: Nachricht modifizieren -- q :: Ende");
            sentence = inFromUser.readLine();

            switch (sentence) {
                case "u":
                    System.out.println("Bitte geben Sie einen Namen für den Benutzer ein:");
                    String userName = inFromUser.readLine();
                    System.out.println("Bitte geben Sie ein Passwort für den Benutzer ein:");
                    String userPassword = inFromUser.readLine();

                    User user = new User(userName, userPassword);

                    //User wird in DB geschriben

                    userService.writeUser(user);

                    break;
                case "l":
                    System.out.println("Bitte geben Sie einen Benutzernamen ein:");
                    String userNameForLogin = inFromUser.readLine();
                    System.out.println("Bitte geben Sie das Passwort des Benutzers ein:");
                    String userPasswordForLogin = inFromUser.readLine();

                    User userForLogin = new User(userNameForLogin, userPasswordForLogin);

                    //User aus DB auslesen und vergleichen
                    User dbUser = userService.getUser(userNameForLogin);

                    if(dbUser != null && dbUser.checkPassword(userPasswordForLogin))
                    {
                        currentUser = dbUser;
                        System.out.println("Sie sind jetzt eingeloggt");
                    }else {
                        System.out.println("Fehler: Sie sind NICHT eingeloggt");
                    }

                    break;
                case "n":
                    if (currentUser == null) {
                        System.out.println("Sie sind nicht angemeldet!");
                        continue;
                    }
                    System.out.println("Bitte geben Sie einen NAchricht ein:");
                    String messageText = inFromUser.readLine();

                    Message message = new Message(messageText);

                    //Nachricht in die Datenbank schreiben
                    messageService.writeMessage(message);

                    break;
                case "m":
                    if (currentUser == null) {
                        System.out.println("Sie sind nicht angemeldet!");
                        continue;
                    }

                    System.out.println(messageService.getAListOfAllMessages());

                    boolean idIsANumber = false;
                    int messsageId = -1;
                    do {
                        System.out.println("Bitte geben Sie die ID der  Nachricht ein, die Sie verändern wollen:");
                        String messageIdText = inFromUser.readLine();
                        try {
                            messsageId = Integer.parseInt(messageIdText);
                            idIsANumber = true;
                        } catch (Exception e) {

                        }
                    } while (!idIsANumber);

                    System.out.println("Bitte geben Sie eine neue Nachricht ein:");
                    String newMessageText = inFromUser.readLine();

                    //Neue Nachricht
                    Message newMessage = new Message(newMessageText);

                    //ID und neue nachricht übergeben an Service
                    messageService.modifyMessage(messsageId,newMessage);
                    //Nachricht in DB ändern
                    break;
                case "a":
                    if (currentUser == null) {
                        System.out.println("Sie sind nicht angemeldet!");
                        continue;
                    }

                    System.out.println(messageService.getAListOfAllMessages());
                    break;
                case "q":
                    done = true;
                    break;
            }

        }

    }

}
