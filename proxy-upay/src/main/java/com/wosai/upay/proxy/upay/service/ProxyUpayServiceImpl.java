package com.wosai.upay.proxy.upay.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wosai.upay.proxy.exception.ResponseResolveException;
import com.wosai.upay.proxy.model.Response;
import com.wosai.upay.proxy.upay.exception.ProxyUpayException;
import com.wosai.upay.proxy.upay.exception.ProxyUpayResolveException;
import com.wosai.upay.proxy.upay.exception.UpayApiException;
import com.wosai.upay.proxy.upay.model.Order;
import com.wosai.upay.proxy.upay.model.TerminalKey;
import com.wosai.upay.proxy.util.ResponseUtil;

/**
 * 代理终端逻辑
 * <ul>
 * <li>实现终端交易流程（轮询、自动撤单）</li>
 * <li>初始化终端密钥</li>
 * <li>交易触发自动签到</li>
 * </ul>
 * @author dun
 *
 */
@Service
public class ProxyUpayServiceImpl implements ProxyUpayService {
    private static final Logger logger = LoggerFactory.getLogger(ProxyUpayServiceImpl.class); 

    @Autowired
    private UpayApiFacade upayApi;
    
    @Autowired
    private TerminalKeyStore keyStore;
    
    private static final Map<String,String> secretMap=new HashMap<String,String>();
    private static final Map<String,String> dateMap=new HashMap<String,String>();
    
    private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void init(String terminalSn, String secret)
            throws ProxyUpayException {

        // 初始化终端密钥
        // 以后自动签到会更新密钥
        keyStore.setKey(terminalSn, secret);
        
    }

    @Override
    public Map<String, Object> pay(Map<String, Object> request)
            throws ProxyUpayException {

        // B扫C终端流程
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = keyStore.getKey(terminalSn);
        try {
			Map<String, Object> response = upayApi.pay(terminalSn,
			                                         terminalKey,
			                                         request);
			try {
				return ResponseUtil.resolve(response);
			} catch (ResponseResolveException e) {
				//转发解析错误
				this.parseException(response);
			}
			
		}  catch (IOException e) {
			//如果请求或获取处理结果超时
			logger.debug("call pay api timeout",e);
			try {
				//查询订单状态
				Map<String, Object> response = upayApi.query(terminalSn, terminalKey, request);
				Map<String, Object> bizData=ResponseUtil.resolve(response);
				String status=(String) bizData.get(Order.ORDER_STATUS);
				//判断是否已经交易成功
				if(status!=null&&
						(status.equals(Order.ORDER_STATUS_PAID)||status.equals(Order.ORDER_STATUS_PAY_CANCELED))
						){
					//返回最新订单信息
					return bizData;
				}
			} catch (ProxyUpayException e1) {
				logger.debug("call pay > query Api timeout",e1);
			} catch (ResponseResolveException e1) {
				logger.debug("resolve pay > query response faild",e1);
			}
			try {
				//等待一段时间继续查询
				Thread.sleep(upayApi.getFailedWaitTime());
			} catch (InterruptedException e1) {
				logger.debug("pay sleep error",e1);
			}

			//再次查询订单状态
			String status=null;
			Map<String, Object> bizData=null;
			try {
				Map<String, Object> response = upayApi.query(terminalSn, terminalKey, request);
				bizData=ResponseUtil.resolve(response);
				status=(String) bizData.get(Order.ORDER_STATUS);
			} catch (ProxyUpayException e1) {
				logger.debug("call pay > query Api timeout",e1);
			} catch (ResponseResolveException e1) {
				logger.debug("resolve pay > query response faild",e1);
			}

			//判断是否已经交易成功
			if(status!=null){
				if(status.equals(Order.ORDER_STATUS_PAID)
						||status.equals(Order.ORDER_STATUS_PAY_CANCELED)){
					//返回最新订单信息
					return bizData;
				} else if (status.equals(Order.ORDER_STATUS_CREATED)){
					try {
						bizData=ResponseUtil.resolve(upayApi.cancel(terminalSn, terminalKey, request));
						return bizData;
					} catch (ResponseResolveException e1) {
						logger.debug("resolve pay > query > cancel response faild",e1);
					}
				}
			}
		}

        //所有执行都失败，电话联系客服
        throw new UpayApiException(" call pay api failed , please contact customer service ");
    }

    @Override
    public Map<String, Object> precreate(Map<String, Object> request)
            throws ProxyUpayException {

        // C扫B入口
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = keyStore.getKey(terminalSn);
		try {
			Map<String, Object> response = upayApi.precreate(terminalSn,
			                                         terminalKey,
			                                         request);
			try {
     				return ResponseUtil.resolve(response);
     			} catch (ResponseResolveException e) {
     				//转发解析错误
     				this.parseException(response);
     			}
		} catch (IOException e) {
			//如果请求或获取处理结果超时
			logger.debug("call precreate api timeout",e);
			try {
				//查询订单状态
				Map<String, Object> response = upayApi.query(terminalSn, terminalKey, request);
				Map<String, Object> bizData=ResponseUtil.resolve(response);
				String status=(String) bizData.get(Order.ORDER_STATUS);
				//判断是否已经交易成功
				if(status!=null&&
						(status.equals(Order.ORDER_STATUS_PAID)||status.equals(Order.ORDER_STATUS_PAY_CANCELED))
						){
					//返回最新订单信息
					return bizData;
				}
			} catch (ProxyUpayException e1) {
				logger.debug("call pay > query Api timeout",e1);
			} catch (ResponseResolveException e1) {
				logger.debug("resolve pay > query response faild",e1);
			}
		}
		
        //所有执行都失败，电话联系客服
        throw new UpayApiException(" call precreate api failed , please contact customer service ");
    }

    @Override
    public Map<String, Object> refund(Map<String, Object> request)
            throws ProxyUpayException {

        // 退款
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = keyStore.getKey(terminalSn);
		try {
			Map<String, Object> response = upayApi.refund(terminalSn,
			                                         terminalKey,
			                                         request);

			try {
     				return ResponseUtil.resolve(response);
     			} catch (ResponseResolveException e) {
     				//转发解析错误
     				this.parseException(response);
     			}
		} catch (IOException e) {
			//如果请求或获取处理结果超时
			logger.debug("call refund api timeout",e);
			try {
				//查询订单状态
				Map<String, Object> response = upayApi.query(terminalSn, terminalKey, request);
				Map<String, Object> bizData=ResponseUtil.resolve(response);
				String status=(String) bizData.get(Order.ORDER_STATUS);
				//判断是否已经交易成功
				if(status!=null&&
						(status.equals(Order.ORDER_STATUS_PAID)||status.equals(Order.ORDER_STATUS_PAY_CANCELED))
						){
					//返回最新订单信息
					return bizData;
				}
			} catch (ProxyUpayException e1) {
				logger.debug("call pay > query Api timeout",e1);
			} catch (ResponseResolveException e1) {
				logger.debug("resolve pay > query response faild",e1);
			}
		}
		
        //所有执行都失败，电话联系客服
        throw new UpayApiException(" call refund api failed , please contact customer service ");
    }

    @Override
    public Map<String, Object> query(Map<String, Object> request)
            throws ProxyUpayException {

        // 查询
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = keyStore.getKey(terminalSn);
		Map<String, Object> response = upayApi.query(terminalSn, terminalKey, request);
		try {
			return ResponseUtil.resolve(response);
		} catch (ResponseResolveException e1) {
			throw this.parseException(response);
		}
    }

    @Override
    public Map<String, Object> revoke(Map<String, Object> request)
            throws ProxyUpayException {

        // 手动撤单
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = keyStore.getKey(terminalSn);
        Map<String, Object> response = upayApi.revoke(terminalSn,
		                                         terminalKey,
		                                         request);
		try {
			return ResponseUtil.resolve(response);
		} catch (ResponseResolveException e1) {
			throw this.parseException(response);
		}
    }

    @Override
    public Map<String, Object> cancel(Map<String, Object> request)
            throws ProxyUpayException {

        // 冲正
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = keyStore.getKey(terminalSn);
        Map<String, Object> response = upayApi.cancel(terminalSn,
		                                         terminalKey,
		                                         request);
        
		try {
			return ResponseUtil.resolve(response);
		} catch (ResponseResolveException e1) {
			throw this.parseException(response);
		}
    }

    
    /**
     * 根据支付请求发起查询请求获取订单信息
     * @param request
     * @return
     */
    public Map<String, Object> getOrderDataByPayRequest(Map<String, Object> request){
    	try {
			//查询订单状态
			Map<String, Object> response = this.query(request);
			Map<String, Object> bizData=ResponseUtil.resolve(response);
			String status=(String) bizData.get(Order.ORDER_STATUS);
			//判断是否已经交易成功
			if(status!=null&&
					(status.equals(Order.ORDER_STATUS_PAID)||status.equals(Order.ORDER_STATUS_PAY_CANCELED))
					){
				//返回最新订单信息
				return bizData;
			}
		} catch (ProxyUpayException e1) {
			logger.debug("call pay > query Api timeout",e1);
		} catch (ResponseResolveException e1) {
			logger.debug("resolve pay > query response faild",e1);
		}
    	return null;
    }

    /**
     * 封装重复代码，转发服务端通信正常，但业务处理的错误信息
     * @param response
     * @return
     */
	public ProxyUpayResolveException parseException(Map<String, Object> response){
		String resultCode=(String) response.get(Response.RESULT_CODE);
		String errorCode=(String) response.get(Response.ERROR_CODE);
		String errorMessage=(String) response.get(Response.RESULT_CODE);
		return new ProxyUpayResolveException(resultCode,errorCode,errorMessage);
	}
	
	/**
	 * 封装重复代码，实现每天第一次签到逻辑(后续版本迁移到core中，改成自动签到)
	 * @param terminalSn
	 * @return
	 * @throws IOException 
	 */
	public String getKey(String terminalSn,Map<String,Object> request) throws ProxyUpayResolveException{
		String today=sdf.format(Calendar.getInstance());
		String date=dateMap.get(terminalSn);
		if(date==null||date.equals(today)){
			String deviceId=request.get(TerminalKey.DEVICE_ID).toString();
			Map<String,Object> response = upayApi.checkin(terminalSn, deviceId);
			try {
				Map<String, Object> bizData = ResponseUtil.resolve(response);
				String secret = bizData.get(bizData.get(TerminalKey.TERMINAL_SN)).toString();

				dateMap.put(terminalSn, date);
				secretMap.put(terminalSn, secret);
				return secret;
			} catch (ResponseResolveException e) {
				throw this.parseException(response);
			}
		}

        return secretMap.get(terminalSn);
		

	}
}
