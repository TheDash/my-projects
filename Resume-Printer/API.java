
public class API {

	private String name;
	private int years;
	private int months;
	
	private API() {
		
	}
	
	/**
	 * Application's programmer interface you have used.
	 * 
	 * @param name
	 * @param years
	 * @param months
	 */
	public API(String name, int years, int months) {
		this.name = name;
		this.years = years;
		this.months = months;
	}
	
	public int getYears() {
		return this.years;
	}
	
	public int getMonths() {
		return this.months;
	}
	
	public String getName() {
		return this.name;
	}
}
