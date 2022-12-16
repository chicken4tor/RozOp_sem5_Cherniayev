package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class Callback {
    public boolean bShouldEnded = false;
}

class Manager implements Runnable {
    Manager(Socket inSocket, Callback inC) {
        c = inC;
        socket = inSocket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("Receiving input");

            int commandIndex = ois.readInt();
            System.out.println("Command " + commandIndex);

            if (commandIndex == 3) {
                System.out.println("Close command");
                c.bShouldEnded = true;
                return;
            }
            String author = null, publish = null;
            Integer year = null;

            switch (commandIndex) {
                case 0:
                    author = ois.readUTF();
                    break;
                case 1:
                    publish = ois.readUTF();
                    break;
                case 2:
                    year = ois.readInt();
                    break;
            }

            ArrayList<Book> answer = new ArrayList<>();

            for (Book book : ClientSocketTask5.books) {
                switch (commandIndex) {
                    case 0:
                        if (book.authors.contains(author)) {
                            answer.add(book);
                        }
                        break;
                    case 1:
                        if (book.publisher.equals(publish)) {
                            answer.add(book);
                        }
                        break;
                    case 2:
                        if (book.year.compareTo(year) > 0) {
                            answer.add(book);
                        }
                        break;
                }
            }
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(answer);

            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {

        }
    }

    Socket socket;
    Callback c;
}

public class ServerSocketTask5 {
    private static ServerSocket server;
    private static int port = 9876;

    public static void main(String args[]) throws IOException {
        server = new ServerSocket(port);
        while (!c.bShouldEnded) {
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();

            Manager calc = new Manager(socket, c);
            calc.run();

        }
        System.out.println("Shutting down Socket server!!");
        server.close();
    }

    public static Callback c = new Callback();
}