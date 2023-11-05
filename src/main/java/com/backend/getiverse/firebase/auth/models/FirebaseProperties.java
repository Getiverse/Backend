package com.backend.getiverse.firebase.auth.models;

import lombok.Data;

@Data
public class FirebaseProperties {

	int sessionExpiryInDays;
	String databaseUrl;
	boolean enableStrictServerSession;
	boolean enableCheckSessionRevoked;
	boolean enableLogoutEverywhere;

}
