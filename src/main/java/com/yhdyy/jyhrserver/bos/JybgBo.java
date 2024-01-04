package com.yhdyy.jyhrserver.bos;

import lombok.Data;

import java.util.Date;

@Data
public class JybgBo {
    public String PatientID;
    public String Visit_No;
    public String Visit_type;
    public String Test_sample_No;
    public String Electronic_aplc_No;
    public String Outpatient_No;
    public String Inpatient_No;
    public String Test_report_No;
    public String Ward_No;
    public String Bed_No;
    public String Phone;
    public String Age_years;
    public String Age_month;
    public String Patient_name;
    public String Review_phys_sign;
    public String Review_phys_sign_code;
    public String Reporting_phys_sign;
    public String Reporting_phys_sign_code;
    public String Test_phys_sign;
    public String Test_phys_sign_code;
    public String Test_technician_sign;
    public String Test_technician_sign_code;
    public String Gender_code;
    public String Gender_name;
    public String Patient_type_code;
    public Date Test_report_date;
    public Integer Test_category;
    public String Test_item_name;
    public String Test_item_code;
    public String Hr_result_code;
    public String Hr_result_name;
    public String Sample_category;
    public String Sample_name;
    public String Sample_status;
    public Date Sample_dt;
    public Date Test_dt;
    public Date Receiving_sample_dt;
    public String Test_report_notes;
    public String Test_report_organization;
    public String Test_aplc_organization;
    public String Test_report_dept;
    public String Test_aplc_dept;
    public String Dept_name;
    public String Dept_code;
    public String Hospital_id;
    public String Hospital_name;
    public String Ward_name;
    public String Ward_code;
    public String System_ID;
    public String Record_text;
    public String Test_report;
    public String Medical_record_number;
    public String Applicant;
    public String Aboratory_name;
    public String Aboratory_address;
    public String Report_form_type;
    public String Executive_department;
    public String Darcode;
    public String Detection;
}
