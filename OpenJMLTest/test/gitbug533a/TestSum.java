import java.util.List;
import java.util.ArrayList;

public class TestSum {
	/*@
	 private invariant (\sum int i; list.contains(i); i) > 0; 
	 @*/
	private List<Integer> list = new ArrayList<Integer>();
	
	public void add(int i) {
		list.add(i);
	}
}