package com.brutus.andbrutus.utils;
import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class MaskMap {
	public static MaskMap instance;
	public DB database;
	static String path;
	//submask es. get last n alarm, get alarm by tag name

	public MaskMap(String path) {
		MaskMap.path = path;
		if(path == null) return;
		database = DBMaker.newFileDB(new File(path))
				.closeOnJvmShutdown()
				.encryptionEnable("password")
				.make();//if no rooted phone there is no problem for db
		instance = this;
	}


	public static synchronized MaskMap getInstance(){
		if(instance == null)
			return instance = new MaskMap(path);
		else return instance;
	}

	public void commit(){
		if(database != null)
			database.commit();
	}

	public <T, K> ConcurrentNavigableMap<T,K> createTree(String collectionName){
		return database.getTreeMap(collectionName);
	}

	public <T, K> ConcurrentNavigableMap<T, K> getTree(String collectionName){
		return createTree(collectionName);
	}

	public <T, K> void putInTree(T key, K value,String collectionName){
		getTree( collectionName).put(key, value);
		commit();
	}

	public <T, K> void replaceInTree(T key, K value,String collectionName){
		getTree( collectionName).replace(key, value);
		commit();
	}

	public <T> Object removeFromTree(T key,String collectionName){
		return getTree(collectionName).remove(key);
	}
	
	public <T> Object getFromTree(T key,String collectionName){
		return getTree(collectionName).get(key);
	}


}
