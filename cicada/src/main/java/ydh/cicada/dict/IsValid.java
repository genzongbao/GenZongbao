package ydh.cicada.dict;

@DictEnum
public enum IsValid implements TitleDict {
	INVALID("无效"), VALID("有效");

	private String title;
	
	private IsValid(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
