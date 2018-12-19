package inspur.dzzw.web.action;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import inspur.dzzw.messageclient.mas.webservice.AppServiceApi;
import inspur.dzzw.messageclient.mas.webservice.model.RespInfo;
import inspur.dzzw.messageclient.mas.webservice.model.SendRespMsg;
import inspur.dzzw.web.po.MessageLogBean;
import inspur.dzzw.web.service.MessageLogService;



@Controller
@RequestMapping("/app")
public class MessageLogController {

	@Autowired
	MessageLogService messageLogService;
	
	// 鉴权账号
	@Value("${spid}")
	private String spid;

	@Value("${pwd}")
	private String pwd;

	@Value("${srcNo}")
	private String srcNo;

	@Value("${sign}")
	private String sign;
	
	@RequestMapping("/sendMessage")
	@ResponseBody
	public void sendMessage(HttpServletRequest request ,HttpServletResponse response) {
		String app_code = request.getParameter("app_code");
		String number	= request.getParameter("number");
		String content	= request.getParameter("content") ;

		AppServiceApi serviceApi = AppServiceApi.getInstance();
		MessageLogBean messageLogBean=new MessageLogBean();
		JSONObject json = new JSONObject();
		String passport = null;
			
		serviceApi.setEndPoint("http://218.206.27.231:8085/mas_webservice/services/AppService");
		try {
			RespInfo respInfo = serviceApi.auth(spid, pwd);
			if (StringUtils.isEmpty(app_code)) {
				throw new Exception("app_code不能为空！");
			}
			if (StringUtils.isEmpty(number)) {
				throw new Exception("number不能为空！");
			}
			if (StringUtils.isEmpty(content)) {
				throw new Exception("content不能为空！");
			}
			if (respInfo.getRespCode() == 0) {
				passport = respInfo.getRespMessage();
				String[] receiveList = { number };
				String Content	= content+ "[" + sign + "]";
				respInfo = serviceApi.sendSms(passport, 1, srcNo, receiveList, Content, true);
				if (respInfo.getRespCode() != 0) {
					throw new Exception("短信提交失败,错误码:" + respInfo.getRespCode() + "," + respInfo.getRespMessage());
				}
				SendRespMsg[] sendRespMsgs = serviceApi.getSendResp(passport);
				if (sendRespMsgs != null) {
					for (SendRespMsg sendRespMsg : sendRespMsgs) {
						System.out.println("短信应答:" + sendRespMsg.getMsgid() + "," + sendRespMsg.getType() + "," + sendRespMsg.getResult());
						messageLogBean.setCreateTime(new Date());
						messageLogBean.setId(String.valueOf(UUID.randomUUID()));
						messageLogBean.setMsgid(String.valueOf(sendRespMsg.getMsgid()));
						messageLogBean.setNumber(number);
						messageLogBean.setContent(content);
						messageLogBean.setState(messageLogBean.STATE_SUBMIT);
						messageLogBean.setAppCode(app_code);
						messageLogService.save(messageLogBean);
					}
				}

			}else {
				int resp = respInfo.getRespCode();
				System.out.println("监权失败:" + resp);
				if (resp == 1) {
					throw new Exception("监权失败"+resp+"：消息结构错");
				} else if (resp == 2) {
					throw new Exception("监权失败"+resp+"：非法源地址");
				} else if (resp == 3) {
					throw new Exception("监权失败"+resp+"：认证错误");
				} else if (resp == 4) {
					throw new Exception("监权失败"+resp+"：版本太高");
				} else if (resp == 5) {
					throw new Exception("监权失败"+resp+"：其他错误");
				} else if (resp == 6) {
					throw new Exception("监权失败"+resp+"：连接失败");
				} else if (resp == 7) {
					throw new Exception("监权失败"+resp+"：参数格式错误");
				} else {
					throw new Exception("监权失败"+resp);
				}
			}
			String code = "200";
			String msg = "短信提交成功！";
			json.put("state", code);
			json.put("msg", msg);
		}
		catch(Exception e){
			String code = "300";
			String msg = e.getMessage();
			//app_code、number、content为空不保存
			if(!(StringUtils.isEmpty(app_code) || StringUtils.isEmpty(number) || StringUtils.isEmpty(content) )) {
				messageLogBean.setCreateTime(new Date());
				messageLogBean.setId(String.valueOf(UUID.randomUUID()));
				messageLogBean.setNumber(number);
				messageLogBean.setContent(content);
				messageLogBean.setState(messageLogBean.STATE_ERROR);
				messageLogBean.setAppCode(app_code);
				messageLogBean.setErrorMsg(msg);
				messageLogService.save(messageLogBean);
			}

			json.put("state", code);
			json.put("error", msg);
			e.printStackTrace();
		}
		finally {
			if (passport != null)
				try {
					serviceApi.terminate(passport);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
		}
	}
}
