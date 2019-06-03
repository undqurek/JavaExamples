package com.example.rmi.features;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.example.domain.User;

public interface GameServer extends Remote
{
	public boolean loginUser(String login, String password) throws RemoteException;
	public void logoutUser() throws RemoteException;
	
	public User[] getUsers() throws RemoteException;
}
