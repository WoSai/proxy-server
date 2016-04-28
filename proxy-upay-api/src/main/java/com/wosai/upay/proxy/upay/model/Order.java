package com.wosai.upay.proxy.upay.model;

public abstract class Order {
    public static final String CLIENT_TERMINAL = "client_terminal";
    
    public static final String CLIENT_SN = "client_sn";
    public static final String TERMINAL_SN = "terminal_sn";
    public static final String SN = "sn";
    public static final String REFUND_REQUEST_NO = "refund_request_no";
    public static final String DYNAMIC_ID = "dynamic_id";
    public static final String SUBJECT = "subject";
    public static final String BODY = "body";
    public static final String DESCRIPTION = "description";
    public static final String TOTAL_AMOUNT = "total_amount";
    public static final String REFUND_AMOUNT = "refund_amount";
    public static final String PAYWAY = "payway";
    public static final String SUB_PAYWAY = "sub_payway";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String EXTENDED = "extended";
    public static final String REFLECT = "reflect";
    public static final String OPERATOR = "operator";
    public static final String DEVICE_ID = "device_id";
    public static final String PAYER_UID = "payer_uid";
    public static final String NOTIFY_URL = "notify_url";
    
    
    //订单状态
    public static final String ORDER_STATUS = "order_status";
    public static final String ORDER_STATUS_CREATED = "CREATED"; 
    public static final String ORDER_STATUS_PAID = "PAID"; 
    public static final String ORDER_STATUS_PAY_CANCELED = "PAY_CANCELED"; 
    
    //日志
    public static final String ORDER_LOG = "log";

}
