package inspur.dzzw.web.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import inspur.dzzw.web.po.MessageLogBean;


public interface MessageLogDao extends JpaRepository<MessageLogBean, String>{

}
