package org.jmlspecs.openjmltest.testcases;

import java.util.Locale;

import org.jmlspecs.openjmltest.EscBase;
import org.junit.Test;

@org.junit.FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
public class escStringsTest extends EscBase {

    public escStringsTest() {
        super(null, "z3_4_3");
    }
    
    @Override
    public void setUp() throws Exception {
        //noCollectDiagnostics = true;
        super.setUp();
        main.addOptions("-nullableByDefault"); // Because the tests were written this way
        //JmlEsc.escdebug = true;
        //org.jmlspecs.openjml.provers.YicesProver.showCommunication = 3;
    }
    
    public void helperTCX(String s, Object...list) {
    	helpTCX("tt.Testjava",
    			""
    			+ "package tt;\n"
    			+ "import org.jmlspecs.annotation.*;\n"
    			+ "@NonNullByDefault class TestJava {\n"
    			+ "  public TestJava t;\n"
    			+ s
    			+ "  public TestJava() { t = new TestJava(); }\n"
    			+ "}", 
    			list);
    } 
    
    public String stringArrayContentEquals(String [] expected) {
    	if (expected.length == 0) {
    		return "ensures \\result.length == 0;";
    	}
    	
    	String res = "";
    	for (int i = 0; i < expected.length; i++) {
    		for (int j = 0; j < expected[i].length(); j++) {
    			res += String.format("ensures \\result[%d].charAt(%d) == %s.charAt(%d);", i, j, "\"" + expected[i] +  "\"", j);
    		}
    	}
    	
    	return res;
    }
    
    public String contentEquals(String expected) {
    	if (expected.isEmpty()) {
    		return "ensures \\result.isEmpty() == true;";
    	}
    	
    	String res = "";
    	for (int i = 0; i < expected.length(); i++) {
    		res += String.format("ensures \\result.charAt(%d) == %s.charAt(%d);", i, "\"" + expected + "\"", i);
    	}
    	
    	return res;
    }
    
    public void substringContentEquals(String s, int start, int end) {
    	helperTCX(""
    			+ "	/*@"
    			+ "	requires s == \"" + s + "\";"
    			+ contentEquals(s.substring(start, end))
    			+ "	@*/"
                +"  public String m1(String s) {"
                +"       return s.substring(" + start + ", " + end + ");"
                +"  }"
                );
    }
    
    public void replace(String s, char t, char t2) {    	
    	helperTCX(""
    			+ "/*@"
    			+ "	requires s == \"" + s + "\";"
    			+ contentEquals(s.replace(t, t2))
    			+ "@*/"
    			+ "public String m1(String s) {"
    			+ "	return s.replace('" + t + "', '" + t2 + "');"
    			+ "}"
    			);
    }
    
    public void replaceFirst(String s, String regex, String replacement) {
    	helperTCX(""
    			+ "/*@"
    			+ "	requires s == \"" + s + "\";"
    			+ contentEquals(s.replaceFirst(regex, replacement))
    			+ "@*/"
    			+ "public String m1(String s) {"
    			+ "	return s.replaceFirst(\"" + regex + "\", \"" + replacement + "\");"
    			+ "}"
    			);
    }
    
    public void replaceAll(String s, String regex, String replacement) {
    	helperTCX(""
    			+ "/*@"
    			+ "	requires s == \"" + s + "\";"
    			+ contentEquals(s.replaceAll(regex, replacement))
    			+ "@*/"
    			+ "public String m1(String s) {"
    			+ "	return s.replaceAll(\"" + regex + "\", \"" + replacement + "\");"
    			+ "}"
    			);
    }
    
    public void split(String s, String regex) {    	
    	helperTCX(""
    			+ "/*@"
    			+ "	requires s == \"" + s + "\";"
    			+ stringArrayContentEquals(s.split(regex))
    			+ "@*/"
    			+ "public String [] m1(String s) {"
    			+ "	return s.split(\"" + regex + "\");"
    			+ "}"
    			);
    }
    
    public void splitLimit(String s, String regex, int limit) {
    	helperTCX(""
    			+ "/*@"
    			+ "	requires s == \"" + s + "\";"
    			+ stringArrayContentEquals(s.split(regex, limit))
    			+ "@*/"
    			+ "public String [] m1(String s) {"
    			+ "	return s.split(\"" + regex + "\", " + limit + ");"
    			+ "}"
    			);
    }
    
    public void valueOf(String input, String type, String expected) {
    	helperTCX(""
    			+ "/*@"
    			+ "	requires s == " + input + ";"
    			+ contentEquals(expected)
    			+ "@*/"
    			+ "public String m1(" + type + " s) {"
    			+ "	return String.valueOf(s);"
    			+ "}"
    			);
    }
    
    public void trim(String input) {
    	helperTCX(""
    			+ "/*@"
    			+ "	requires s == \"" + input + "\";"
    			+ contentEquals(input.trim())
    			+ "@*/"
    			+ "public String m1(String s) {"
    			+ "	return s.trim();"
    			+ "}"
    			);
    }
    
    public void valueOfChars(String input, String type, String expected) {
    	String t = (""
    			+ "/*@"
    			+ "	requires java.util.Arrays.equals(s," + input + ");"
    			+ contentEquals(expected)
    			+ "@*/"
    			+ "public String m1(" + type + " s) {"
    			+ "	return String.valueOf(s);"
    			+ "}");
//    	System.out.println(t.replaceAll(";", "\n"));
    	helperTCX(t);
    }
    
    public void valueOfChars(String input, String type, String expected, int offset, int count) {
    	String t = (""
    			+ "/*@"
    			+ "	requires java.util.Arrays.equals(s," + input + ");"
    			+ contentEquals(expected)
    			+ "@*/"
    			+ "public String m1(" + type + " s) {"
    			+ "	return String.valueOf(s," + offset + ", " + count + ");"
    			+ "}");
//    	System.out.println(t.replaceAll(";", "\n"));
    	helperTCX(t);
    }
    
    public void toLowerCase(String input, Locale locale) {
    	helperTCX(""
    			+ "/*@"
    			+ "	requires s == \"" + input + "\";"
    			+ contentEquals(input.toLowerCase(Locale.getDefault()))
    			+ "@*/"
    			+ "public String m1(String s) {"
    			+ "	return s.toLowerCase(java.util.Locale.getDefault());"
    			+ "}"
    			);
    }
    
    public void toUpperCase(String input, Locale locale) {
    	helperTCX(""
    			+ "/*@"
    			+ "	requires s == \"" + input + "\";"
    			+ contentEquals(input.toUpperCase(Locale.getDefault()))
    			+ "@*/"
    			+ "public String m1(String s) {"
    			+ "	return s.toUpperCase(java.util.Locale.getDefault());"
    			+ "}"
    			);
    }
    
    public void toCharArray(String input) {
    	
    	String arrayEquals = "";
    	if (input.length() == 0) {
    		arrayEquals = "ensures \\result.length == 0;";
    	} else {
    		for (int i = 0; i < input.length(); i++) {
    			arrayEquals += String.format("ensures \\result[%d] == s.charAt(%d);", i, i);
    		}    		
    	}
    	
    	helperTCX(""
    			+ "/*@"
    			+ "	requires s == \"" + input + "\";"
    			+ arrayEquals
    			+ "@*/"
    			+ "public char [] m1(String s) {"
    			+ "	return s.toCharArray();"
    			+ "}"
    			);
    }
    
    /** FAILED */
    @Test
    public void testToLowerCaseLocale() {
    	toLowerCase("abcdef", Locale.getDefault()); // TODO: Only supports default
    }
    
    /** FAILED */
    @Test
    public void testToUpperCaseLocale() {
    	toUpperCase("abcdef", Locale.getDefault()); // TODO: Only supports default
    }
    
    static String s = "abcdef";
    
    /** PASSED */
    @Test
    public void testToCharArray() {
    	toCharArray("openjml");
    }

    /** PASSED */
    @Test
    public void testToCharArrayEmpty() {
    	toCharArray("");
    }
    
    /** PASSED */
    @Test
    public void testSubstringStart() {
    	substringContentEquals(s, 0, 3);
    }
    
    /** PASSED */
    @Test
    public void testSubstringEnd() {
    	substringContentEquals(s, 2, s.length());
    }
    
    /** PASSED */
    @Test
    public void testSubstringMiddle() {
    	substringContentEquals(s, 2, 4);
    }
    
    /** PASSED */
    @Test
    public void testSubstringWhole() {
    	substringContentEquals(s, 0, s.length());
    }
    
    static String replaceS = "java";
    
    /** PASSED */
    @Test
    public void testReplaceWithReplacement() {
    	replace(replaceS, 'a', 'o');
    }
    
    /** PASSED */
    @Test
    public void testReplaceWithoutReplacement() {
    	replace(replaceS, 'e', 'o');
    }
    
    /** FAILED */
    @Test
    public void testMatches() {
    	helperTCX(""
    			+ "/*@"
    			+ "requires s == \"abcdef\";"
    			+ "ensures \\result == true;"
    			+ "@*/"
    			+ "public boolean m1(String s) {"
    			+ "	return s.matches(\"\\\\w+\");"
    			+ "}");
    }
    
    static String replaceWithStrings = "cow cow geese1";
    
    /** FAILED
     * NO REGEXES WORK */
    @Test
    public void testReplaceFirst() {
    	replaceFirst(replaceWithStrings, "cow", "");
    }
    
    @Test
    public void testReplaceAll() {
    	replaceFirst(replaceWithStrings, "cow", "");
    }
    
    /** FAILED */
    @Test
    public void testSplit() {
    	split(replaceWithStrings, " ");
    }
    
    /** FAILED */
    @Test
    public void testSplitLimit() {
    	splitLimit(replaceWithStrings, "", 1);
    }
    
    /** FAILED */
    @Test
    public void testValueOfBoolean() {
    	valueOf("false", "boolean", String.valueOf(false));
    }

    /** PASSED */
    @Test
    public void testValueOfChar() {
    	valueOf("'c'", "char", String.valueOf('c'));
    }
    
    /**
     * valueOf is equivalent to copyValueOf!!
     */
    /** PASSED */
    @Test
    public void testValueOfCharArray() {
    	String s = "openjml";
    	valueOfChars(String.format("\"%s\".toCharArray()",  s), "char[]", String.valueOf(s.toCharArray()));
    }
    
    /** FAILED */
    @Test
    public void testValueOfCharArrayOffsets() {
    	String s = "openjml";
    	valueOfChars(String.format("\"%s\".toCharArray()",  s), "char[]", String.valueOf(s.toCharArray(), 1, 3), 1, 3);
    }

    /** FAILED */
    @Test
    public void testValueOfDouble() {
    	valueOf("1.234", "double", String.valueOf(1.234));
    }

    /** FAILED */
    @Test
    public void testValueOfFloat() {
    	valueOf("1.2345", "float", String.valueOf(1.2345));
    }

    /** FAILED */
    @Test
    public void testValueOfInteger() {
    	valueOf("5", "int", String.valueOf(5));
    }

    /** FAILED */
    @Test
    public void testValueOfLong() {
    	valueOf("10000000000L", "long", String.valueOf(10000000000L));
    }

    /** FAILED */
    @Test
    public void testValueOfObject() {
    	valueOf("new String()", "Object", String.valueOf(new String()));
    }
    
    /** PASSED */
    @Test
    public void testSubSequence() {
    	helperTCX(""
    			+ "	/*@"
    			+ "	requires s == \"abcdef\";"
    			+ "	ensures \\result.charAt(0) == s.charAt(0);"
    			+ "	@*/"
                +"  public CharSequence m1(String s) {"
                +"       return s.subSequence(0, 3);"
                +"  }"
                );
    }
    
    /** PASSED */
    @Test
    public void testSimpleString() {
    	helperTCX(""
                +"  public void m1(String s) {\n"
                +"       String ss = s;\n"
                +"       //@ assert s != null;\n"
                +"       //@ assert s == ss;\n"
                +"  }\n"
                );
    }
    
    /** PASSED */
    @Test
    public void testStringLength() {
    	helperTCX(""
                +"  public void m1(String s) {\n"
                +"       String ss = s;\n"
                +"       //@ assert s != null;\n"
                +"       //@ assert s.length() == ss.length();\n"
                +"  }\n"
                );
    }
    
    /** PASSED */
    @Test
    public void testStringLength2() {
    	helperTCX(""
                +"  public void m1(String s) {\n"
                +"       String s1 = \"a\";\n"
                +"       String s2 = \"a\";\n"
                +"       //@ assert s1.length() == s2.length();\n"
                +"  }\n"
                );
    }
    
    /** FAILED */
    @Test
    public void testLastIndexOf() {
    	helperTCX(""
    			+ "	/*@\n"
    			+ "		requires s == \"abcdefg\";\n"
    			+ "		ensures \\result == 6;\n"
    			+ "	@*/\n"
                +"  public int m1(String s) {\n"
                +"       return s.lastIndexOf('g');"
                +"  }\n"
                );
    }
    
    /** FAILED */
    @Test
    public void testLastIndexOf2() {
    	helperTCX(""
    			+ "	/*@"
    			+ "		requires s == \"abcdefg\";\n"
    			+ "		ensures \\result == 6;\n"
    			+ "	@*/"
                +"  public int m1(String s) {\n"
                +"       return s.lastIndexOf('g', 2);"
                +"  }\n"
                );
    }
    
    /** FAILED */
    @Test
    public void testIntern() {
    	helperTCX("    public void m1() {\n"
    			+ "        String s = new String(\"abcdef\");\n"
    			+ "        //@ assert s != \"abcdef\";\n"
    			+ "        s = s.intern();\n"
    			+ "        //@ assert s == \"abcdef\";\n"
    			+ "    }"
                );
    }
    
    /** FAILED */
    @Test
    public void testTrimBoth() {
    	trim(" openjml ");
    }
    
    /** FAILED */
    @Test
    public void testTrimRight() {
    	trim("openjml ");
    }
    
    /** FAILED */
    @Test
    public void testTrimLeft() {
    	trim(" openjml");
    }
    
    /** FAILED */
    @Test
    public void testTrimNone() {
    	trim("openjml");
    }
    
    /** FAILED: WONT IMPLEMENT OTHERS */
    @Test
    public void testGetBytes() {
    	helperTCX(""
    			+ "	/*@"
    			+ "		requires s == \"a\";\n"
    			+ "		ensures \\result[0] == 97;\n"
    			+ "	@*/"
                +"  public byte [] m1(String s) {\n"
                +"       return s.getBytes();"
                +"  }\n"
                );
    }

	// All test case descriptions can be found in the Design Documentation
    /** Test that the length of two equal strings is the same */
    @Test
    public void testStringLength1() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"   
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"                
                +"  public void m1(String s) {\n"
                +"       String ss = s;\n"
                +"       //@ assert s != null;\n"
                +"       int lengthS = s.length();\n"
                +"       int lengthSs = ss.length();\n"
                +"       //@ assert lengthS == lengthSs;\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }

    
    /** This String declaration and assignment */
    @Test
    public void testStringEndsWith() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  public void m1(String s) {\n"
                +"       String s1 = \"abc\";\n"
                +"       boolean condition = s1.endsWith(\"c\");;\n"
                +"       //@ assert condition == false;\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }

    @Test
    public void testStringIsEmpty1() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires s.length() == 0; \n"
                +"  public void m1(String s) {\n"
                +"       boolean condition = s.isEmpty();\n"
                +"       //@ assert condition == true;\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }    
    
    @Test
    public void testStringIsEmpty2() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires s.length() > 0; \n"
                +"  public void m1(String s) {\n"
                +"       boolean condition = s.isEmpty();\n"
                +"       //@ assert condition == false;\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }  
    
    /** This String declaration and assignment */
    @Test
    public void testCharAt2() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires (s.length() > 0) && (ss.length() > 0); \n"
                +"  public void m1(String s, String ss) {\n"
                +"       int length = ss.length();\n"
                +"       String sss = ss + s;\n"
                +"       //@ assert sss.charAt(length) == s.charAt(0);\n"
                +"  }\n"
                
                +"  //@ requires (s.length() > 0); \n"
                +"  public void m2(String s) {\n"
                +"       String ss = s;\n"
                +"       //@ assert ss.charAt(0) == s.charAt(0);\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }

    @Test
    public void testContentEqualsSB1() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  public void m1(String s) {\n"
                +"       StringBuffer sb = new StringBuffer(s);"
                +"		 boolean condition = s.contentEquals(sb);\n"
                +"       //@ assert condition == true;\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    @Test
    public void testContentEqualsSB2() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires (s != ss); \n"
                +"  public void m1(String s, String ss) {\n"
                +"       StringBuffer sb = new StringBuffer(ss);\n"
                +"		 boolean condition = s.contentEquals(sb);\n"
                +"       //@ assert condition == false;\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }

    @Test
    public void testCompareTo1() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  public void m1(String s) {\n"
                +"       String ss = s;\n"
                +"       int condition = ss.compareTo(s);\n"
                +"       int condition2 = s.compareTo(ss);\n"
                +"       //@ assert condition == condition2;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }  
    
    @Test
    public void testCompareTo2() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  public void m1(String s) {\n"
                +"       String ss = s;\n"
                +"       int condition = ss.compareTo(s);\n"
                +"       //@ assert condition == 0;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }  
    
    @Test
    public void testCompareTo3() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires (s.length() == ss.length());\n"
                +"  public void m1(String s, String ss) {\n"
                +"       int i = 0;\n"
                +"       int condition = 0;\n"
                +"       //@ maintaining (condition == 0) <==> (\\forall int j; 0 <= j && j < i; s.charAt(j) == ss.charAt(j));\r\n"
                +"		 //@ maintaining 0 <= i && i <= s.length();\r\n"
                +"       for (i = 0; i < s.length(); i++){\n"
                +"            if (s.charAt(i) != ss.charAt(i)){\n"
                +"                 condition = s.charAt(i)-ss.charAt(i);\n"
                +"                 break;\n"
                +"            }\n"
                +"       }\n"
                +"       int condition2 = s.compareTo(ss);\n"
                +"       //@ assert condition == condition2;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }

    @Test
    public void testCompareTo4() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires s.length() > 0;\n"
                +"  public void m1(String s) {\n"
                +"       String ss = s + s;\n"
                +"       int lengthDiff = s.length() - ss.length();\n"
                +"       int condition = s.compareTo(ss);\n"
                +"       //@ assert condition == lengthDiff;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }   
    
    /** This String declaration and assignment */
    @Test
    public void testCharAt() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  //@ requires (s.length() > 0); \n"
                +"  public void m2(String s) {\n"
                +"       String ss = s;\n"
                +"       //@ assert ss.charAt(0) == s.charAt(0);\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }

    @Test
    public void testRegionMatches1() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  public void m1(String s) {\n"
                +"       //@ assume s.length() + s.length() <= Integer.MAX_VALUE;\n"
                +"       String ss = s + s;\n"
                +"       int len = s.length();\n"
                +"       boolean condition = ss.regionMatches(len, s, 0, len);\n"
                +"       //@ assert condition == true;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
        
        
    }

    //broken
    @Test
    public void testRegionMatches2() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires s.length() == 0;\n"
                +"  public void m1(String s) {\n"
                +"       boolean condition = s.regionMatches(0, s, 0, 0);\n"
                +"       //@ assert condition == true;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    //broken
    @Test
    public void testStartsWithIndex1() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  public void m1(String s) {\n"
                +"       //@ assume s.length() + s.length() <= Integer.MAX_VALUE;\n"
                +"       String ss = s + s;\n"
                +"       int len = s.length();\n"
                +"       boolean condition = ss.startsWith(s, len);\n"
                +"       //@ assert condition == true;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }

    @Test
    public void testStartsWithIndex2() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  public void m1(String s) {\n"
                +"       //@ assume s.length() + s.length() <= Integer.MAX_VALUE;\n"
                +"       String ss = s + s;\n"
                +"       int len = s.length();\n"
                +"       boolean condition = ss.startsWith(s, 0);\n"
                +"       //@ assert condition == true;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    @Test
    public void testEndsWith1() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  public void m1(String s) {\n"
                +"       //@ assume s.length() + s.length() <= Integer.MAX_VALUE;\n"
                +"       String ss = s + s;\n"
                +"       boolean condition = ss.endsWith(s);\n"
                +"       //@ assert condition == true;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    } 

    //hyper busted
    @Test
    public void testEndsWith2() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  public void m1(String s) {\n"
                +"       boolean condition = s.endsWith(s);\n"
                +"       //@ assert condition == true;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }

    //Broken again
    @Test
    public void testIndexOfFromIndex2() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires s.length() > 0;\n"
                +"  public void m1(String s) {\n"
                +"       char c = s.charAt(0);\n"
                +"       String ss = s+s;\n"
                +"       int index = ss.indexOf(c, s.length());\n"
                +"       //@ assert index == s.length();\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }

    @Test
    public void testLastIndexOfFromIndexCh() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires s.length() == 1;\n"
                +"  public void m1(String s) {\n"
                +"       String ss = s+s;\n"
                +"       int index = ss.lastIndexOf(s, s.length()*2-1);\n"
                +"       //@ assert index == s.length();\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }  
    
    
    @Test
    public void testConcat() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  public void m1(String s, String ss) {\n"
                +"       //@ assert s.concat(ss).equals(s + ss);\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }  
    
    @Test
    public void testLastIndexOfFromIndexStr() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires s.length() == 1;\n"
                +"  public void m1(String s) {\n"
                +"       String ss = s+s;\n"
                +"       int index = ss.lastIndexOf(s, 0);\n"
                +"       //@ assert index == s.length();\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    } 
    
    @Test
    public void testIndexOfFromIndex1() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires s.length() > 0;\n"
                +"  public void m1(String s) {\n"
                +"       char c = s.charAt(0);\n"
                +"       int index = s.indexOf(c, 0);\n"
                +"       //@ assert index == 0;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }

    @Test
    public void testToLowerCase() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires s.length() == 0;\n"
                +"  public void m1(String s) {\n"
                +"        String ss = s.toLowerCase();\n"
                +"        boolean condition = true;"
                +"        for (int i = 0; i < ss.length(); i++){\n"
                +"             char c = ss.charAt(i);\n"
                +"             condition = condition && c.isLowerCase();\n"
                +"        }\n"
                +"        //@ assert condition == true;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    @Test
    public void testToUpperCase() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires s.length() == 0;\n"
                +"  public void m1(String s) {\n"
                +"        String ss = s.toUpperCase();\n"
                +"        boolean condition = true;"
                +"        int i = 0;\n"
                +"        for (i = 0; i < ss.length(); i++){\n"
                +"             char c = ss.charAt(i);\n"
                +"             condition = condition && c.isUpperCase();\n"
                +"        }\n"
                +"        //@ assert condition == true;\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }

    @Test
    public void testToString() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  public void m1(String s) {\n"
                +"        //@ assert s == s.toString();\n"
                +"  }\n"
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    /** This String declaration and assignment */
    @Test
    public void testEqualsObject() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                +"  //@ requires (s.length() > 0); \n"
                +"  public void m1(String s) {\n"
                +"       String ss = s;\n"
                +"       //@ assert ss.equals(s);\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    /** This String declaration and assignment */
    @Test
    public void testEqualsObject2() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  //@ requires (s.length() > 0); \n"
                +"  public void m2(String s) {\n"
                +"       String ss = s + s;\n"
                +"       //@ assert ss.equals(s) == false;\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    /** This String declaration and assignment */
    @Test
    public void testGetBytesObject() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  //@ requires (s.length() > 0); \n"
                +"  public void m2(String s) {\n"
                +"       byte[] arr = s.getBytes();\n"
                +"       //@ assert arr[0] == s.charAt(0);\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    /** This String declaration and assignment */
    @Test
    public void testEqualsIgnoreCase() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  //@ requires (s.length() > 0); \n"
                +"  public void m2(String s) {\n"
                +"       String ss = \"Aye\";\n"
                +"       String sss = ss.toLowerCase();\n"
                +"       //@ assert sss.equalsIgnoreCase(ss) == true;\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    /** This String declaration and assignment */
    @Test
    public void testEqualsIgnoreCase2() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  //@ requires (s.length() > 0); \n"
                +"  public void m2(String s) {\n"
                +"       String ss = s.toLowerCase();\n"
                +"       //@ assert ss.equalsIgnoreCase(s);\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    /** This String declaration and assignment */
    @Test
    public void testCompareToIgnoreCase() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  //@ requires (s.length() > 0); \n"
                +"  public void m2(String s) {\n"
                +"       String ss = s;\n"
                +"       //@ assert ss.compareToIgnoreCase(s) == 0;\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    /** This String declaration and assignment */
    @Test
    public void testCompareToIgnoreCase2() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  //@ requires (s.length() > 0); \n"
                +"  public void m2(String s) {\n"
                +"       String ss = s.toLowerCase();\n"
                +"       //@ assert ss.compareToIgnoreCase(s) == 0;\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    /** This String declaration and assignment */
    @Test
    public void testStartsWith() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  //@ requires (s.length() > 0); \n"
                +"  public void m2(String s) {\n"
                +"       String ss = \"hey\";\n"
                +"       //@ assert ss.startsWith(\"h\") == true;\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
    
    /** This String declaration and assignment */
    @Test
    public void testStartsWith2() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  //@ requires (s.length() > 0); \n"
                +"  public void m2(String s) {\n"
                +"       String ss = s + s;\n"
                +"       boolean t = ss.startsWith(s);\n"
                +"       //@ assert t == true;\n"
                +"  }\n"
                
                +" public TestJava() { t = new TestJava(); }\n"
                +"}"
                );
    }
}