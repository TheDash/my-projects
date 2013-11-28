
public class School {

		private int years;
		private String name;
		private String degree;
		
		private School() {
			
		}
		

		/**
		 * 
		 * Constructor for a school.
		 * 
		 * @param name 
		 * @param degree
		 * @param years
		 */
		public School(String name, String degree, int years) {
			this.name = name;
			this.degree = degree;
			this.years = years;
		}
		
		public String getDegree() {
			return this.degree;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getYears() {
			return this.years;
		}
}
