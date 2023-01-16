package coreframework.com.sec.ram.vo;

import coreframework.com.cmm.ComDefaultVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 권한관리에 대한 model 클래스를 정의한다.
 * @author 공통서비스 개발팀 이문준
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.20  이문준          최초 생성
 *
 * </pre>
 */
@Getter
@Setter
public class AuthorManage extends ComDefaultVO {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 권한관리
	 */	
	private AuthorManage authorManage;
	/**
	 * 권한코드
	 */
	private String authorCode;
	/**
	 * 권한등록일자
	 */
	private String authorCreatDe;
	/**
	 * 권한코드설명
	 */
	private String authorDc;
	/**
	 * 권한 명
	 */
	private String authorNm;
	


}
