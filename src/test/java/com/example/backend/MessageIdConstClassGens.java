package com.example.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import com.example.backend.domain.message.MessageIds;

/**
 * 
 * メッセージIDの定数クラスを自動生成するツール九明日
 * 
 *
 */
public class MessageIdConstClassGens {
	public static void main(String[] args) throws IOException {
		// message properties file
		InputStream inputStream = new FileInputStream("src/main/resources/messages.properties");
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		Class<?> targetClazz = MessageIds.class;
		File output = new File("src/main/java/" + targetClazz.getName().replaceAll(Pattern.quote("."), "/") + ".java");
		System.out.println("write " + output.getAbsolutePath());
		PrintWriter pw = new PrintWriter(output);

		try {
			pw.println("package " + targetClazz.getPackage().getName() + ";");
			pw.println("/**");
			pw.println(" * Message Id");
			pw.println(" * Auto-Generarted By MessageKeysGen Class");
			pw.println(" */");
			pw.println("public class " + targetClazz.getSimpleName() + " {");

			String line;
			while ((line = br.readLine()) != null) {
				writeConst(pw, line);
			}
			pw.println("}");
			pw.flush();
		} finally {
			br.close();
			pw.close();
		}
	}
	private static void writeConst(PrintWriter pw, String line) {
		if (line.startsWith("#")) {
			return;
		}
		if (!(line.startsWith("e.") || line.startsWith("i.") || line.startsWith("w."))) {
			return;
		}
		if (line.contains(".fw.")) {
			return;
		}
		String[] vals = line.split("=", 2);
		if (vals.length > 1) {
			String key = vals[0].trim();
			String value = vals[1].trim();
			pw.println("    public static final String "
					+ key.toUpperCase().replaceAll(Pattern.quote("."), "_").replaceAll(Pattern.quote("-"), "_")
					+ " = \"" + key + "\";");
		}
		return;
	}
}