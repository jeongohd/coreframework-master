package coreframework.com.sec.ram.service;

import coreframework.com.sec.ram.mapper.CoreAuthorRoleManageMapper;
import coreframework.com.sec.ram.vo.AuthorRoleManage;
import coreframework.com.sec.ram.vo.AuthorRoleManageVO;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 권한별 롤관리에 대한 DAO 클래스를 정의한다.
 * @author 공통서비스 개발팀 이문준
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *  수정일               수정자              수정내용
 *  ----------   ---------   ---------------------------
 *  2009.03.11   이문준              최초 생성
 *  2021.02-09   신용호              updateAuthorRole 삭제
 *
 * </pre>
 */

@Service("CoreAuthorRoleManageService")
public class CoreAuthorRoleManageService extends EgovAbstractMapper {

	@Resource(name="CoreAuthorRoleManageMapper'")
    private CoreAuthorRoleManageMapper authorRoleManageDAO;
	
	/**
	 * 권한 롤 관계정보 목록 조회
	 * @param authorRoleManageVO AuthorRoleManageVO
	 * @return List<AuthorRoleManageVO>
	 * @exception Exception
	 */
	public List<AuthorRoleManageVO> selectAuthorRoleList(AuthorRoleManageVO authorRoleManageVO) throws Exception {
		return authorRoleManageDAO.selectAuthorRoleList(authorRoleManageVO);
	}
	
	/**
	 * 권한 롤 관계정보를 화면에서 입력하여 입력항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param authorRoleManage AuthorRoleManage
	 * @exception Exception
	 */
	public void insertAuthorRole(AuthorRoleManage authorRoleManage) throws Exception {
		authorRoleManageDAO.insertAuthorRole(authorRoleManage);
	}

	/**
	 * 권한 롤 관계정보를 화면에 조회하여 데이터베이스에서 삭제
	 * @param authorRoleManage AuthorRoleManage
	 * @exception Exception
	 */
	public void deleteAuthorRole(AuthorRoleManage authorRoleManage) throws Exception {
		authorRoleManageDAO.deleteAuthorRole(authorRoleManage);
	}

    /**
	 * 목록조회 카운트를 반환한다
	 * @param authorRoleManageVO AuthorRoleManageVO
	 * @return int
	 * @exception Exception
	 */
	public int selectAuthorRoleListTotCnt(AuthorRoleManageVO authorRoleManageVO) throws Exception {
		return authorRoleManageDAO.selectAuthorRoleListTotCnt(authorRoleManageVO);
	}
}