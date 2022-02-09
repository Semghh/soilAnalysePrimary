package com.example.content2.Util.Execel;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;


@Component
public class GenerateSmartCard {

    private static HSSFWorkbook template ;

    @Value(value = "${excelTemplate.absolutePath}")
    private String absolutePath;

    private judgerProxy proxy  = new judgerProxy();

    public GenerateSmartCard() {
    }

    public static HSSFWorkbook getTemplateExcel(String absolutePath) throws TemplateFileNotFound, notCorrectFile {
        File file = new File(absolutePath);
        //检查是否存在
        if (!file.exists()) throw new TemplateFileNotFound(absolutePath);
        //检查文件格式
        String name = file.getName();
        if (!isXlsFile(name)){
            throw new notCorrectFile(name);
        }
        try {
            return new HSSFWorkbook(new FileInputStream(file));

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }


    private static boolean isXlsFile(String name){
        return name.endsWith(".xls");
    }



    public static class notCorrectFile extends Exception{
        public notCorrectFile(String message) {
            super("不是以 .xls 结尾的文件 : " + message);
        }
    }


    public static class TemplateFileNotFound extends Exception{

        public TemplateFileNotFound(String message) {
            super("找不到Excel模板文件 : "+ message);
        }
    }


    /**
     *
     *  2022-02-09 21:38:06
     *  第一个版本，仅更新了土壤指标 : 提出了复用方法updateSoilMsg
     *
     * @param indicatedPath 指定的路径  (包括文件名.xls)
     * @param fields  依次为土壤中的  氮、磷、钾、有机质、ph
     *                "mea_Effective_N","mea_Olsen_P","mea_Olsen_K","mea_organic_matter","mea_ph"
     * @throws IOException
     * @throws notCorrectFile
     * @throws TemplateFileNotFound
     */
    public void generateSmartCard1(String indicatedPath,Object[] fields) throws IOException,
            notCorrectFile, TemplateFileNotFound {
        if (template==null){
            template = getTemplateExcel(absolutePath);
        }
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(absolutePath));

        updateSoilMsg(workbook,fields);

        out(workbook,indicatedPath);

        workbook.close();

    }


    public String out(HSSFWorkbook aim, String path) throws IOException {
        File f = new File(path);
        aim.write(f);
        aim.close();
        return f.getAbsolutePath();
    }

    /**
     *
     * @param workbook 工作簿
     * @param fields  依次为土壤中的  氮、磷、钾、有机质、ph
     *                "mea_Effective_N","mea_Olsen_P","mea_Olsen_K","mea_organic_matter","mea_ph"
     */

    private void updateSoilMsg(HSSFWorkbook workbook,Object[] fields){


        HSSFSheet sheetAt1 = workbook.getSheetAt(1);
        HSSFRow rowAt7 = sheetAt1.getRow(7);
        HSSFRow rowAt8 = sheetAt1.getRow(8);




        //碱解氮
        HSSFCell rowAt7cellAt7 = rowAt7.getCell(7);
        rowAt7cellAt7.setCellType(CellType.STRING);
        rowAt7cellAt7.setCellValue((Double)fields[0]);
        HSSFCell rowAt7cellAt8 = rowAt7.getCell(8);
        rowAt7cellAt8.setCellType(CellType.STRING);
        rowAt7cellAt8.setCellValue(proxy.judgeForN((Double) fields[0]));


        //磷
        HSSFCell rowAt8cellAt4 = rowAt8.getCell(4);
        rowAt8cellAt4.setCellType(CellType.STRING);
        rowAt8cellAt4.setCellValue((Double)fields[1]);
        HSSFCell rowAt5cellAt5 = rowAt8.getCell(5);
        rowAt5cellAt5.setCellType(CellType.STRING);
        rowAt5cellAt5.setCellValue(proxy.judgeForP((Double) fields[1]));


        //钾
        HSSFCell rowAt8cellAt7 = rowAt8.getCell(7);
        rowAt8cellAt7.setCellType(CellType.STRING);
        rowAt8cellAt7.setCellValue((Double)fields[2]);
        HSSFCell rowAt8cellAt8 = rowAt8.getCell(8);
        rowAt8cellAt8.setCellType(CellType.STRING);
        rowAt8cellAt8.setCellValue(proxy.judgerForK((Double) fields[2]));


        //有机质
        HSSFCell rowAt7cellAt4 = rowAt7.getCell(4);
        rowAt7cellAt4.setCellType(CellType.STRING);
        rowAt7cellAt4.setCellValue(fields[3]+"%");
        HSSFCell rowAt7cellAt5 = rowAt7.getCell(5);
        rowAt7cellAt5.setCellType(CellType.STRING);
        rowAt7cellAt5.setCellValue(proxy.judgerForOrganicMatter((Double) fields[3]));

        //全氮  judge和有机质一样
        HSSFCell rowAt7cell2 = rowAt7.getCell(2);
        rowAt7cell2.setCellType(CellType.STRING);
        rowAt7cell2.setCellValue(proxy.judgerForOrganicMatter((Double) fields[3]));



        //ph
        HSSFCell rowAt8cellAt1 = rowAt8.getCell(1);
        rowAt8cellAt1.setCellType(CellType.STRING);
        rowAt8cellAt1.setCellValue((Double)fields[4]);
        HSSFCell rowAt8cellAt2 = rowAt8.getCell(2);
        rowAt8cellAt2.setCellType(CellType.STRING);
        rowAt8cellAt2.setCellValue(proxy.judgerForPH((Double) fields[4]));


    }


    /**
     *
     * 策略模式
     *
     * 共同行为： judge
     *
     * 为不同元素judge，实现了不用的策略。judgerForN judgerForP judgerForK ....
     *
     *
     */

    static interface Judger{
        public String doJudge(Double val);
    }

    static class judgerForN implements Judger{

        @Override
        public String doJudge(Double val) {
            if (val>200)return "高";
            else if (val>150) return "较高";
            else if (val>100) return "中等";
            else if (val>50) return "较低";
            else return "低";
        }
    }

    static class judgerForP implements Judger{

        @Override
        public String doJudge(Double val) {
            if (val>100)return "高";
            else if (val>40) return "较高";
            else if (val>20) return "中等";
            else if (val>10) return "较低";
            else if (val>5) return "低";
            else if (val>3) return "极低";
            else return "微少";
        }
    }

    static class judgerForK implements Judger{

        @Override
        public String doJudge(Double val) {
            if (val>200)return "高";
            else if (val>150) return "较高";
            else if (val>100) return "中等";
            else if (val>50) return "较低";
            else return "低";
        }
    }

    static class judgerForPh implements Judger{

        @Override
        public String doJudge(Double val) {
            if (val>8)return "碱性";
            else if (val>7) return "偏碱";
            else if (val==7) return "中等";
            else if (val>6) return "偏酸";
            else if (val>5) return "酸性";
            else return "酸化";
        }
    }

    static class judgerForOrganicMatter implements Judger{

        @Override
        public String doJudge(Double val) {
            val /=10;
            if (val>4)return "高";
            else if (val>3) return "较高";
            else if (val>2) return "中等";
            else if (val>1) return "较低";
            else return "低";
        }
    }

    static class judgerProxy{


        private judgerForN n = new judgerForN();
        public String judgeForN(Double v){
            return n.doJudge(v);
        }

        private judgerForP p = new judgerForP();
        public String judgeForP(Double v){
            return p.doJudge(v);
        }

        private judgerForK k = new judgerForK();
        public String judgerForK(Double v){
            return k.doJudge(v);
        }

        private judgerForOrganicMatter organicMatter = new judgerForOrganicMatter();
        public String judgerForOrganicMatter(Double v){
            return organicMatter.doJudge(v);
        }

        private judgerForPh ph = new judgerForPh();
        public String judgerForPH(Double v){
            return ph.doJudge(v);
        }
    }

}
