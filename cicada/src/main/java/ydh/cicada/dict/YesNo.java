package ydh.cicada.dict;


@DictEnum
public enum YesNo implements TitleDict {
	NO("否"), YES("是");
	
	private String title;
	
	private YesNo(String title) {
		this.title = title;
	}
	
	public String title() {
		return title;
	}

}
