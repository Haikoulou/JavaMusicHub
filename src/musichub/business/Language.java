package musichub.business;

public enum Language {
	FRENCH ("french"), ENGLISH ("english"), ITALIAN ("italian"), SPANISH ("spanish"), GERMAN("german");
	private String language;
	private Language (String language) {
		this.language = language;
	}
	public String getLanguage() {
		return language;
	}
}