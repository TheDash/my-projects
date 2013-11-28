
public class VersionControl {

	private String name;
	private int years;
	private int months;
	
	private VersionControl() {
		
	}
	/**
	 * Version control that you have used.
	 * 
	 * @param name
	 * @param years
	 * @param months
	 */
	public VersionControl(String name, int years, int months) {
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
