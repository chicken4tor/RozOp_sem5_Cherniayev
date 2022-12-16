package org.example;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRmiTask5 {
    public static void main(String[] args) throws RemoteException {
        ManagerImpl find = new ManagerImpl();
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind("find", find);
    }
}