package com.symantec.dbshare.server.util;

import java.util.Random;
import java.util.UUID;

import com.symantec.dbshare.model.DBShare;

public final class DBShareBuilder {
	private DBShare dbshare;

	public DBShareBuilder() {
		dbshare = new DBShare();
	}

	public DBShareBuilder id(String id) {
		dbshare.setId(id);
		return this;
	}

	public DBShareBuilder name(String name) {
		dbshare.setName(name);
		return this;
	}

	public DBShareBuilder path(String path) {
		dbshare.setPath(path);
		return this;
	}

	public DBShareBuilder type(String type) {
		dbshare.setType(type);
		return this;
	}

	public DBShareBuilder size(String size) {
		dbshare.setSize(size);
		return this;
	}

	public DBShareBuilder clients(String clients) {
		dbshare.setClients(clients);
		return this;
	}

	public DBShareBuilder options(String options) {
		dbshare.setOptions(options);
		return this;
	}

	public DBShare build() {
		return dbshare;
	}

	public static final DBShare builder() {
		DBShareBuilder builder = new DBShareBuilder();
		Random random = new Random();
		int rand1 = random.nextInt(256) + 1;
		double rand2 = random.nextDouble();
		DBShare dbshare = builder.id(UUID.randomUUID().toString())
				.name("Test DBShare " + rand2).type("end-to-end")
				.path("/dump/test_dbshare_" + rand1).size(rand1 + "TB")
				.clients("test-client-" + rand1 + ".company.com")
				.options("no_root_squash").build();
		return dbshare;
	}

	public static DBShare createDBShare(String id) {
		DBShare dbshare = new DBShare();
		dbshare.setId(id);
		return dbshare;
	}

	public static DBShare createDBShare(String id, String name, String path,
			String type, String size, String clients, String options) {
		DBShare dbshare = new DBShare();
		dbshare.setId(id);
		dbshare.setName(name);
		dbshare.setPath(path);
		dbshare.setType(type);
		dbshare.setSize(size);
		dbshare.setClients(clients);
		dbshare.setOptions(options);
		return dbshare;
	}
}
