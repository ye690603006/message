package inspur.dzzw.web.po;



import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "message_log",schema = "hibernatetest")
public class MessageLogBean {

    // 已提交
    public final String STATE_SUBMIT = "0";
    // 已发送
    public final String STATE_SUCCESS = "1";
    // 发送失败
    public final String STATE_ERROR = "2";

    @Id
    @Column(name = "ID",length = 36 ,nullable = false,unique = true)
    private String id;

    @Column(name = "MSGID",length = 50, nullable = true)
    private String msgid;

    @Column(name = "NUMBER", nullable = false)
    private String number;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "STATE", nullable = false)
    private String state;

    @Column(name = "CREATE_TIME", nullable = true, insertable = true)
    private Date createTime;

    @Column(name = "UPDATE_TIME", nullable = true, updatable = true)
    private Date updateTime;
    
    @Column(name = "APP_CODE", nullable = false)
    private String appCode;
    
    @Column(name = "ERROR_MSG", nullable = true)
    private String errorMsg;
    
    public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
