import java.util.ArrayList;

public class Resume {

	private String author;
	private String title;
	private String email;
	private String cell;
	private String github;

	private Objective objective;
	private School school;

	private int schoolYear;	

	private ArrayList<Experience>     experiences = new ArrayList<Experience>();
	private ArrayList<Language>         languages = new ArrayList<Language>();
	private ArrayList<String>         values		 = new ArrayList<String>();
	private ArrayList<API>            apis        = new ArrayList<API>();
	private ArrayList<VersionControl> vcs         = new ArrayList<VersionControl>();
	private ArrayList<Skill>         skills       = new ArrayList<Skill>();
	
	public Resume() {
	}
	/**
	 * 
	 * 
	 * @param author
	 * @param title
	 * @param email
	 * @param cell
	 * @param github
	 */
	public Resume(String author, String title, String email, String cell, String github) {
		this.setAuthor(author);
		this.setTitle(title);
		this.setEmail(email);
		this.setCell(cell);
		this.setGithub(github);
	}
	
	public void printResume() {
		
	}
	
	public void addSkill(Skill sk) {
		skills.add(sk);
	}
	
	public void addLanguage(Language lan) {
		languages.add(lan);
	}
	
	public void addVersionControl(VersionControl vc) {
		vcs.add(vc);
	}
	
	public void addAPI(API api) {
		apis.add(api);
	}
	
	public void addValue(String val) {
		values.add(val);
	}
	
	public void addExperience(Experience e) {
		experiences.add(e);
	}
	
	public void setExperiences(final ArrayList<Experience> exps) {
		this.experiences = exps;
	}

	public void setLanguages(final ArrayList<Language> langs) {
		this.languages = langs;
	}

	public void setValues(final ArrayList<String> vals) {
		this.values = vals;
	}

	public void setAPIs(final ArrayList<API> apis) {
		this.apis = apis;
	}

	public void setVersionControls(final ArrayList<VersionControl> vcs) {
		this.vcs = vcs;
	}

	public void setObjective(final Objective obj) {
		this.objective = obj;
	}

	public void setSchool(final School sch) {
		this.school = sch;
	}

	public void setSchoolYear(final int yr) {
		this.schoolYear = yr;
	}

	public ArrayList<Experience> getExperiences() {
		return this.experiences;
	}

	public ArrayList<Language> getLanguages() {
		return this.languages;
	}

	public ArrayList<String> getValues() {
		return this.values;
	}

	public ArrayList<API>  getAPIs() {
		return this.apis;
	}

	public ArrayList<VersionControl> getVersionControls() {
		return this.vcs;
	}

	public int getSchoolYear() {
		return this.schoolYear;
	}

	public School getSchool() {
		return this.school;
	}

	public Objective getObjective() {
		return this.objective;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGithub() {
		return github;
	}
	public void setGithub(String github) {
		this.github = github;
	}
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	
	
}