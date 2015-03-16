package com.brutus.core.storage;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.brutus.shared.BrutusConf;
import com.brutus.shared.Constants;

public class MapWrapper implements Constants{
	
	public static MapWrapper singleton;
	protected DB db;


	public MapWrapper() {
		db = DBMaker.newFileDB(new File(BrutusConf.dbPath)).closeOnJvmShutdown().encryptionEnable("password").make();
	}

	public static synchronized MapWrapper getInstance(){
		if(singleton == null)
			singleton = new MapWrapper();
		return singleton;
	}

	public void commit(){
		if(db != null)
			db.commit();
	}

	public <T, K> ConcurrentNavigableMap<T,K> createTree(String collectionName){
		return db.getTreeMap(collectionName);
	}

	public <T, K> ConcurrentNavigableMap<T, K> getTree(String collectionName){
		return createTree(collectionName);
	}

	public <T, K> void putInTree(T key, K value,String collectionName){
		getTree( collectionName).put(key, value);
		db.commit();
	}

	public <T, K> void replaceInTree(T key, K value,String collectionName){
		getTree( collectionName).replace(key, value);
		db.commit();
	}

	public <T> Object removeFromTree(T key,String collectionName){
		return getTree(collectionName).remove(key);
	}

	public <T> Object getFromTree(T key,String collectionName){
		return getTree(collectionName).get(key);
	}


}
