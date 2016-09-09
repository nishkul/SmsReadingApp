package dto;

/**
 * Created by root on 8/9/16.
 */

public class ExcelDatadto {
    private String ColumnName;
    private String ColumnValue;

    public String getColumnValue() {
        return ColumnValue;
    }

    public void setColumnValue(String columnValue) {
        ColumnValue = columnValue;
    }

    public String getColumnName() {
        return ColumnName;
    }

    public void setColumnName(String columnName) {
        ColumnName = columnName;
    }
}
