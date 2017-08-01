package com.thinkgem.jeesite.modules.turn.service.stschedule;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import org.apache.poi.ss.usermodel.Row;
import org.omg.CORBA.ContextList;

import java.util.ArrayList;
import java.util.List;

public class TurnStTable {
    //第一行的日期列表
    private List<String> dateList;
    //分科室表格
    private List<StTableLine> lineList;


    public List<StTableLine> getLineList() {
        return lineList;
    }

    public void setLineList(List<StTableLine> lineList) {
        this.lineList = lineList;
    }


    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }


    public static ExportExcel convertToExcel(String name, List<TurnStTable> tableList) {
        //多个表在一起，上下之间的空挡长度设定为整体的十分之一，最少为2行
        int verticalInterval = 2;
        int columnNum = tableList.get(0).dateList.size()+1;
//        for (TurnStTable p : tableList) {
//            verticalInterval += p.getLineList().size();
//        }
//        verticalInterval /= tableList.size();
        //空白列表
        List<List<String>> blankList = Lists.newArrayList();
        for (int i = 0; i < verticalInterval; i++) {
            List<String> r = new ArrayList<>();
            for (int j = 0; j < columnNum; j++) {
                r.add("");
            }
            blankList.add(r);
        }
        List<String> headerList = Lists.newArrayList();
        List<List<String>> dataList = Lists.newArrayList();
        for (int i = 0; i < tableList.size(); i++) {
            TurnStTable table = tableList.get(i);
            //第一个table是添加成表头，其他的不是
            if (i == 0) {
                headerList.add("科室\\时间");
                headerList.addAll(table.getDateList());
            } else {
                List<String> tMidHead = new ArrayList<>();
                tMidHead.add("科室\\时间");
                tMidHead.addAll(table.getDateList());
                dataList.add(tMidHead);
            }
            //中间几行
            for (StTableLine line : table.getLineList()) {
                List<String> dataRowList = Lists.newArrayList();
                dataRowList.add(line.lineHeader.get(1));
                line.cellList.forEach(c->dataRowList.add(c.toString()));
                dataList.add(dataRowList);
            }
            //添加空格行
            dataList.addAll(blankList);
        }

        ExportExcel ee = new ExportExcel(name, headerList);
        for (List<String> aDataList : dataList) {
            Row row = ee.addRow();
            for (int j = 0; j < aDataList.size(); j++) {
                ee.addCell(row, j, aDataList.get(j));
            }
        }
        return ee;
    }

    /**
     * 单行类
     */
    public static class StTableLine {
        //对于规培来说：line header:0：depId，1：depName
        private List<String> lineHeader;

        private List<StTableCell> cellList;


        public List<StTableCell> getCellList() {
            return cellList;
        }

        public void setCellList(List<StTableCell> cellList) {
            this.cellList = cellList;
        }

        public void addCell(StTableCell cell) {
            if (this.cellList == null) {
                this.cellList = new ArrayList<>();
            }
            this.cellList.add(cell);
        }

        public List<String> getLineHeader() {
            return lineHeader;
        }

        public void setLineHeader(List<String> lineHeader) {
            this.lineHeader = lineHeader;
        }

        public void addLineHeader(String head) {
            if (this.lineHeader == null) {
                this.lineHeader = new ArrayList<>();
            }
            this.lineHeader.add(head);
        }
    }

    /**
     * 单元格类
     */
    public static class StTableCell {

        private List<String> cellHeaderList;
        private List<String> cellContentList;

        public List<String> getCellContentList() {
            return cellContentList;
        }

        public void setCellContentList(List<String> cellContentList) {
            this.cellContentList = cellContentList;
        }

        public void addCellContent(String cellContent) {
            if (this.cellContentList == null) {
                this.cellContentList = new ArrayList<>();
            }
            this.cellContentList.add(cellContent);
        }

        public List<String> getCellHeaderList() {
            return cellHeaderList;
        }

        public void setCellHeaderList(List<String> cellHeaderList) {
            this.cellHeaderList = cellHeaderList;
        }

        public void addCellHeader(String cellHeader) {
            if (this.cellHeaderList == null) {
                this.cellHeaderList = new ArrayList<>();
            }
            this.cellHeaderList.add(cellHeader);
        }

        @Override
        public String toString() {
            if (cellContentList == null || cellContentList.isEmpty())
                return "";
            StringBuilder sb = new StringBuilder();
            int i;
            for (i = 0; i < cellContentList.size() - 1; i++) {
                sb.append(cellContentList.get(i)).append(",");
            }
            sb.append(cellContentList.get(i));
            return sb.toString();
        }
    }
}
