package coreframework.com.sym.prm.service;

import coreframework.com.cmm.ComDefaultVO;
import coreframework.com.sym.prm.mapper.CoreProgrmManageMapper;
import coreframework.com.sym.prm.vo.ProgrmManageDtlVO;
import coreframework.com.sym.prm.vo.ProgrmManageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 프로그램목록관리 및 프로그램변경관리에 관한 비즈니스 구현 클래스를 정의한다.
 * @author 개발환경 개발팀 이용
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.20  이  용          최초 생성
 *
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("CoreProgrmManageService")
public class CoreProgrmManageService extends EgovAbstractMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreProgrmManageService.class);
	@Resource(name="CoreProgrmManageMapper")
    private CoreProgrmManageMapper coreProgrmManageMapper;


	/**
	 * 프로그램 상세정보를 조회
	 * @param vo ComDefaultVO
	 * @return ProgrmManageVO
	 * @exception Exception
	 */

	public ProgrmManageVO selectProgrm(ProgrmManageVO vo) throws Exception{
         	return coreProgrmManageMapper.selectProgrm(vo);
	}
	/**
	 * 프로그램 목록을 조회
	 * @param vo ComDefaultVO
	 * @return List
	 * @exception Exception
	 */

	public List<?> selectProgrmList(ComDefaultVO vo) throws Exception {
   		return coreProgrmManageMapper.selectProgrmList_D(vo);
	}
	/**
	 * 프로그램목록 총건수를 조회한다.
	 * @param vo  ComDefaultVO
	 * @return Integer
	 * @exception Exception
	 */

	public int selectProgrmListTotCnt(ComDefaultVO vo) throws Exception {
        return coreProgrmManageMapper.selectProgrmListTotCnt(vo);
	}
	/**
	 * 프로그램 정보를 등록
	 * @param vo ProgrmManageVO
	 * @exception Exception
	 */

	public void insertProgrm(ProgrmManageVO vo) throws Exception {
    	coreProgrmManageMapper.insertProgrm(vo);
	}

	/**
	 * 프로그램 정보를 수정
	 * @param vo ProgrmManageVO
	 * @exception Exception
	 */

	public void updateProgrm(ProgrmManageVO vo) throws Exception {
    	coreProgrmManageMapper.updateProgrm(vo);
	}

	/**
	 * 프로그램 정보를 삭제
	 * @param vo ProgrmManageVO
	 * @exception Exception
	 */

	public void deleteProgrm(ProgrmManageVO vo) throws Exception {
    	coreProgrmManageMapper.deleteProgrm(vo);
	}

	/**
	 * 프로그램 파일 존재여부를 조회
	 * @param vo ComDefaultVO
	 * @return int
	 * @exception Exception
	 */

	public int selectProgrmNMTotCnt(ComDefaultVO vo) throws Exception{
		return coreProgrmManageMapper.selectProgrmNMTotCnt(vo);
	}

	/**
	 * 프로그램변경요청 정보를 조회
	 * @param vo ProgrmManageDtlVO
	 * @return ProgrmManageDtlVO
	 * @exception Exception
	 */

	public ProgrmManageDtlVO selectProgrmChangeRequst(ProgrmManageDtlVO vo) throws Exception{
       	return coreProgrmManageMapper.selectProgrmChangeRequst(vo);
	}

	/**
	 * 프로그램변경요청 목록을 조회
	 * @param vo ComDefaultVO
	 * @return List
	 * @exception Exception
	 */

	public List<?> selectProgrmChangeRequstList(ComDefaultVO vo) throws Exception {
   		return coreProgrmManageMapper.selectProgrmChangeRequstList(vo);
	}

	/**
	 * 프로그램변경요청목록 총건수를 조회한다.
	 * @param vo ComDefaultVO
	 * @return int
	 * @exception Exception
	 */

	public int selectProgrmChangeRequstListTotCnt(ComDefaultVO vo) throws Exception {
        return coreProgrmManageMapper.selectProgrmChangeRequstListTotCnt(vo);
	}

	/**
	 * 프로그램변경요청을 등록
	 * @param vo ProgrmManageDtlVO
	 * @exception Exception
	 */

	public void insertProgrmChangeRequst(ProgrmManageDtlVO vo) throws Exception {
    	coreProgrmManageMapper.insertProgrmChangeRequst(vo);
	}

	/**
	 * 프로그램변경요청을 수정
	 * @param vo ProgrmManageDtlVO
	 * @exception Exception
	 */

	public void updateProgrmChangeRequst(ProgrmManageDtlVO vo) throws Exception {
    	coreProgrmManageMapper.updateProgrmChangeRequst(vo);
	}

	/**
	 * 프로그램변경요청을 삭제
	 * @param vo ProgrmManageDtlVO
	 * @exception Exception
	 */

	public void deleteProgrmChangeRequst(ProgrmManageDtlVO vo) throws Exception {
    	coreProgrmManageMapper.deleteProgrmChangeRequst(vo);
	}

	/**
	 * 프로그램변경요청 요청번호MAX 정보를 조회
	 * @param vo ProgrmManageDtlVO
	 * @return ProgrmManageDtlVO
	 * @exception Exception
	 */

	public ProgrmManageDtlVO selectProgrmChangeRequstNo(ProgrmManageDtlVO vo) throws Exception {
   		return coreProgrmManageMapper.selectProgrmChangeRequstNo(vo);
	}

	/**
	 * 프로그램변경요청처리 목록을 조회
	 * @param vo ComDefaultVO
	 * @return List
	 * @exception Exception
	 */

	public List<?> selectChangeRequstProcessList(ComDefaultVO vo) throws Exception {
   		return coreProgrmManageMapper.selectChangeRequstProcessList(vo);
	}

	/**
	 * 프로그램변경요청처리목록 총건수를 조회한다.
	 * @param vo ComDefaultVO
	 * @return int
	 * @exception Exception
	 */

	public int selectChangeRequstProcessListTotCnt(ComDefaultVO vo) throws Exception {
        return coreProgrmManageMapper.selectChangeRequstProcessListTotCnt_S(vo);
	}

	/**
	 * 프로그램변경요청처리를 수정
	 * @param vo ProgrmManageDtlVO
	 * @exception Exception
	 */

	public void updateProgrmChangeRequstProcess(ProgrmManageDtlVO vo) throws Exception {
    	coreProgrmManageMapper.updateProgrmChangeRequstProcess(vo);
	}

	/**
	 * 화면에 조회된 메뉴 목록 정보를 데이터베이스에서 삭제
	 * @param checkedProgrmFileNmForDel String
	 * @exception Exception
	 */

	public void deleteProgrmManageList(String checkedProgrmFileNmForDel) throws Exception {
		ProgrmManageVO vo = null;
		String [] delProgrmFileNm = checkedProgrmFileNmForDel.split(",");
		for (int i=0; i<delProgrmFileNm.length ; i++){
			vo = new ProgrmManageVO();
			vo.setProgrmFileNm(delProgrmFileNm[i]);
			coreProgrmManageMapper.deleteProgrm(vo);
		}
	}

	/**
	 * 프로그램변경요청자 Email 정보를 조회
	 * @param vo ProgrmManageDtlVO
	 * @return ProgrmManageDtlVO
	 * @exception Exception
	 */

	public ProgrmManageDtlVO selectRqesterEmail(ProgrmManageDtlVO vo) throws Exception{
       	return coreProgrmManageMapper.selectRqesterEmail(vo);
	}


}