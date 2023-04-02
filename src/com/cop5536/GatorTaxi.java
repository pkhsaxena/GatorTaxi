package com.cop5536;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GatorTaxi {

	public static void main(String[] args) {

		File file = checkFile(args);
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

			String line;
			int i =0;
			while ((line = bufferedReader.readLine()) != null) {
				i++;
				System.out.println(line);
			}
			System.out.println(i);
		} catch (IOException e) {
			System.out.println("Error reading file");
			e.printStackTrace();
		}

	}

	public static File checkFile(String[] args) {
		if (args.length == 0) {
			System.out.println("Please provide a filename as a command line argument.");
			System.exit(1);
		}

		String filename = args[0];
		File file = new File(filename);

		if (!file.exists() || !file.canRead()) {
			System.out.println("Cannot read file: " + filename);
			System.exit(1);
		}

		return file;
	}

}
