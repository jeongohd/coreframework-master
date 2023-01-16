/**
 * 개요
 * - 로그인정책에 대한 ServiceImpl 클래스를 정의한다.
 * 
 * 상세내용
 * - 로그인정책에 대한 등록, 수정, 삭제, 조회, 반영확인 기능을 제공한다.
 * - 로그인정책의 조회기능은 목록조회, 상세조회로 구분된다.
 * @author lee.m.j
 * @version 1.0
 * @created 03-8-2009 오후 2:08:54
 *   <pre>
 * == 개정이력(Modification Information) ==
 * 
 *  수정일               수정자            수정내용
 *  ----------   --------   ---------------------------
 *  2009.08.03   이문준            최초 생성
 *  2021.02.18   신용호            selectLoginPolicyResult() 삭제
 * </pre>
 */

package coreframework.com.uat.uap.service;


import coreframework.com.uat.uap.mapper.CoreLoginPolicyMapper;
import coreframework.com.uat.uap.vo.LoginPolicy;
import coreframework.com.uat.uap.vo.LoginPolicyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("CoreLoginPolicyService")
public class CoreLoginPolicyService extends EgovAbstractMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreLoginPolicyService.class);


	@Resource(name="CoreLoginPolicyMapper")
	private CoreLoginPolicyMapper egovLoginPolicyMapper;
	/**
	 * 로그인정책 목록을 조회한다.
	 * @param loginPolicyVO - 로그인정책 VO
	 * @return List - 로그인정책 목록
	 */
	public List<LoginPolicyVO> selectLoginPolicyList(LoginPolicyVO loginPolicyVO) throws Exception {
		return egovLoginPolicyMapper.selectLoginPolicyList(loginPolicyVO);
	}

	/**
	 * 로그인정책 목록 수를 조회한다.
	 * @param loginPolicyVO - 로그인정책 VO
	 * @return int
	 */
	public int selectLoginPolicyListTotCnt(LoginPolicyVO loginPolicyVO) throws Exception {
		return egovLoginPolicyMapper.selectLoginPolicyListTotCnt(loginPolicyVO);
	}

	/**
	 * 로그인정책 목록의 상세정보를 조회한다.
	 * @param loginPolicyVO - 로그인정책 VO
	 * @return LoginPolicyVO - 로그인정책 VO
	 */
	public LoginPolicyVO selectLoginPolicy(LoginPolicyVO loginPolicyVO) throws Exception {
		return egovLoginPolicyMapper.selectLoginPolicy(loginPolicyVO);
	}

	/**
	 * 로그인정책 정보를 신규로 등록한다.
	 * @param loginPolicy - 로그인정책 model
	 */
	public void insertLoginPolicy(LoginPolicy loginPolicy) throws Exception {
		egovLoginPolicyMapper.insertLoginPolicy(loginPolicy);
	}

	/**
	 * 기 등록된 로그인정책 정보를 수정한다.
	 * @param loginPolicy - 로그인정책 model
	 */
	public void updateLoginPolicy(LoginPolicy loginPolicy) throws Exception {
		egovLoginPolicyMapper.updateLoginPolicy(loginPolicy);
	}

	/**
	 * 기 등록된 로그인정책 정보를 삭제한다.
	 * @param loginPolicy - 로그인정책 model
	 */
	public void deleteLoginPolicy(LoginPolicy loginPolicy) throws Exception {
		egovLoginPolicyMapper.deleteLoginPolicy(loginPolicy);
	}

	/**
	 * 로그인정책에 대한 현재 반영되어 있는 결과를 조회한다.
	 * @param loginPolicyVO - 로그인정책 VO
	 * @return LoginPolicyVO - 로그인정책 VO
	 */
	/*
	 * public LoginPolicyVO selectLoginPolicyResult(LoginPolicyVO loginPolicyVO)
	 * throws Exception { return
	 * loginPolicyDAO.selectLoginPolicyResult(loginPolicyVO); }
	 */
}
