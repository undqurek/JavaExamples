package com.example.domain;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageEvent extends Serializable, Remote 
{
	void messageSended(String message) throws RemoteException;
}
