
public class Objective {

	
	private String description;
	
	private Objective() {
		
	}
	/**
	 * 
	 * @param descr Give a description about your objective
	 */
	public Objective(String descr) {
		this.description = descr;
	}
	
	public void setDescription(String OBJECTIVE_DESCR) {
		this.description = OBJECTIVE_DESCR;
	}
	
	public String getDescription() {
		return this.description;
	}

}
