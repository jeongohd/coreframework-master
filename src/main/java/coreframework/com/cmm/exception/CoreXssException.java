package coreframework.com.cmm.exception;

import org.egovframe.rte.fdl.cmmn.exception.BaseRuntimeException;

/**
 * CoreXssException 클래스
 *
 * @author 장동한
 * @since 2016.10.27
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일        수정자           수정내용
 *  -------      -------------  ----------------------
 *   2016.10.17  장동한           최초 생성
 * </pre>
 */

public class CoreXssException extends BaseRuntimeException {
    
	private static final long serialVersionUID = 1L;
	 
	/**
	 * CoreXssException 생성자.
	 * 
	 * @param defaultMessage 메세지 지정
	 * @param wrappedException 원인 Exception
	 */
	public CoreXssException(String message, String messageKey) {
		this.messageKey = messageKey;
		this.messageParameters = null;
		this.message = message;
		this.wrappedException = null;
	}

}
