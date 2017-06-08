package com.svv.dms.app;

import java.awt.Dimension;
import java.awt.Insets;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

/*
 * createPDFFile 创建PDF
 * word2PDF 将doc文件转PDF
 * waterMark 给PDF添加水印
 * html2PDF 将html转PDF
 * pdf2Html 将PDF转html
 */
public class PDFUtil {
	static final int wdDoNotSaveChanges = 0;// 不保存待定的更改。

	public static void word2PDF(String src, String dest) {

		String filename = src;
		String toFilename = dest;
		System.out.println("启动Word...");
		long start = System.currentTimeMillis();
		ActiveXComponent app = null;
		try {
			app = new ActiveXComponent("Word.Application");
			app.setProperty("Visible", false);
	
			Dispatch docs = app.getProperty("Documents").toDispatch();
			System.out.println("打开文档..." + filename);
			Dispatch doc = Dispatch.call(docs,//
					"Open", //
					filename,// FileName
					false,// ConfirmConversions
					true // ReadOnly
					).toDispatch();
	
			System.out.println("转换文档到PDF..." + toFilename);
			File tofile = new File(toFilename);
			if (tofile.exists()) {
				tofile.delete();
			}
			
			/*Dispatch.invoke(doc,  
					"SaveAs",  
					Dispatch.Method,  
					new Object[] {toFilename,new Variant(17)},  
					new int[1]);*/
			
			Dispatch.call(doc,//
					"SaveAs", //
					toFilename, // FileName
					17);
	
			Dispatch.call(doc, "Close", false);
			long end = System.currentTimeMillis();
			System.out.println("转换完成..用时：" + (end - start) + "ms.");
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("========Error:文档转换失败：" + e.getMessage());
		} finally {
			if (app != null)
				app.invoke("Quit", 0);
		}
	}

    public static void buidPDF(String pdfFile, String imageFile,  
            String waterMarkName, int permission) {  
        try {  
            File file = File.createTempFile("tempFile", ".pdf"); // 创建临时文件  
  
            // 生成PDF  
            if (createPDFFile(file)) {  
                waterMark(file.getPath(), imageFile, pdfFile, waterMarkName,  
                        permission); // 添加水印  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
    }  
  
    /** 
     * 创建PDF文件 
     *  
     * @param file 
     *            临时文件 
     * @return 成功/失败 
     */  
    public static boolean createPDFFile(File file) {  
        Rectangle rect = new Rectangle(PageSize.A4);  
        Document doc = new Document(rect, 50.0F, 50.0F, 50.0F, 50.0F);  
        try {  
            PdfWriter.getInstance(doc, new FileOutputStream(file));  
            doc.open();  
  
            BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", "Identity-H", true);// 使用系统字体  
  
            Font font = new Font(bf, 14.0F, 0);
            font.setStyle(37); // 设置样式  
            font.setFamily("宋体"); // 设置字体
  
            Paragraph p = new Paragraph("付 款 通 知 书\r\n", font);
            p.setAlignment(1);
            doc.add(p);
            
            //产生一个字体为HELVETICA、大小为10、带下划线的字符串：
            Chunk chunk1 = new Chunk("This text is underlined", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.UNDERLINE)); 
            p = new Paragraph();
            p.add(chunk1);
            
//            Table table = new Table(3);
//            table.setBorderWidth(1);
//            table.setBorderColor(new Color(0, 0, 255));
//            table.setPadding(5);
//            table.setSpacing(5);
//            Cell cell = new Cell("header");
//            cell.setHeader(true);
//            cell.setColspan(3);
//            table.addCell(cell);
//            table.endHeaders();
//            cell = new Cell("example cell with colspan 1 and rowspan 2");
//            cell.setRowspan(2);
//            cell.setBorderColor(new Color(255, 0, 0));
//            table.addCell(cell);
//            table.addCell("1.1");
//            table.addCell("2.1");
//            table.addCell("1.2");
//            table.addCell("2.2");
//            table.addCell("cell test1");
//            cell = new Cell("big cell");
//            cell.setRowspan(2);
//            cell.setColspan(2);
//            table.addCell(cell);
//            table.addCell("cell test2");
              
            doc.close();  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return false;  
    }  
  
//    public boolean word2PDF(String filePath) throws IOException{
//        System.out.println("==============================");
//        System.out.println("Write to pdf start:===========");
//        boolean flag = false;
//        final int margin = 10;
//        //String contents = getContents(partfile);
//        
////        //将 MultipartFile 转换为file       
////        CommonsMultipartFile cf= (CommonsMultipartFile)partfile; //这个partfile是MultipartFile的
////        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
////        File file = fi.getStoreLocation();
//        File file = new File(filePath);
//        String cont = getWord(file);
//        if(file.length()>0){
//            Document doc = null;
//            FileOutputStream fos = null;
//            PdfWriter pdfWriter = null;
//            try {
//             BaseFont baseFT = BaseFont.createFont("c://windows//fonts//simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//             //BaseFont baseFT = BaseFont.CreateFont("c://windows//fonts//simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//                //BaseFont bFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
//                Font font = new Font(baseFT);
//                font.setSize(8);
//                doc = new Document(PageSize.A4, margin, margin, margin, margin);
//                
//                //转换后的PDF的文件路径
//               fos = new FileOutputStream("C:/Users/Administrator/Desktop/aaa.pdf");
//                pdfWriter = PdfWriter.getInstance(doc, fos);
//                
//                doc.open();
//                doc.addTitle(filePath.replace("D:\\", "")); 
//                doc.add(new Paragraph(cont, font));
//                
//                flag=true;
//                System.out.println("Write to pdf end:===========");
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            } finally{
//                if(doc != null){
//                    doc.close();
//                }
//                 
//                if(pdfWriter != null){
//                    pdfWriter.close();
//                }
//                 
//                if(fos != null){
//                    fos.close();
//                }
//                 
//            }
//        }
//        return flag;
//    }
    //读取Word
    public static String getWord(File file) {
        System.out.println("==============================");
        System.out.println("Extract word start:===========");
       // File file = new File(path);
        String returnString = "";
        InputStream is;
        try {
            is = new FileInputStream(file);
            WordExtractor extractor = new WordExtractor(is);
            returnString=extractor.getText();
            System.out.println("Extract word end:===========");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnString;
    }

    //PDF加水印图片、水印文字
    public static void waterMark(String inputFile, String imageFile, String outputFile, String waterMarkName, int permission) {  
        try {  
            PdfReader reader = new PdfReader(inputFile);  
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));  
  
            BaseFont base = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", "Identity-H", true);// 使用系统字体  
  
            int total = reader.getNumberOfPages() + 1;  
            Image image = Image.getInstance(imageFile);  
  
            // 图片位置  
            image.setAbsolutePosition(400, 480);  
            PdfContentByte under;  
            int j = waterMarkName.length();  
            char c = 0;  
            int rise = 0;  
            for (int i = 1; i < total; i++) {  
                rise = 400;  
                under = stamper.getUnderContent(i);  
                under.beginText();  
                under.setFontAndSize(base, 30);  
  
                if (j >= 15) {  
                    under.setTextMatrix(200, 120);  
                    for (int k = 0; k < j; k++) {  
                        under.setTextRise(rise);  
                        c = waterMarkName.charAt(k);  
                        under.showText(c + "");  
                    }  
                } else {  
                    under.setTextMatrix(240, 100);  
                    for (int k = 0; k < j; k++) {  
                        under.setTextRise(rise);  
                        c = waterMarkName.charAt(k);  
                        under.showText(c + "");  
                        rise -= 18;
                    }  
                }  
  
                // 添加水印文字  
                under.endText();  
  
                // 添加水印图片  
                under.addImage(image);  
  
                // 画个圈  
                under.ellipse(250, 450, 350, 550);  
                under.setLineWidth(1f);  
                under.stroke();  
            }  
            stamper.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }

	public static String html2PDF(String urlstring, String fileName){
		Dimension format = PD4Constants.A4;
		boolean landscapeValue = false;
		int topValue = 10;
		int leftValue = 10;
		int rightValue = 10;
		int bottomValue = 10;
		String unitsValue = "mm";
		String proxyHost = "";
		int proxyPort = 0;
		int userSpaceWidth = 780;
		
		if (urlstring.length() > 0) {

			if (!urlstring.startsWith("http://") && !urlstring.startsWith("file:")) {
				urlstring = "file:///" + urlstring;
			}

			ByteArrayOutputStream ba = new ByteArrayOutputStream();

			if (proxyHost != null && proxyHost.length() != 0 && proxyPort != 0) {
				System.getProperties().setProperty("proxySet", "true");
				System.getProperties().setProperty("proxyHost", proxyHost);
				System.getProperties().setProperty("proxyPort", "" + proxyPort);
			}

			try {
				PD4ML pd4ml = new PD4ML();
				pd4ml.setPageSize(new java.awt.Dimension(450, 450));
				pd4ml.setPageInsets(new java.awt.Insets(20, 50, 10, 10));
				pd4ml.enableImgSplit(false);
				pd4ml.useTTF("java:fonts", true);
				pd4ml.enableDebugInfo();

				pd4ml.setPageSize(landscapeValue ? pd4ml.changePageOrientation(format) : format);

				if (unitsValue.equals("mm")) {
					pd4ml.setPageInsetsMM(new Insets(topValue, leftValue, bottomValue, rightValue));
				} else {
					pd4ml.setPageInsets(new Insets(topValue, leftValue, bottomValue, rightValue));
				}

				pd4ml.setHtmlWidth(userSpaceWidth);

				URL url = new URL(urlstring);
				pd4ml.render(urlstring, ba);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(fileName.lastIndexOf(".pdf")==-1) fileName = fileName + ".pdf";			
			return fileName;
		}
        return null;
	}

	/*
	 * @param pdfName 需要转换的pdf文件名称
	 * @param htmlName 生成的html文件名称
	 * @return
	 */
	public static boolean pdf2Html(String command,String pdfName,String htmlName){
		Runtime rt = Runtime.getRuntime();
		try {
			Process p = rt.exec(command);
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");              
		      // kick off stderr  
		    errorGobbler.start();  
		    StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), "STDOUT");  
		      // kick off stdout  
		    outGobbler.start(); 
			int w = p.waitFor();
			System.out.println(w);
			int v = p.exitValue();
			System.out.println(v);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

    public static void main(String[] args) {
       /* String imageFilePath = "D:/logo.png"; // 水印图片路径  
        String pdfFilePath = "D:/itext2.pdf"; // 文件生成路径  
        buidPDF(pdfFilePath, imageFilePath, "华亿星辰", 16); 
        System.out.println("文件已生成："+pdfFilePath);
        

		String filename = "D:\\app\\银联电子支付升级通知函.docx";
		String toFilename = "D:\\app\\test001.pdf";//filename.substring(0, filename.length()-4) + ".pdf";
		word2PDF(filename, toFilename);*/
		
		html2PDF("e:\\demo.html", "e:\\生成pdf.pdf");
		
		
    }
}

/*
pdf2html命令用法：

用法: pdf2htmlEX [options] <input.pdf> [<output.html>]
  -f,--first-page <int>         需要转换的起始页 (默认: 1)
  -l,--last-page <int>          需要转换的最后一页 (默认: 2147483647)
  --zoom <fp>                   缩放比例
  --fit-width <fp>              适合宽度 <fp> 像素
  --fit-height <fp>             适合高度 <fp> 像素
  --use-cropbox <int>           使用剪切框 (default: 1)
  --hdpi <fp>                   图像水平分辨率 (default: 144)
  --vdpi <fp>                   图像垂直分辨率 (default: 144)
  --embed <string>              指定哪些元素应该被嵌入到输出
  --embed-css <int>             将CSS文件嵌入到输出中 (default: 1)
  --embed-font <int>            将字体文件嵌入到输出中 (default: 1)
  --embed-image <int>           将图片文件嵌入到输出中 (default: 1)
  --embed-javascript <int>      将javascript文件嵌入到输出中 (default: 1)
  --embed-outline <int>         将链接嵌入到输出中 (default: 1)
  --split-pages <int>           将页面分割为单独的文件 (default: 0)
  --dest-dir <string>           指定目标目录 (default: ".")
  --css-filename <string>       生成的css文件的文件名 (default: "")
  --page-filename <string>      分割的网页名称  (default:"")
  --outline-filename <string>   生成的链接文件名称 (default:"")
  --process-nontext <int>       渲染图行，文字除外 (default: 1)
  --process-outline <int>       在html中显示链接 (default: 1)
  --printing <int>              支持打印 (default: 1)
  --fallback <int>              在备用模式下输出 (default: 0)
  --embed-external-font <int>   嵌入局部匹配的外部字体 (default: 1)
  --font-format <string>        嵌入的字体文件后缀 (ttf,otf,woff,svg) (default: "woff")
  --decompose-ligature <int>    分解连字-> fi (default:0)
  --auto-hint <int>             使用fontforge的autohint上的字体时不提示 (default: 0)
  --external-hint-tool <string> 字体外部提示工具 (overrides --auto-hint) (default: "")
  --stretch-narrow-glyph <int>  伸展狭窄的字形，而不是填充 (default: 0)
  --squeeze-wide-glyph <int>    收缩较宽的字形，而不是截断 (default: 1)
  --override-fstype <int>       clear the fstype bits in TTF/OTF fonts (default:0)
  --process-type3 <int>         convert Type 3 fonts for web (experimental) (default: 0)
  --heps <fp>                   合并文本的水平临界值，单位：像素(default: 1)
  --veps <fp>                   vertical threshold for merging text, in pixels (default: 1)
  --space-threshold <fp>        断字临界值 (临界值 * em) (default:0.125)
  --font-size-multiplier <fp>   一个大于1的值增加渲染精度 (default: 4)
  --space-as-offset <int>       把空格字符作为偏移量 (default: 0)
  --tounicode <int>             如何处理ToUnicode的CMap (0=auto, 1=force,-1=ignore) (default: 0)
  --optimize-text <int>         尽量减少用于文本的HTML元素的数目 (default: 0)
  --bg-format <string>          指定背景图像格式 (default: "png")
  -o,--owner-password <string>  所有者密码 (为了加密文件)
  -u,--user-password <string>   用户密码 (为了加密文件)
  --no-drm <int>                覆盖文档的 DRM 设置 (default: 0)
  --clean-tmp <int>             转换后删除临时文件 (default: 1)
  --data-dir <string>           指定的数据目录 (default: ".\share\pdf2htmlEX")
  --debug <int>                 打印调试信息 (default: 0)
  -v,--version                  打印版权和版本信息
  -h,--help                     打印使用帮助信息
*/
