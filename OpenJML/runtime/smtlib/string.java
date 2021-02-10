package smtlib;

//@ pure
public class string {
    public static /*@ pure @*/ char at(/*@ non_null */ String s, int i) {
        return s.charAt(i);
    }
}
