/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul.helpers;

/**
 * @author frede
 */
public class NameGenerator {
	static private String[] words = new String[45];

	static void randomNames() {
		words[0] = "ÖJA";
		words[1] = "PROG";
		words[2] = "KLUB";
		words[3] = "KE";
		words[4] = "VAM";
		words[5] = "BODA";
		words[6] = "LITT";
		words[7] = "HOPEN";
		words[8] = "LÖR";
		words[9] = "HES";
		words[10] = "VEST";
		words[11] = "LUTT";
		words[12] = "TRA";
		words[13] = "ÅTÅR";
		words[14] = "KEN";
		words[15] = "TER";
		words[16] = "NITT";
		words[17] = "FÖR";
		words[18] = "INGA";
		words[19] = "BÄR";
		words[20] = "ALLÅ";
		words[21] = "LACK";
		words[22] = "KAV";
		words[23] = "FTIG";
		words[24] = "HOLM";
		words[25] = "INGBY";
		words[26] = "SUND";
		words[27] = "GRUN";
		words[28] = "JOD";
		words[29] = "KUGGA";
		words[30] = "LOPP";
		words[31] = "LACK";
		words[32] = "STÅL";
		words[33] = "ÖMME";
		words[34] = "NYGÅ";
		words[35] = "APPA";
		words[36] = "SVIK";
		words[37] = "LEK";
		words[38] = "JÄR";
		words[39] = "GVES";
		words[40] = "HJÄR";
		words[41] = "ITTE";
		words[42] = "OCKO";
		words[43] = "ITYD";
		words[44] = "ALBO";
	}

	public static String pickRandomName() {
		randomNames();
		String name;
		int pickRandomNumber = (int) (Math.random() * words.length);
		int pickSecondRandomNumber = (int) (Math.random() * words.length);
		name = words[pickRandomNumber] + words[pickSecondRandomNumber];
		return name;
	}

}
