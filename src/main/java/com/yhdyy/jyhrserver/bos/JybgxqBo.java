package com.yhdyy.jyhrserver.bos;

import lombok.Data;

import java.util.Date;

@Data
public class JybgxqBo {
    public String PatientID;
    public Date Test_report_date;
    public String Visit_No;
    public String Visit_type;
    public String Test_sample_No;
    public String Electronic_aplc_No;
    public String Outpatient_No;
    public String Inpatient_No;
    public String Test_report_No;
    public String Test_method_name;
    public String Test_quantitative_result;
    public String Test_quantitative_result_unit;
    public String Test_result_code;
    public String Test_result_name;
    public String Test_item_code;
    public String Test_item_name;
    public String Hr_result_code;
    public String Hr_result_name;
    public String Range_high;
    public String Range_low;
    public String Range_text;
    public String Result_status;
    public Integer Sort_No;
    public String abbreviation;
}
