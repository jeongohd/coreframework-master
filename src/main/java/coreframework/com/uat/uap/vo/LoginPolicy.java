/**
 * 개요
 * - 로그인정책에 대한 model 클래스를 정의한다.
 * 
 * 상세내용
 * - 로그인정책정보의 사용자ID, IP정보, 중복허용여부, 제한여부 항목을 관리한다.
 * @author lee.m.j
 * @version 1.0
 * @created 03-8-2009 오후 2:08:53
 *   <pre>
 * == 개정이력(Modification Information) ==
 * 
 *   수정일       수정자           수정내용
 *  -------     --------    ---------------------------
 *  2009.8.3    이문준     최초 생성
 * </pre>
 */

package coreframework.com.uat.uap.vo;

import coreframework.com.cmm.ComDefaultVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPolicy extends ComDefaultVO {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
    /**
	 * 사용자 ID
	 */	
	private String emplyrId;
    /**
	 * 사용자 명
	 */	
	private String emplyrNm;	
    /**
	 * 사용자 구분
	 */	
	private String emplyrSe;		
    /**
	 * IP정보
	 */	
    private String ipInfo;
    /**
	 * 중복허용여부
	 */	
    private String dplctPermAt;
    /**
	 * 제한여부
	 */	
    private String lmttAt;
    /**
	 * 등록자 ID
	 */	
    private String userId;
    /**
	 * 등록일시
	 */	
    private String regDate;
    /**
	 * 등록여부
	 */	
    private String regYn;
    

}
