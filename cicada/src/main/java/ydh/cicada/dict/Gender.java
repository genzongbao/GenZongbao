package ydh.cicada.dict;


@DictEnum
public enum Gender implements TitleDict {
	FEMALE("女"), MALE("男");
	
	private String title;
	
	private Gender(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}
}
