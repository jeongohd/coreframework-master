package coreframework.com.sym.prm.mapper;

import coreframework.com.cmm.ComDefaultVO;
import coreframework.com.sym.prm.vo.ProgrmManageDtlVO;
import coreframework.com.sym.prm.vo.ProgrmManageVO;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 프로그램관리에 관한 서비스 인터페이스 클래스를 정의한다.
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

@Mapper("CoreProgrmManageMapper")
@Repository
public interface CoreProgrmManageMapper {
	/**
	 * 프로그램 상세정보를 조회
	 * @param vo ComDefaultVO
	 * @return ProgrmManageVO
	 * @exception Exception
	 */
	ProgrmManageVO selectProgrm(ProgrmManageVO vo) throws Exception;
	/**
	 * 프로그램 목록을 조회
	 * @param vo ComDefaultVO
	 * @return List
	 * @exception Exception
	 */
	List<?> selectProgrmList(ComDefaultVO vo) throws Exception;
	/**
	 * 프로그램목록 총건수를 조회한다.
	 * @param vo ComDefaultVO
	 * @return int
	 * @exception Exception
	 */
	int selectProgrmListTotCnt(ComDefaultVO vo) throws Exception;
	/**
	 * 프로그램 정보를 등록
	 * @param vo ProgrmManageVO
	 * @exception Exception
	 */
	void insertProgrm(ProgrmManageVO vo) throws Exception;

	/**
	 * 프로그램 정보를 수정
	 * @param vo ProgrmManageVO
	 * @exception Exception
	 */
	void updateProgrm(ProgrmManageVO vo) throws Exception;

	/**
	 * 프로그램 정보를 삭제
	 * @param vo ProgrmManageVO
	 * @exception Exception
	 */
	void deleteProgrm(ProgrmManageVO vo) throws Exception;

	/**
	 * 프로그램 파일 존재여부를 조회
	 * @param vo ComDefaultVO
	 * @return int
	 * @exception Exception
	 */
	int selectProgrmNMTotCnt(ComDefaultVO vo) throws Exception;

	/**
	 * 프로그램변경요청 정보를 조회
	 * @param vo ProgrmManageDtlVO
	 * @return ProgrmManageDtlVO  프로그램변경요청 리스트
	 * @exception Exception
	 */
	ProgrmManageDtlVO selectProgrmChangeRequst(ProgrmManageDtlVO vo) throws Exception;

	/**
	 * 프로그램변경요청 목록을 조회
	 * @param vo ComDefaultVO
	 * @return List
	 * @exception Exception
	 */
	List<?> selectProgrmChangeRequstList(ComDefaultVO vo) throws Exception;
	/**
	 * 프로그램변경요청목록 총건수를 조회한다.
	 * @param vo ComDefaultVO
	 * @return int
	 * @exception Exception
	 */
	int selectProgrmChangeRequstListTotCnt(ComDefaultVO vo) throws Exception;

	/**
	 * 프로그램변경요청을 등록
	 * @param vo ProgrmManageDtlVO
	 * @exception Exception
	 */
	void insertProgrmChangeRequst(ProgrmManageDtlVO vo) throws Exception;

	/**
	 * 프로그램변경요청을 수정
	 * @param vo ProgrmManageDtlVO
	 * @exception Exception
	 */
	void updateProgrmChangeRequst(ProgrmManageDtlVO vo) throws Exception;

	/**
	 * 프로그램변경요청을 삭제
	 * @param vo ProgrmManageDtlVO
	 * @exception Exception
	 */
	void deleteProgrmChangeRequst(ProgrmManageDtlVO vo) throws Exception;

	/**
	 * 프로그램변경요청 요청번호MAX 정보를 조회
	 * @param vo ProgrmManageDtlVO
	 * @return ProgrmManageDtlVO
	 * @exception Exception
	 */
	ProgrmManageDtlVO selectProgrmChangeRequstNo(ProgrmManageDtlVO vo) throws Exception;

	/**
	 * 프로그램변경요청처리 목록을 조회
	 * @param vo ComDefaultVO
	 * @return List
	 * @exception Exception
	 */
	List<?> selectChangeRequstProcessList(ComDefaultVO vo) throws Exception;

	/**
	 * 프로그램변경요청처리목록 총건수를 조회한다.
	 * @param vo ComDefaultVO
	 * @return int
	 * @exception Exception
	 */
	int selectChangeRequstProcessListTotCnt(ComDefaultVO vo) throws Exception;

	/**
	 * 프로그램변경요청처리를 수정
	 * @param vo ProgrmManageDtlVO
	 * @exception Exception
	 */
	void updateProgrmChangeRequstProcess(ProgrmManageDtlVO vo) throws Exception;

	/**
	 * 화면에 조회된 메뉴 목록 정보를 데이터베이스에서 삭제
	 * @param checkedProgrmFileNmForDel String
	 * @exception Exception
	 */
	void deleteProgrmManageList(String checkedProgrmFileNmForDel) throws Exception;

	/**
	 * 프로그램변경요청자 Email 정보를 조회
	 * @param vo ProgrmManageDtlVO
	 * @return ProgrmManageDtlVO  프로그램변경요청 리스트
	 * @exception Exception
	 */
	ProgrmManageDtlVO selectRqesterEmail(ProgrmManageDtlVO vo) throws Exception;

	int selectChangeRequstProcessListTotCnt_S(ComDefaultVO vo) throws Exception;

	int selectProgrmListTotCnt_S();

	void deleteAllProgrm();

	void deleteAllProgrmDtls();

	List<?> selectProgrmList_D(ComDefaultVO vo);
}