package org.example;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Manager extends Remote {
    List<Book> find(int command, String author, String publish, Integer year) throws RemoteException;
}

class ManagerImpl extends UnicastRemoteObject implements Manager {
    public ManagerImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized List<Book> find(int command, String author, String publish, Integer year) throws RemoteException {
        ArrayList<Book> results = new ArrayList<>();
        for (Book book : ClientRmiTask5.books) {
            switch (command) {
                case 0:
                    if (book.authors.contains(author)) {
                        results.add(book);
                    }
                    break;
                case 1:
                    if (book.publisher.equals(publish)) {
                        results.add(book);
                    }
                    break;
                case 2:
                    if (book.year.compareTo(year) > 0) {
                        results.add(book);
                    }
                    break;
            }
        }
        return results;
    }
}

public class ClientRmiTask5 {
    private final Manager manager;
    public static ArrayList<Book> books = new ArrayList<>(0);

    public ClientRmiTask5() throws RemoteException, NotBoundException, MalformedURLException {

        String remoteURL = "//127.0.0.1:1234/find";
        manager = (Manager) Naming.lookup(remoteURL);
        System.out.println("RMI object found");
        books.add(new Book(1, "Name1", List.of("a1"), "ukr", 2000, 200, 123.0, "type1"));
        books.add(new Book(2, "Name2", List.of("a2", "a0"), "ukr", 2040, 300, 1230.0, "type3"));
        books.add(new Book(3, "Name3", List.of("a2", "a1"), "ukr2", 2000, 100, 123.6, "type2"));
        books.add(new Book(4, "Name4", List.of("a3", "a5"), "ukr3", 2030, 20, 125.0, "type1"));
        books.add(new Book(5, "Name5", List.of("a4"), "ukr", 2040, 204, 143.0, "type1"));
        books.add(new Book(6, "Name6", List.of("a4"), "ukr2", 2000, 500, 1663.0, "type3"));
        books.add(new Book(7, "Name7", List.of("a4"), "ukr", 2030, 260, 1253.0, "type4"));
    }

    public List<Book> find(int command, String author, String publish, Integer year) throws RemoteException {
        return manager.find(command, author, publish, year);
    }

    public static void main(String[] args) {

        System.out.println("This program RMI.");
        Scanner in = new Scanner(System.in);
        String author = null, publisher = null;
        Integer year = null;
        System.out.println("Input a num of operation");
        System.out.println("[0] - a list of books by a given author");
        System.out.println("[1] - list of books published by a given publisher");
        System.out.println("[2] - list of books released after a given year ");

        int commandIndex = in.nextInt();
        if (commandIndex == 0) {
            System.out.println("Enter author");
            author = in.next();
        }
        if (commandIndex == 1) {
            System.out.println("Enter publisher");
            publisher = in.next();
        }
        if (commandIndex == 2) {
            System.out.println("Enter year");
            year = in.nextInt();
        }

        try {

            System.out.println("Sending request to RMI");
            ClientRmiTask5 rmiClient = new ClientRmiTask5();
            List<Book> results = rmiClient.find(commandIndex, author, publisher, year);
            System.out.println(results);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
