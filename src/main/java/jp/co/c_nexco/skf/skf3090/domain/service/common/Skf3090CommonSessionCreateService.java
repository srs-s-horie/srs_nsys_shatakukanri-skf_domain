package jp.co.c_nexco.skf.skf3090.domain.service.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.ScopedProxyMode;
import java.io.Serializable;


@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Skf3090CommonSessionCreateService implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String shainNo;
 
    public String getShainNo() {
        return shainNo;
    }

    public void setShainNo(String shainNo) {
        this.shainNo = shainNo;
    }

}
