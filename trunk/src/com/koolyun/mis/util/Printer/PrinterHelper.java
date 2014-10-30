package com.koolyun.mis.util.Printer;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class PrinterHelper {

	public static final int alignLeft = 0;
	public static final int alignCenter = 1;
	public static final int alignRight = 2;
	public static final int alignSide = 3;

	public static int CHAR_PER_LINE = 32;

	public String getPrintStringWithFormat(String str1, String str2, int align, boolean isDoubleWidth) {
		int linecount = isDoubleWidth ? CHAR_PER_LINE / 2 : CHAR_PER_LINE;
		CharCount tmp1 = getCharCount(str1);
		CharCount tmp2 = getCharCount(str2);

		int length1 = tmp1.getCharSize();
		int length2 = tmp2.getCharSize();
		int length = length1 + length2;
		int spacecount = linecount - length;
		String retstr = "";
		if (spacecount >= 0) {
			if (align == alignLeft) {
				retstr = str1 + str2 + getNString(" ", spacecount);
			} else if (align == alignCenter) {
				if (spacecount % 2 == 0) {
					retstr = getNString(" ", spacecount / 2) + str1 + str2 + getNString(" ", spacecount / 2);
				} else {
					retstr = getNString(" ", spacecount / 2 + 1) + str1 + str2 + getNString(" ", spacecount / 2);
				}
			} else if (align == alignRight) {
				retstr = getNString(" ", spacecount) + str1 + str2;
			} else if (align == alignSide) {
				retstr = str1 + getNString(" ", spacecount) + str2;
			}
		} else {
			retstr = str1 + str2;
		}
		return retstr;
	}

	public String getPrintStringWithFormat(String str, int align, boolean isDoubleWidth) {
		int linecount = isDoubleWidth ? CHAR_PER_LINE / 2 : CHAR_PER_LINE;
		CharCount tmp = getCharCount(str);
		int length = tmp.getCharSize();
		int spacecount = linecount - length;
		String retstr = "";
		if (spacecount > 0) {
			if (align == alignLeft) {
				retstr = str + getNString(" ", spacecount);
			} else if (align == alignCenter) {
				if (spacecount % 2 == 0) {
					retstr = getNString(" ", spacecount / 2) + str + getNString(" ", spacecount / 2);
				} else {
					retstr = getNString(" ", spacecount / 2 + 1) + str + getNString(" ", spacecount / 2);
				}
			} else if (align == alignRight) {
				retstr = getNString(" ", spacecount) + str;
			}
		} else {
			retstr = str;
		}

		return retstr;
	}

	public String getNString(String str, int n) {
		StringBuilder strbld = new StringBuilder();
		for (int i = 0; i < n; i++) {
			strbld.append(str);
		}
		return strbld.toString();
	}

	public String getPrintStringWithFormat(List<PrintPare> mList, int align, boolean isDoubleWidth) {
		StringBuilder strbld = new StringBuilder();
		int size = mList.size();
		for (int i = 0; i < size; i++) {
			String tmp = "";
			CharCount tmpc = getCharCount(mList.get(i).getName());
			int numofspace = 0;
			numofspace = mList.get(i).getLength() - tmpc.getCharSize();
			if (numofspace < 0)
				numofspace = 0;
			if (align == alignLeft) {
				tmp = mList.get(i).getName() + getNString(" ", numofspace);
			} else if (align == alignCenter) {
				if (numofspace % 2 == 0) {
					tmp = getNString(" ", numofspace / 2) + mList.get(i).getName() + getNString(" ", numofspace / 2);
				} else {
					tmp = getNString(" ", numofspace / 2 + 1) + mList.get(i).getName()
							+ getNString(" ", numofspace / 2);
				}

			} else if (align == alignRight) {
				tmp = getNString(" ", numofspace) + mList.get(i).getName();
			} else if (align == alignSide) {
				if (i % 2 == 0) {
					tmp = mList.get(i).getName() + getNString(" ", numofspace);
				} else {
					tmp = getNString(" ", numofspace) + mList.get(i).getName();
				}
			}
			strbld.append(tmp);
		}

		return strbld.toString();
	}

	private CharCount getCharCount(String str) {
		int wcn = 0;
		int cn = 0;
		int acn = 0;
		if (str != null && !str.isEmpty()) {
			try {

				byte[] tmpGB2312 = str.getBytes("gb2312");
				byte[] tmpUFT8 = str.getBytes("utf8");

				wcn = tmpUFT8.length - tmpGB2312.length;
				cn = tmpGB2312.length - 2 * wcn;
				acn = str.length();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return new CharCount(wcn, cn, acn);
	}

	class CharCount {
		private int WCharCount; // 汉字个数
		private int CharCount; // 字符个数
		private int ACharCount;// 总个数

		public CharCount(int wCharCount, int charCount, int aCharCount) {
			WCharCount = wCharCount;
			CharCount = charCount;
			ACharCount = aCharCount;
		}

		public int getWCharCount() {
			return WCharCount;
		}

		public void setWCharCount(int wCharCount) {
			WCharCount = wCharCount;
		}

		public int getCharCount() {
			return CharCount;
		}

		public void setCharCount(int charCount) {
			CharCount = charCount;
		}

		public int getACharCount() {
			return ACharCount;
		}

		public void setACharCount(int aCharCount) {
			ACharCount = aCharCount;
		}

		public int getCharSize() {
			return 2 * WCharCount + CharCount;
		}
	}

	public class PrintPare {
		String name;
		int length;

		public PrintPare(String name, int length) {
			this.name = name;
			this.length = length;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

	}
}
