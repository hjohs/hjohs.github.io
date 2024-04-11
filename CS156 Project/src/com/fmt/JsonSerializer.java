package com.fmt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializer {

	public static void serialize(String fileName, List<?> list) {
	
	//Serializes persons into json file
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	try {
		FileWriter writer = new FileWriter(fileName);
		gson.toJson(list, writer);
		writer.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
}
