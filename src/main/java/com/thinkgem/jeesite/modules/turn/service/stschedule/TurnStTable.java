package com.thinkgem.jeesite.modules.turn.service.stschedule;

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
