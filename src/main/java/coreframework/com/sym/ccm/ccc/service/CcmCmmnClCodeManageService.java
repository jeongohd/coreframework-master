package coreframework.com.sym.ccm.ccc.service;

import coreframework.com.sym.ccm.ccc.mapper.CcmCmmnClCodeManageMapper;
import coreframework.com.sym.ccm.ccc.vo.CmmnClCode;
import coreframework.com.sym.ccm.ccc.vo.CmmnClCodeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
*
* 공통분류코드에 대한 서비스 구현클래스를 정의한다
* 
* @author 공통서비스 개발팀 이중호
* @since 2009.04.01
* @version 1.0
* @see
*
*      <pre>
* << 개정이력(Modification Information) >>
* 
*   수정일      수정자           수정내용
*  -------    --------    ---------------------------
*   2009.04.01  이중호          최초 생성
*
* </pre>
*/
@Slf4j
@Service("CcmCmmnClCodeManageService")
@RequiredArgsConstructor
public class CcmCmmnClCodeManageService extends EgovAbstractMapper{
	private static final Logger LOGGER = LoggerFactory.getLogger(CcmCmmnClCodeManageService.class);
//	@Resource(name = "CmmnClCodeManageDAO")
//	private CmmnClCodeManageDAO cmmnClCodeManageDAO;


    @Resource(name="CcmCmmnClCodeManageMapper")
	private  CcmCmmnClCodeManageMapper ccmCmmnClCodeManageMapper;

	/**
	 * 공통분류코드 총 갯수를 조회한다.
	 */

	public int selectCmmnClCodeListTotCnt(CmmnClCodeVO searchVO) throws Exception {
        return ccmCmmnClCodeManageMapper.selectCmmnClCodeListTotCnt(searchVO);
	}
	
	/**
	 * 공통분류코드 목록을 조회한다.
	 */

	public List<?> selectCmmnClCodeList(CmmnClCodeVO searchVO) throws Exception {
        return ccmCmmnClCodeManageMapper.selectCmmnClCodeList(searchVO);
	}
	
	/**
	 * 공통분류코드 상세항목을 조회한다.
	 */

	public CmmnClCode selectCmmnClCodeDetail(CmmnClCodeVO cmmnClCodeVO) throws Exception {
		return ccmCmmnClCodeManageMapper.selectCmmnClCodeDetail(cmmnClCodeVO);
	}
	
	/**
	 * 공통분류코드를 등록한다.
	 */

	public void insertCmmnClCode(CmmnClCodeVO cmmnClCodeVO) throws Exception {
		System.out.println("TEST4 : 등록 Serviceimpl");
    	ccmCmmnClCodeManageMapper.insertCmmnClCode(cmmnClCodeVO);
	}
	
	/**
	 * 공통분류코드를 삭제한다.
	 */

	public void deleteCmmnClCode(CmmnClCodeVO cmmnClCodeVO) throws Exception {
		ccmCmmnClCodeManageMapper.deleteCmmnClCode(cmmnClCodeVO);
	}
	
	/**
	 * 공통분류코드를 수정한다.
	 */

	public void updateCmmnClCode(CmmnClCodeVO cmmnClCodeVO) throws Exception {
		ccmCmmnClCodeManageMapper.updateCmmnClCode(cmmnClCodeVO);
		
	}
}
