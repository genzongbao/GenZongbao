package ydh.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFTool {
	public static final String CHARACTOR_FONT_CH_FILE = "SIMHEI.TTF";  //黑体常规  
    
    public static final Rectangle PAGE_SIZE = PageSize.A4;  
    public static final float MARGIN_LEFT = 50;  
    public static final float MARGIN_RIGHT = 50;  
    public static final float MARGIN_TOP = 50;  
    public static final float MARGIN_BOTTOM = 50;  
    public static final float SPACING = 20;  
      
      
    private Document document = null;  
      
    /** 
     * 功能:创建导出数据的目标文档 
     * @param fileName 存储文件的临时路径 
     * @return  
     * @throws IOException 
     */  
    public void createDocument(String fileName) throws IOException { 
        File file = new File(fileName); 
        file.createNewFile();
        document = new Document(PAGE_SIZE, MARGIN_LEFT, MARGIN_RIGHT, MARGIN_TOP, MARGIN_BOTTOM);  
        try {  
            PdfWriter.getInstance(document, new FileOutputStream(file));  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (DocumentException e) {  
            e.printStackTrace();  
        }  
        // 打开文档准备写入内容  
        document.open();  
    }  
      
    /** 
     * 将章节写入到指定的PDF文档中  带附表表格
     * @param chapter 
     * @return  
     */  
    public void writeChapterToDoc(Chapter chapter,PdfPTable table,Image jpg) {  
        try {  
            if(document != null) {  
                if(!document.isOpen()) document.open();  
                document.add(chapter);
                document.add(jpg);
                document.add(table);
            }  
        } catch (DocumentException e) {  
            e.printStackTrace();  
        }  
    } 
    /** 
     * 将章节写入到指定的PDF文档中 
     * @param chapter 
     * @return  
     */  
    public void writeChapterToDoc(Chapter chapter) {  
        try {  
            if(document != null) {  
                if(!document.isOpen()) document.open();  
                document.add(chapter);  
            }  
        } catch (DocumentException e) {  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * 功能  创建PDF文档中的章节 
     * @param title 章节标题 
     * @param chapterNum 章节序列号 
     * @param alignment 0表示align=left，1表示align=center 
     * @param numberDepth 章节是否带序号 设值=1 表示带序号 1.章节一；1.1小节一...，设值=0表示不带序号 
     * @param font 字体格式 
     * @return Chapter章节 
     */  
    public static Chapter createChapter(String title, int chapterNum, int alignment, int numberDepth, Font font) {  
        Paragraph chapterTitle = new Paragraph(title, font);  
        chapterTitle.setAlignment(alignment);  
        Chapter chapter = new Chapter(chapterTitle, chapterNum);
        chapter.setNumberDepth(numberDepth);   
        return chapter;  
    }  
      
    /** 
     * 功能:创建某指定章节下的小节 
     * @param chapter 指定章节 
     * @param title 小节标题 
     * @param font 字体格式 
     * @param numberDepth 小节是否带序号 设值=1 表示带序号 1.章节一；1.1小节一...，设值=0表示不带序号 
     * @return section在指定章节后追加小节 
     */  
    public static Section createSection(Chapter chapter, String title, int alignment, int numberDepth, Font font) {  
        Section section = null;  
        if(chapter != null) {  
            Paragraph sectionTitle = new Paragraph(title, font);  
            sectionTitle.setSpacingBefore(SPACING);
            sectionTitle.setAlignment(alignment);
            section = chapter.addSection(sectionTitle);  
            section.setNumberDepth(numberDepth);
        }  
        return section;  
    }  
      
    /** 
     * 功能:向PDF文档中添加的内容 
     * @param text 内容 
     * @param font 内容对应的字体 
     * @return phrase 指定字体格式的内容 
     */  
    public static Phrase createPhrase(String text,Font font) {  
        Phrase phrase = new Paragraph(text,font);  
        return phrase;  
    }  
      
    /** 
     * 功能:创建列表 
     * @param numbered  设置为 true 表明想创建一个进行编号的列表 
     * @param lettered 设置为true表示列表采用字母进行编号，为false则用数字进行编号 
     * @param symbolIndent 
     * @return list 
     */  
    public static List createList(boolean numbered, boolean lettered, float symbolIndent) {  
        List list = new List(numbered, lettered, symbolIndent);  
        return list;  
    }  
      
    /** 
     * 功能:创建列表中的项 
     * @param content 列表项中的内容 
     * @param font 字体格式 
     * @return listItem 
     */  
    public static ListItem createListItem(String content, Font font) {  
        ListItem listItem = new ListItem(content, font);  
        return listItem;  
    }  
  
    /** 
     * 功能:创造字体格式 
     * @param fontname  
     * @param size 字体大小 
     * @param style 字体风格 
     * @param color 字体颜色 
     * @return Font 
     */  
    public static Font createFont(String fontname, float size, int style, BaseColor color) {  
        Font font =  FontFactory.getFont(fontname, size, style, color);  
        return font;  
    }  
      
    /** 
     * 功能: 返回支持中文的字体---仿宋 
     * @param size 字体大小 
     * @param style 字体风格 
     * @param color 字体 颜色 
     * @return  字体格式 
     */  
    public static Font createChineseFont(float size, int style, BaseColor color) throws DocumentException, IOException {  
    	String ttfPath = ConfigTool.getString(PdfConfig.templatePath) + "/" + "simhei.ttf";
    	//String ttfPath = PDFTool.class.getResource("").getPath()+"simhei.ttf";
    	BaseFont bfChinese = BaseFont.createFont(ttfPath,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);  
        return new Font(bfChinese, size, style, color); 
    }  
      
    /** 
     * 最后关闭PDF文档 
     */  
    public void closeDocument() {  
        if(document != null) {  
            document.close();  
        }  
    }  
    /**
	 * 为表格添加一个内容
	 * @param value 值
	 * @param font  字体
	 * @param align  对齐方式
	 * @return 添加的文本框
	 */
	public PdfPCell createCell(String value, Font font, int align) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}

	/**
	 * 为表格添加一个内容 
	 * @param value  值
	 * @param font  字体
	 * @return 添加的文本框
	 */
	public PdfPCell createCell(String value, Font font) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}

	/**
	 * 为表格添加一个内容
	 * @param value 值
	 * @param font  字体
	 * @param align 对齐方式
	 * @param colspan 占多少列
	 * @return 添加的文本框
	 */
	public PdfPCell createCell(String value, Font font, int align, int colspan) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}
	/**
	 * 为表格添加一个内容
	 * @param value 值
	 * @param font 字体
	 * @param align 对齐方式
	 * @param colspan  占多少列
	 * @param boderFlag  是否有有边框
	 * @return 添加的文本框
	 */
	public PdfPCell createCell(String value, Font font, int align, int colspan,
			boolean boderFlag) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
		cell.setPhrase(new Phrase(value, font));
		cell.setPadding(3.0f);
		if (!boderFlag) {
			cell.setBorder(0);
			cell.setPaddingTop(15.0f);
			cell.setPaddingBottom(8.0f);
		}
		return cell;
	}

	/**
	 * 创建一个表格对象
	 * @param colNumber 表格的列数
	 * @return 生成的表格对象
	 */
	public PdfPTable createTable(int colNumber) {
		PdfPTable table = new PdfPTable(colNumber);
		try {
			table.setTotalWidth(550);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setBorder(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}
  
    /** 
     * 读PDF文件，使用了pdfbox开源项目 
     * @param fileName 
     */  
    public void readPDF(String fileName) {  
        File file = new File(fileName);  
        FileInputStream in = null;  
        try {  
            in = new FileInputStream(fileName);  
            // 新建一个PDF解析器对象  
            PDFParser parser = new PDFParser(in);  
            // 对PDF文件进行解析  
            parser.parse();  
            // 获取解析后得到的PDF文档对象  
            PDDocument pdfdocument = parser.getPDDocument();  
            // 新建一个PDF文本剥离器  
            PDFTextStripper stripper = new PDFTextStripper();  
            // 从PDF文档对象中剥离文本  
            String result = stripper.getText(pdfdocument);  
            System.out.println("PDF文件的文本内容如下:");  
            System.out.println(result);  
  
        } catch (Exception e) {  
            System.out.println("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);  
            e.printStackTrace();  
        } finally {  
            if (in != null) {  
                try {  
                    in.close();  
                } catch (IOException e1) {  
                }  
            }  
        }  
    } 
    
}
