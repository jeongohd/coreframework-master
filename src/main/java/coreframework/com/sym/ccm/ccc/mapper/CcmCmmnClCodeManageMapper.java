package coreframework.com.sym.ccm.ccc.mapper;

import coreframework.com.sym.ccm.ccc.vo.CmmnClCode;
import coreframework.com.sym.ccm.ccc.vo.CmmnClCodeVO;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * description    :
 * packageName    : coreframework.com.sym.ccm.ccc.mapper
 * fileName       : CcmCmmnClCodeManageMapper
 * author         : yoonsikcha
 * date           : 2022/12/08
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/12/08        yoonsikcha       최초 생성
 */
@Mapper("CcmCmmnClCodeManageMapper")
@Repository
public interface CcmCmmnClCodeManageMapper {

    /**
     * 공통분류코드 총 갯수를 조회한다.
     *
     * @param searchVO
     * @return int(공통분류코드 총 갯수)
     */
    int selectCmmnClCodeListTotCnt(CmmnClCodeVO searchVO) throws Exception;

    /**
     * 공통분류코드 목록을 조회한다.
     * @param searchVO
     * @return List(공통분류코드 목록)
     * @throws Exception
     */
    List<?> selectCmmnClCodeList(CmmnClCodeVO searchVO);

    /**
     *  공통분류코드 상세항목을 조회한다.
     * @param cmmnClCode
     * @return CmmnClCode(공통분류코드)
     *  @throws Exception
     */
    CmmnClCode selectCmmnClCodeDetail(CmmnClCodeVO cmmnClCodeVO) throws Exception;

    /**
     * 공통분류코드를 등록한다.
     * @param cmmnClCodeVO
     * @throws Exception
     */
    void insertCmmnClCode(CmmnClCodeVO cmmnClCodeVO) throws Exception;

    /**
     * 공통분류코드를 삭제한다.
     * @param cmmnClCodeVO
     * @throws Exception
     */
    void deleteCmmnClCode(CmmnClCodeVO cmmnClCodeVO) throws Exception;

    /**
     * 공통분류코드를 수정한다.
     * @param cmmnClCodeVO
     * @throws Exception
     */
    void updateCmmnClCode(CmmnClCodeVO cmmnClCodeVO) throws Exception;

}
