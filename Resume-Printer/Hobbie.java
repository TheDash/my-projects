
public class Hobbie {
	
	
	private String title;
	private String description;
	private int hoursPerWeek;
	private String purpose;
	
	@SuppressWarnings("unused")
	private Hobbie() {
		
	}
	
	/**
	 * Something you do on your spare time to pass the time
	 * 
	 * @param title
	 * @param description
	 * @param purpose
	 * @param hpw
	 */
	public Hobbie(String title, String description, String purpose, int hpw) {
		this.setTitle(title);
		this.setDescription(description);
		this.setPurpose(purpose);
		this.setHoursPerWeek(hpw);
	}

	public int getHoursPerWeek() {
		return hoursPerWeek;
	}

	public void setHoursPerWeek(int hoursPerWeek) {
		this.hoursPerWeek = hoursPerWeek;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
