package com.gil.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ClientType {
	CUSTOMER(1), COMPANY(2), ADMIN(3);

	private static final Map<Integer,ClientType> clientTypeLookup = new HashMap<Integer,ClientType>();
	
	static {
		for(ClientType client : EnumSet.allOf(ClientType.class))
			clientTypeLookup.put(client.getClientCode(), client);
	}
	
	private int clientCode;

	ClientType(int clientCode) {
		this.clientCode = clientCode;
	}

	public int getClientCode() {
		return clientCode;
	}
	
	public static ClientType getClientType(int clientCode){
		return clientTypeLookup.get(clientCode);
	}
}