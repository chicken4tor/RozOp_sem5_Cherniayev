package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientSocketTask5 {

    private static int port = 9876;
    public static ArrayList<Book> books = new ArrayList<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        InetAddress host = InetAddress.getLocalHost();
        Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;

        books.add(new Book(1, "Name1", List.of("a1"), "ukr", 2000, 200, 123.0, "type1"));
        books.add(new Book(2, "Name2", List.of("a2", "a0"), "ukr", 2040, 300, 1230.0, "type3"));
        books.add(new Book(3, "Name3", List.of("a2", "a1"), "ukr2", 2000, 100, 123.6, "type2"));
        books.add(new Book(4, "Name4", List.of("a3", "a5"), "ukr3", 2030, 20, 125.0, "type1"));
        books.add(new Book(5, "Name5", List.of("a4"), "ukr", 2040, 204, 143.0, "type1"));
        books.add(new Book(6, "Name6", List.of("a4"), "ukr2", 2000, 500, 1663.0, "type3"));
        books.add(new Book(7, "Name7", List.of("a4"), "ukr", 2030, 260, 1253.0, "type4"));

        Scanner scan = new Scanner(System.in);

        while (true) {
            socket = new Socket(host.getHostName(), port);
            oos = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("Input a num of operation");
            System.out.println("[0] - a list of books by a given author");
            System.out.println("[1] - list of books published by a given publisher");
            System.out.println("[2] - list of books released after a given year ");
            System.out.println("[3] - exit");

            int commandIndex = scan.nextInt();
            if (commandIndex == 3) {
                System.out.println("Sending close Request");
                oos.writeInt(commandIndex);
                oos.flush();
                break;
            }
            if (commandIndex == 0) {
                System.out.println("Enter author");
                String author;
                author = scan.next();
                System.out.println("Sending request to Socket Server");
                oos.writeInt(commandIndex);
                oos.flush();
                oos.writeBytes(author);
                oos.flush();
            }
            if (commandIndex == 1) {
                System.out.println("Enter publisher");
                String publisher;
                publisher = scan.next();
                System.out.println("Sending request to Socket Server");
                oos.writeInt(commandIndex);
                oos.flush();
                oos.writeBytes(publisher);
                oos.flush();
            }
            if (commandIndex == 2) {
                System.out.println("Enter year");
                Integer year;
                year = scan.nextInt();
                System.out.println("Sending request to Socket Server");
                oos.writeInt(commandIndex);
                oos.flush();
                oos.writeInt(year);
                oos.flush();
            }

            System.out.println("Receiving answers");
            ois = new ObjectInputStream(socket.getInputStream());
            ArrayList<Book> answer;
            answer = (ArrayList<Book>) ois.readObject();
            System.out.println(answer);
            ois.close();
            oos.close();
            Thread.sleep(100);
        }
        oos.writeInt(3);
        System.out.println("Shutting down client!!");
    }
}