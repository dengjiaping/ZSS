package com.koolyun.mis.txt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TxtBase {

	protected String printString;

	public TxtBase(String txtPath) throws IOException {
		File file = new File(txtPath);
		if (file.exists()) {
			System.out.print("file exists");
		} else {
			System.out.print("file not exists");
			// 不存在则创建Statistics
			file.createNewFile();
		}
	}

	public static void appendTxt(String fileName, String content) throws IOException {
		// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
		FileWriter writer = new FileWriter(fileName, true);
		writer.write(content);
		writer.close();

	}

	public static void overwriteTxt(String fileName, String content) throws IOException {
		// 打开一个写文件器，构造函数中的第二个参数false表示覆盖写文件
		FileWriter writer = new FileWriter(fileName, false);
		writer.write(content);
		writer.close();
	}

	protected void addWriteContent(String content) {
		if (content == null)
			printString += "\n";
		else
			printString += content + "\n";
	}

}
