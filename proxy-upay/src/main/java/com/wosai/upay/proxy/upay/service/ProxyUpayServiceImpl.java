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

import com.wosai.upay.proxy.exception.BizResponseResolveException;
import com.wosai.upay.proxy.exception.ResponseResolveException;
import com.wosai.upay.proxy.model.Response;
import com.wosai.upay.proxy.upay.exception.ProxyUpayException;
import com.wosai.upay.proxy.upay.exception.ProxyUpayKeyStoreException;
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
        try {
            keyStore.setKey(terminalSn, secret);
        }catch(Exception ex) {
            throw new ProxyUpayKeyStoreException(ex.getMessage(), ex);
        }
        
    }

    @Override
    public Map<String, Object> pay(Map<String, Object> request)
            throws ProxyUpayException {

        // B扫C终端流程
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = this.getKey(terminalSn,request);
        try {
			Map<String, Object> response = upayApi.pay(terminalSn,
			                                         terminalKey,
			                                         request);
			
			return response;
			
		}  catch (IOException e) {
			//如果请求或获取处理结果超时
			logger.debug("call pay api timeout",e);
			try {
				//查询订单状态
				Map<String, Object> response = upayApi.query(terminalSn, terminalKey, request);
				Map<String, Object> bizData=ResponseUtil.resolve2(response);
				String status=(String) bizData.get(Order.ORDER_STATUS);
				//判断是否已经交易成功
				if(status!=null&&
						(status.equals(Order.ORDER_STATUS_PAID)||status.equals(Order.ORDER_STATUS_PAY_CANCELED))
						){
					//返回最新订单信息
					return response;
				}
			} catch (IOException e1) {
				logger.debug("possible network outage",e1);
			} catch (ResponseResolveException e1) {
				logger.debug("maybe the server is temporarily unavailable",e1);
			} catch (BizResponseResolveException ex) {
                logger.debug("if the order doesn't exist you'll end up here.", ex);
			}
			try {
				//等待一段时间继续查询
				Thread.sleep(upayApi.getFailedWaitTime());
			} catch (InterruptedException e1) {
			}

			try {
				//再次查询订单状态
				Map<String, Object> response = upayApi.query(terminalSn, terminalKey, request);
				Map<String, Object> bizData=ResponseUtil.resolve2(response);
				String status=(String) bizData.get(Order.ORDER_STATUS);
				
				//判断是否已经交易成功
				if(status!=null){
					if(status.equals(Order.ORDER_STATUS_PAID)
							||status.equals(Order.ORDER_STATUS_PAY_CANCELED)){
						//返回最新订单信息
						return response;
					}
				}
			} catch (IOException e1) {
                logger.debug("possible network outage",e1);
            } catch (ResponseResolveException e1) {
                logger.debug("maybe the server is temporarily unavailable",e1);
            } catch (BizResponseResolveException ex) {
                logger.debug("if the order doesn't exist you'll end up here.", ex);
            }
			
			try {
			    Map<String, Object> response = upayApi.cancel(terminalSn, terminalKey, request); 
			    Map<String, Object> bizData=ResponseUtil.resolve2(response);
			    return response;

			} catch (IOException e1) {
                logger.debug("possible network outage",e1);
            } catch (ResponseResolveException e1) {
                logger.debug("maybe the server is temporarily unavailable",e1);
            } catch (BizResponseResolveException ex) {
                logger.debug("if the order doesn't exist you'll end up here.", ex);
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
        String terminalKey = this.getKey(terminalSn,request);
		try {
			Map<String, Object> response = upayApi.precreate(terminalSn,
			                                         terminalKey,
			                                         request);
			return response;
		} catch (IOException e) {
			//如果请求或获取处理结果超时
			logger.debug("call precreate api timeout",e);
			try {
				//查询订单状态
				Map<String, Object> response = upayApi.query(terminalSn, terminalKey, request);
				Map<String, Object> bizData = ResponseUtil.resolve2(response);
				String status=(String) bizData.get(Order.ORDER_STATUS);
				//判断是否已经交易成功
				if(status!=null&&
						(status.equals(Order.ORDER_STATUS_PAID)||status.equals(Order.ORDER_STATUS_PAY_CANCELED))
						){
					//返回最新订单信息
					return response;
				}
			} catch (IOException e1) {
                logger.debug("possible network outage",e1);
            } catch (ResponseResolveException e1) {
                logger.debug("maybe the server is temporarily unavailable",e1);
            } catch (BizResponseResolveException ex) {
                logger.debug("if the order doesn't exist you'll end up here.", ex);
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
        String terminalKey = this.getKey(terminalSn,request);
		try {
			Map<String, Object> response = upayApi.refund(terminalSn,
			                                         terminalKey,
			                                         request);
			return response;
		} catch (IOException e) {
			//如果请求或获取处理结果超时
			logger.debug("call refund api timeout",e);
			try {
				//查询订单状态
				Map<String, Object> response = upayApi.query(terminalSn, terminalKey, request);
				Map<String, Object> bizData=ResponseUtil.resolve2(response);
				String status=(String) bizData.get(Order.ORDER_STATUS);
				//判断是否已经交易成功
				if(status!=null&&
						(status.equals(Order.ORDER_STATUS_PAID)||status.equals(Order.ORDER_STATUS_PAY_CANCELED))
						){
					//返回最新订单信息
					return response;
				}
			} catch (IOException e1) {
                logger.debug("possible network outage",e1);
            } catch (ResponseResolveException e1) {
                logger.debug("maybe the server is temporarily unavailable",e1);
            } catch (BizResponseResolveException ex) {
                logger.debug("if the order doesn't exist you'll end up here.", ex);
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
        String terminalKey = this.getKey(terminalSn,request);
        try {
            Map<String, Object> response = upayApi.query(terminalSn, terminalKey, request);
            return response;
        } catch (IOException e1) {
            logger.debug("possible network outage",e1);
            throw new UpayApiException("failed to query");
        }
    }

    @Override
    public Map<String, Object> revoke(Map<String, Object> request)
            throws ProxyUpayException {

        // 手动撤单
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = this.getKey(terminalSn,request);
        try {
            Map<String, Object> response = upayApi.revoke(terminalSn,
    		                                         terminalKey,
    		                                         request);
            return response;
        } catch (IOException e1) {
            logger.debug("possible network outage",e1);
            throw new UpayApiException("failed to revoke.");
        }
    }

    @Override
    public Map<String, Object> cancel(Map<String, Object> request)
            throws ProxyUpayException {

        // 冲正
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = this.getKey(terminalSn,request);
        try {
            Map<String, Object> response = upayApi.cancel(terminalSn,
    		                                         terminalKey,
    		                                         request);
            return response;
        } catch (IOException e1) {
            logger.debug("possible network outage",e1);
            throw new UpayApiException("failed to cancel");
        }
    }


    @Override
    public Map<String, Object> uploadLog(Map<String, Object> request)
            throws ProxyUpayException {
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        //上传日志暂时不做签到
//        String terminalKey = this.getKey(terminalSn,request);
        String terminalKey = keyStore.getKey(terminalSn);
        
        String log = (String)request.get(Order.ORDER_LOG);
        try {
            Map<String, Object> response = upayApi.uploadLog(terminalSn,
    		                                         terminalKey,
    		                                         log);
            return response;
        } catch (IOException e1) {
            logger.debug("possible network outage",e1);
            throw new UpayApiException("failed to uploadLog");
        }
    }
    

	/**
	 * 封装重复代码，实现每天第一次签到逻辑
	 * @param terminalSn
	 * @return
	 * @throws IOException 
	 */
	private synchronized String getKey(String terminalSn,Map<String,Object> request) {
		String today=sdf.format(Calendar.getInstance().getTime());
		String date=dateMap.get(terminalSn);
		logger.debug("getKey date: " + date);
		//判断今天是否签到过了
		if(date==null||!date.equals(today)){
			logger.info("getKey and the key need to update");
		    String deviceId=request.get(TerminalKey.DEVICE_ID).toString();
		    String terminalKey=keyStore.getKey(terminalSn);
		    String secret = terminalKey;
		    try {
		    	logger.debug(terminalSn+" is signing ");
    		    Map<String,Object> response = upayApi.checkin(terminalSn, terminalKey, deviceId);
    		    Map<String,Object> responseBiz = (Map<String,Object>) response.get(Response.BIZ_RESPONSE);
    		    if(responseBiz==null){
                    logger.debug("responseBiz is null");
                    throw new ProxyUpayKeyStoreException(" checkin faild.");
    		    }
    		    secret = responseBiz.get(TerminalKey.TERMINAL_KEY).toString();
		    	logger.debug(terminalSn+"' sign is "+secret);
		    } catch (IOException e1) {
                logger.debug("possible network outage",e1);
            }
		    // 更新密钥
		    dateMap.put(terminalSn, today);
		    secretMap.put(terminalSn, secret);
		    keyStore.setKey(terminalSn, secret);
		    return secret;
		}
		logger.debug("getKey from the cache.");
        return secretMap.get(terminalSn);
		

	}
}
