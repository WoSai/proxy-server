package com.wosai.upay.proxy.upay.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.internal.engine.MethodConstraintViolationImpl;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wosai.upay.helper.UpayServiceAnnotation;
import com.wosai.upay.proxy.exception.RemoteResponse400;
import com.wosai.upay.proxy.exception.RemoteResponse500;
import com.wosai.upay.proxy.model.Response;
import com.wosai.upay.proxy.upay.exception.ParameterValidationException;
import com.wosai.upay.proxy.upay.exception.ProxyUpayException;
import com.wosai.upay.proxy.upay.exception.UpayApi400;
import com.wosai.upay.proxy.upay.exception.UpayApi500;
import com.wosai.upay.proxy.upay.exception.UpayApiIOException;
import com.wosai.upay.proxy.upay.model.Order;

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
@UpayServiceAnnotation
public class ProxyUpayServiceImpl implements ProxyUpayService {
    private static final Logger logger = LoggerFactory.getLogger(ProxyUpayServiceImpl.class); 

    @Autowired
    private UpayApiFacade upayApi;
    
    @Autowired
    private CachedSnMap snMap;
    
    @Autowired
    private CachedTerminalKeyStore keyStore;

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
        String terminalKey = this.getKey(terminalSn,request);
        try {
        	//转换client_sn本地映射，解决支付失败client_sn已存在的bug
        	String client_sn = (String)request.get(Order.CLIENT_SN);
        	request.put(Order.CLIENT_SN, snMap.generateMappingClientSn(client_sn));
        	
			Map<String, Object> bizResponse = upayApi.pay(terminalSn,
			                                              terminalKey,
			                                              request);
			//成功后固化client_sn
			if(bizResponse.get(Response.RESULT_CODE).toString().indexOf(Response.RESPONSE_CODE_SUCEESS)>0){
				snMap.fixedMappingClientSn(client_sn);
			}
			return bizResponse;
			
		}  catch (IOException e) {
			//如果请求或获取处理结果超时
			logger.debug("call pay api timeout",e);
			try {
				//查询订单状态
				Map<String, Object> bizResponse = upayApi.query(terminalSn, terminalKey, request);
				Map<String, Object> data = (Map<String, Object>) bizResponse.get(Response.DATA);
				
				String status = (String) data.get(Order.ORDER_STATUS);
				//判断是否已经交易成功
				if(status != null && !status.contains(Order.ORDER_STATUS_IN_PROGRESS)) {
					//返回最新订单信息
					return bizResponse;
				}

			} catch (IOException ex) {
				logger.warn("possible network outage", ex);

			} catch (RemoteResponse400 ex) {
				logger.error("QUERY api specifications might have changed. contact software developer.", ex);
				throw new UpayApi400(String.format("upay-gatway QUERY 400 %s : %s", ex.getCode(), ex.getMessage()), ex);

			} catch (RemoteResponse500 ex) {
                logger.warn("upay-gateway is temporarily unavailable.", ex);
			}catch(MethodConstraintViolationException mcve){
				throw parseParameterValidationException(mcve);
			}

			try {
				//等待一段时间继续查询
				Thread.sleep(upayApi.getFailedWaitTime());
			} catch (InterruptedException e1) {
			}catch(MethodConstraintViolationException mcve){
				throw parseParameterValidationException(mcve);
			}

            try {
                //查询订单状态
                Map<String, Object> bizResponse = upayApi.query(terminalSn, terminalKey, request);
                Map<String, Object> data = (Map<String, Object>) bizResponse.get(Response.DATA);
                
                String status = (String) data.get(Order.ORDER_STATUS);
                //判断是否已经交易成功
                if(status != null && !status.contains(Order.ORDER_STATUS_IN_PROGRESS)) {
                    //返回最新订单信息
                    return bizResponse;
                }

            } catch (IOException ex) {
                logger.warn("possible network outage", ex);

            } catch (RemoteResponse400 ex) {
                logger.error("QUERY api specifications might have changed. contact software developer.", ex);
                throw new UpayApi400(String.format("upay-gatway QUERY 400 %s : %s", ex.getCode(), ex.getMessage()), ex);

            } catch (RemoteResponse500 ex) {
                logger.warn("upay-gateway is temporarily unavailable.", ex);
            }catch(MethodConstraintViolationException mcve){
    			throw parseParameterValidationException(mcve);
    		}
			
            try {
                // 时间到，无法确认订单成功，冲正
                return upayApi.cancel(terminalSn, terminalKey, request);

            } catch (IOException ex) {
                logger.error("PAY/CANCEL failure on i/o", ex);
                throw new UpayApiIOException("pay/cancel failure on i/o", ex);

            } catch (RemoteResponse400 ex) {
                logger.error("CANCEL api specifications might have changed. contact software developer.", ex);
                throw new UpayApi400(String.format("upay-gatway CANCEL 400 %s : %s", ex.getCode(), ex.getMessage()), ex);

            } catch (RemoteResponse500 ex) {
                logger.error("upay-gateway is temporarily unavailable.", ex);
                throw new UpayApi500(String.format("upay-gateway CANCEL 500 %s : %s", ex.getCode(), ex.getMessage()), ex);
            }catch(MethodConstraintViolationException mcve){
    			throw parseParameterValidationException(mcve);
    		}

		} catch (RemoteResponse400 ex) {
		    logger.error("PAY api specification might have changed. contact software developer.", ex);
		    throw new UpayApi400(String.format("upay-gatway PAY 400 %s : %s", ex.getCode(), ex.getMessage()), ex);

		} catch (RemoteResponse500 ex) {
            logger.error("upay-gateway is temporarily unavailable.", ex);
            throw new UpayApi500(String.format("upay-gateway PAY 500 %s : %s", ex.getCode(), ex.getMessage()), ex);
        }catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
		}

    }

    @Override
    public Map<String, Object> precreate(Map<String, Object> request)
            throws ProxyUpayException {

        // C扫B入口
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = this.getKey(terminalSn,request);

        try {
        	//转换client_sn本地映射，解决支付失败client_sn已存在的bug
        	String client_sn = (String)request.get(Order.CLIENT_SN);
        	request.put(Order.CLIENT_SN, snMap.generateMappingClientSn(client_sn));
        	
            Map<String, Object> bizResponse = upayApi.precreate(terminalSn,
                                                                terminalKey,
                                                                request);
            //成功后固化client_sn
			if(bizResponse.get(Response.RESULT_CODE).equals(Response.RESPONSE_CODE_SUCEESS)){
				snMap.fixedMappingClientSn(client_sn);
			}
            return bizResponse;

		} catch (IOException e) {
			//如果请求或获取处理结果超时
			logger.debug("call precreate api timeout",e);

            try {
                //查询订单状态
                return upayApi.query(terminalSn, terminalKey, request);

            } catch (IOException ex) {
                logger.error("PRECREATE/QUERY failure on i/o", ex);
                throw new UpayApiIOException("PRECREATE/QUERY failure on i/o", ex);

            } catch (RemoteResponse400 ex) {
                logger.error("QUERY api specifications might have changed. contact software developer.", ex);
                throw new UpayApi400(String.format("upay-gatway QUERY 400 %s : %s", ex.getCode(), ex.getMessage()), ex);

            } catch (RemoteResponse500 ex) {
                logger.error("upay-gateway is temporarily unavailable.", ex);
                throw new UpayApi500(String.format("upay-gatway QUERY 500 %s : %s", ex.getCode(), ex.getMessage()), ex);
            }catch(MethodConstraintViolationException mcve){
    			throw parseParameterValidationException(mcve);
    		}
		} catch (RemoteResponse400 ex) {
            logger.error("PRECREATE api specifications might have changed. contact software developer.", ex);
            throw new UpayApi400(String.format("upay-gatway PRECREATE 400 %s : %s", ex.getCode(), ex.getMessage()), ex);
 
		} catch (RemoteResponse500 ex) {
            logger.error("upay-gateway is temporarily unavailable.", ex);
            throw new UpayApi500(String.format("upay-gatway PRECREATE 500 %s : %s", ex.getCode(), ex.getMessage()), ex);

		}catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
		}
		
    }

    @Override
    public Map<String, Object> refund(Map<String, Object> request)
            throws ProxyUpayException {

        // 退款
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = this.getKey(terminalSn,request);

        try {
        	
        	//转换client_sn本地映射，解决支付失败client_sn已存在的bug
        	String client_sn = (String)request.get(Order.CLIENT_SN);
        	if(client_sn!=null){
            	request.put(Order.CLIENT_SN, snMap.getMappingClientSn(client_sn));
        	}
        	
            Map<String, Object> bizResponse = upayApi.refund(terminalSn,
                                                             terminalKey,
                                                             request);
            return bizResponse;

        } catch (IOException ex) {
            logger.error("REFUND failure on i/o", ex);
            throw new UpayApiIOException("REFUND failure on i/o", ex);

        } catch (RemoteResponse400 ex) {
            logger.error("REFUND api specifications might have changed. contact software developer.", ex);
            throw new UpayApi400(String.format("upay-gatway REFUND 400 %s : %s", ex.getCode(), ex.getMessage()), ex);
 
        } catch (RemoteResponse500 ex) {
            logger.error("upay-gateway is temporarily unavailable.", ex);
            throw new UpayApi500(String.format("upay-gatway REFUND 500 %s : %s", ex.getCode(), ex.getMessage()), ex);

        } catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
		}
    }

    @Override
    public Map<String, Object> query(Map<String, Object> request)
            throws ProxyUpayException {

        // 查询
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = this.getKey(terminalSn,request);
        try {
        	
        	//转换client_sn本地映射，解决支付失败client_sn已存在的bug
        	String client_sn = (String)request.get(Order.CLIENT_SN);
        	if(client_sn!=null){
            	request.put(Order.CLIENT_SN, snMap.getMappingClientSn(client_sn));
        	}
        	
            Map<String, Object> bizResponse = upayApi.query(terminalSn, terminalKey, request);
            return bizResponse;

        } catch (IOException ex) {
            logger.error("QUERY failure on i/o", ex);
            throw new UpayApiIOException("QUERY failure on i/o", ex);

        } catch (RemoteResponse400 ex) {
            logger.error("QUERY api specifications might have changed. contact software developer.", ex);
            throw new UpayApi400(String.format("upay-gatway QUERY 400 %s : %s", ex.getCode(), ex.getMessage()), ex);

        } catch (RemoteResponse500 ex) {
            logger.error("upay-gateway is temporarily unavailable.", ex);
            throw new UpayApi500(String.format("upay-gatway QUERY 500 %s : %s", ex.getCode(), ex.getMessage()), ex);
        } catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
		}
    }

    @Override
    public Map<String, Object> revoke(Map<String, Object> request)
            throws ProxyUpayException {

        // 手动撤单

        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = this.getKey(terminalSn,request);
        try {
        	
        	//转换client_sn本地映射，解决支付失败client_sn已存在的bug
        	String client_sn = (String)request.get(Order.CLIENT_SN);
        	if(client_sn!=null){
            	request.put(Order.CLIENT_SN, snMap.getMappingClientSn(client_sn));
        	}
        	
            Map<String, Object> bizResponse = upayApi.revoke(terminalSn,
                                                             terminalKey,
                                                             request);
            return bizResponse;

        } catch (IOException ex) {
            logger.error("REVOKE failure on i/o", ex);
            throw new UpayApiIOException("REVOKE failure on i/o", ex);

        } catch (RemoteResponse400 ex) {
            logger.error("REVOKE api specifications might have changed. contact software developer.", ex);
            throw new UpayApi400(String.format("upay-gatway REVOKE 400 %s : %s", ex.getCode(), ex.getMessage()), ex);
 
        } catch (RemoteResponse500 ex) {
            logger.error("upay-gateway is temporarily unavailable.", ex);
            throw new UpayApi500(String.format("upay-gatway REVOKE 500 %s : %s", ex.getCode(), ex.getMessage()), ex);

        } catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
		}
    }

    @Override
    public Map<String, Object> cancel(Map<String, Object> request)
            throws ProxyUpayException {

        // 冲正

        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = this.getKey(terminalSn,request);
        try {
        	
        	//转换client_sn本地映射，解决支付失败client_sn已存在的bug
        	String client_sn = (String)request.get(Order.CLIENT_SN);
        	if(client_sn!=null){
            	request.put(Order.CLIENT_SN, snMap.getMappingClientSn(client_sn));
        	}
        	
            Map<String, Object> bizResponse = upayApi.cancel(terminalSn,
                                                             terminalKey,
                                                             request);
            return bizResponse;

        } catch (IOException ex) {
            logger.error("CANCEL failure on i/o", ex);
            throw new UpayApiIOException("CANCEL failure on i/o", ex);

        } catch (RemoteResponse400 ex) {
            logger.error("CANCEL api specifications might have changed. contact software developer.", ex);
            throw new UpayApi400(String.format("upay-gatway CANCEL 400 %s : %s", ex.getCode(), ex.getMessage()), ex);
 
        } catch (RemoteResponse500 ex) {
            logger.error("upay-gateway is temporarily unavailable.", ex);
            throw new UpayApi500(String.format("upay-gatway CANCEL 500 %s : %s", ex.getCode(), ex.getMessage()), ex);

        } catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
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
            throw new UpayApiIOException("failed to uploadLog");
        } catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
		}
    }
    

	/**
	 * 封装重复代码，实现每天第一次签到逻辑
	 * @param terminalSn
	 * @return
	 * @throws IOException 
	 */
	private String getKey(String terminalSn,Map<String,Object> request) {
	    
	    return keyStore.getKey(terminalSn);

	}
	
	/**
	 * 转换验证异常
	 * @param mcve
	 * @return
	 */
	public ParameterValidationException parseParameterValidationException(MethodConstraintViolationException mcve){
		MethodConstraintViolationImpl mcvi=(MethodConstraintViolationImpl)mcve.getConstraintViolations().iterator().next();
		Map<Object,Object> invalidValue=(Map<Object,Object>)mcvi.getConstraintDescriptor().getAttributes();
		String key = String.valueOf(invalidValue.get("value"));
		return new ParameterValidationException(new StringBuilder("invalid ").append(key).append(".").toString());
	}

	public void setSnMap(CachedSnMap snMap) {
		this.snMap = snMap;
	}

	public void setKeyStore(CachedTerminalKeyStore keyStore) {
		this.keyStore = keyStore;
	}
	
}
