package coreframework.com.cmm.service.impl;

import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.service.CmmnDetailCode;
import coreframework.com.cmm.service.CoreCmmUseService;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Class Name : CoreCmmUseServiceImpl.java
 * @Description : 공통코드등 전체 업무에서 공용해서 사용해야 하는 서비스를 정의하기위한 서비스 구현 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 11.     이삼섭
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 11.
 * @version
 * @see
 *
 */
@Service("CoreCmmUseService")
public class CoreCmmUseServiceImpl extends EgovAbstractServiceImpl implements CoreCmmUseService {

    @Resource(name = "cmmUseDAO")
    private CmmUseDAO cmmUseDAO;

    /**
     * 공통코드를 조회한다.
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public List<CmmnDetailCode> selectCmmCodeDetail(ComDefaultCodeVO vo) throws Exception {
    	return cmmUseDAO.selectCmmCodeDetail(vo);
    }

    /**
     * ComDefaultCodeVO의 리스트를 받아서 여러개의 코드 리스트를 맵에 담아서 리턴한다.
     *
     * @param voList
     * @return
     * @throws Exception
     */
    public Map<String, List<CmmnDetailCode>> selectCmmCodeDetails(List<?> voList) throws Exception {
		ComDefaultCodeVO vo;
		Map<String, List<CmmnDetailCode>> map = new HashMap<String, List<CmmnDetailCode>>();

		Iterator<?> iter = voList.iterator();
		while (iter.hasNext()) {
		    vo = (ComDefaultCodeVO)iter.next();
		    map.put(vo.getCodeId(), cmmUseDAO.selectCmmCodeDetail(vo));
		}

		return map;
    }

    /**
     * 조직정보를 코드형태로 리턴한다.
     *
     * @param 조회조건정보 vo
     * @return 조직정보 List
     * @throws Exception
     */
    public List<CmmnDetailCode> selectOgrnztIdDetail(ComDefaultCodeVO vo) throws Exception {
    	return cmmUseDAO.selectOgrnztIdDetail(vo);
    }

    /**
     * 그룹정보를 코드형태로 리턴한다.
     *
     * @param 조회조건정보 vo
     * @return 그룹정보 List
     * @throws Exception
     */
    public List<CmmnDetailCode> selectGroupIdDetail(ComDefaultCodeVO vo) throws Exception {
    	return cmmUseDAO.selectGroupIdDetail(vo);
    }
}
