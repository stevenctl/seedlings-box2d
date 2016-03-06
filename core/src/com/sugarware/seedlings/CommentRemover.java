package com.sugarware.seedlings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.Scanner;

public class CommentRemover {
	static final String comment = 
			"Decompiled with CFR 0_110";
	
	public static void main(String... args) throws FileNotFoundException{
		fix("C:/Users/Home/Desktop/seedlings-box2d/core/src/com/sugarware/seedlings");
	}
	
	static void fix(String directory) throws FileNotFoundException{
		
		File f = new File(directory);
		String[] directories = f.list(new FilenameFilter() {
			  @Override
			  public boolean accept(File current, String name) {
			    return new File(current, name).isDirectory();
			  }
			});
		String[] files = f.list(new FilenameFilter() {
			  @Override
			  public boolean accept(File current, String name) {
			    return !(new File(current, name).isDirectory());
			  }
			});
		if(directories !=null)for(String d : directories)fix(directory + "/" + d);
		if(files !=null)for(String fn : files)fixFile(directory + "/" + fn);
	}
	
	static void fixFile(String path) throws FileNotFoundException{
		File f = new File(path);
		String content = "";
		if(path.contains("CommentRemover.java"))return;
		System.out.println(path);
		Scanner sc = new Scanner(f);
		while(sc.hasNextLine()){
			content+=sc.nextLine() + "\n";
			if(content.contains(comment)){
				content = "";
				sc.nextLine();
			}
		}
		sc.close();
		PrintWriter p = new PrintWriter(f);
		p.println(content);
		p.close();
	}
}
