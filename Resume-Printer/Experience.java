
public class Experience {

	
	private String description;
	private String title;
	
	private int months;
	private int years;
	
	private Experience() {
		
	}
	
	/**
	 * Experience
	 * 
	 * @param title
	 * @param descr
	 * @param months
	 * @param years
	 */
	public Experience(String title, String descr, int months, int years) {
		this.description = descr;
		this.description = descr;
		this.months = months;
		this.years = years;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public int getYears() {
		return this.years;
	}
	
	public int getMonths() {
		return this.months;
	}
	
	public String getTitle() {
		return this.title;
	}
	
}
