package com.example.rmi.features;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.example.domain.MessageEvent;
import com.example.domain.User;

public interface GameServer extends Remote
{
	public void setMessageEvent(MessageEvent event) throws RemoteException;
	
	public boolean loginUser(String login, String password) throws RemoteException;
	public void logoutUser() throws RemoteException;
	
	public User[] getUsers() throws RemoteException;
	public void sendMessages() throws RemoteException;
}
