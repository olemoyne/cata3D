package model.solid.manage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

import model.viewer.view.Scene;

public class SolidPrintStream extends PrintStream {

	int level;
	
	public final static int LEVEL_ALARM = 0;
	public final static int LEVEL_WARNING = 1;
	public final static int LEVEL_DEBUG = 2;
	
	
	long start;
	
	public ArrayList<Scene> scenes ;
	
	public SolidPrintStream(File file, int lvl) throws FileNotFoundException {
		super(file);
		level = lvl;
		scenes = new ArrayList<Scene>();
	}
	
	public SolidPrintStream(PrintStream file, int lvl) {
		super(file);
		level = lvl;
		scenes = new ArrayList<Scene>();
	}
	
	public int getLevel () {
		return level;
	}
	
	public void startChrono() {
		start = System.currentTimeMillis();
	}
	
	
	public void printChrono (int lvl) {
		if (lvl <= level ) super.print((System.currentTimeMillis() - start)+" - ");
	}
	
	
	public void print(boolean b, int lvl) {
		if (lvl <= level ) super.print(b);
	}

	public void print(char[] b, int lvl) {
		if (lvl <= level ) super.print(b);
	}


	public void print(double b, int lvl) {
		if (lvl <= level ) super.print(b);

	}

	public void print(float b, int lvl) {
		if (lvl <= level ) super.print(b);
	}

	public void print(int b, int lvl) {
		if (lvl <= level ) super.print(b);
	}

	public void print(long b, int lvl) {
		if (lvl <= level ) super.print(b);
	}

	public void print(Object b, int lvl) {
		if (lvl <= level ) super.print(b);
	}

	public void print(String b, int lvl) {
		if (lvl <= level ) super.print(b);
	}

	public void println(boolean b, int lvl) {
		if (lvl <= level ) super.println(b);
	}

	public void println(char b, int lvl) {
		if (lvl <= level ) super.println(b);
	}

	public void println(char[] b, int lvl) {
		if (lvl <= level ) super.println(b);
	}


	public void println(double b, int lvl) {
		if (lvl <= level ) super.println(b);
	}

	public void println(float b, int lvl) {
		if (lvl <= level ) super.println(b);
	}

	public void println(int b, int lvl) {
		if (lvl <= level ) super.println(b);
	}

	public void println(long b, int lvl) {
		if (lvl <= level ) super.println(b);
	}

	public void println(Object b, int lvl) {
		if (lvl <= level ) super.println(b);
	}

	public void println(String b, int lvl) {
		if (lvl <= level ) super.println(b);
	}

	public void addScene(Scene s) {
		scenes.add(s);
	}

	public Scene getScene(int logPos) {
		if (logPos > scenes.size() -1) return null;
		return scenes.get(logPos);
	}

	public int size() {
		return scenes.size();
	}

}
