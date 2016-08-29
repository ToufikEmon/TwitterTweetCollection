package dd;

public class Check {
	
	public static void main(String[] args) {
		String string = "15 22:08:36";
		
		for(int i=0;i<string.length();i++){
			System.out.println(i+" "+ string.charAt(i));
		}
		System.out.println(string.substring(0, 2));
		System.out.println(string.substring(3, 5));
		System.out.println(string.substring(6, 8));
		System.out.println(string.substring(9, 11));
		
	}

}
