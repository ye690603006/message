package inspur.dzzw.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import inspur.dzzw.web.dao.MessageLogDao;
import inspur.dzzw.web.po.MessageLogBean;

@Service
public class MessageLogService {

	@Autowired
	private MessageLogDao messageLogDao;
	
	public void save(MessageLogBean messageLogBean) {
		
		messageLogDao.save(messageLogBean);
	}
}
