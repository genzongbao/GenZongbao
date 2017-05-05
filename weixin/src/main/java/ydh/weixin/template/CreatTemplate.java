package ydh.weixin.template;

/**
 * Created by Leo on 2017/2/11.
 */
public class CreatTemplate {

    public static FourKeyword loadingFourKeyword(String[] temData) {
        FourKeyword loading = new FourKeyword();
        Template first = setTemplate(temData[0], "#000000");
        loading.setFirst(first);

        Template keyword1 = setTemplate(temData[1], "#000000");
        loading.setKeyword1(keyword1);

        Template keyword2 = setTemplate(temData[2], "#000000");
        loading.setKeyword2(keyword2);

        Template keyword3 = setTemplate(temData[3], "#000000");
        loading.setKeyword3(keyword3);

        Template keyword4 = setTemplate(temData[4], "#000000");
        loading.setKeyword4(keyword4);

        Template remark = setTemplate(temData[5], "#000000");
        loading.setRemark(remark);

        return loading;
    }


    public static ThreeKeyTemplate createBindingInvite(String[] data) {
        ThreeKeyTemplate key = new ThreeKeyTemplate();
        Template first = setTemplate(data[0], "#000000");
        key.setFirst(first);

        Template keyword1 = setTemplate(data[1], "#000000");
        key.setKeyword1(keyword1);

        Template keyword2 = setTemplate(data[2], "#000000");
        key.setKeyword2(keyword2);

        Template keyword3 = setTemplate(data[3], "#000000");
        key.setKeyword3(keyword3);

        Template remark = setTemplate(data[4], "#000000");
        remark.setValue(data[4]);
        remark.setColor("#000000");
        key.setRemark(remark);

        return key;
    }

    private static Template setTemplate(String value, String color) {
        Template template = new Template();
        template.setValue(value);
        template.setColor(color);
        return template;
    }

}
