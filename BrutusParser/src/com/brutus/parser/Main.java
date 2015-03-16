package com.brutus.parser;

import java.io.File;
import java.util.ArrayList;

import com.brutus.adapter.CoreAdapter;
import com.brutus.base.Param;

public class Main {
	static ArrayList<Param> buf = new ArrayList<Param>();

	public static void main(String[] args) throws Exception {
		CoreAdapter core = new CoreAdapter();
		buf.add(new Param("luce salotto", 12));
		buf.add(new Param("luce camera", 23));
		buf.add(new Param("luce stanza", 15));
		Parser parser = new Parser(new File
				("input.txt"),core,buf);
		parser.parse();
	}
}
