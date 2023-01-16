package coreframework.com.sec.rgm.service;

import coreframework.com.sec.rgm.mapper.CoreAuthorGroupMapper;
import coreframework.com.sec.rgm.vo.AuthorGroup;
import coreframework.com.sec.rgm.vo.AuthorGroupVO;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 권한그룹에 관한 ServiceImpl 클래스를 정의한다.
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
 *   2009.03.11  이문준          최초 생성
 *
 * </pre>
 */

@Service("CoreAuthorGroupService")
public class CoreAuthorGroupService extends EgovAbstractMapper {
	
	@Resource(name="CoreAuthorGroupMapper")
    private CoreAuthorGroupMapper coreAuthorGroupMapper;

	/**
	 * 그룹별 할당된 권한 목록 조회
	 * @param authorGroupVO AuthorGroupVO
	 * @return List<AuthorGroupVO>
	 * @exception Exception
	 */
	public List<AuthorGroupVO> selectAuthorGroupList(AuthorGroupVO authorGroupVO) throws Exception{
		return coreAuthorGroupMapper.selectAuthorGroupList(authorGroupVO);
	}
	
	/**
	 * 그룹에 권한정보를 할당하여 데이터베이스에 등록
	 * @param authorGroup AuthorGroup
	 * @exception Exception
	 */
	public void insertAuthorGroup(AuthorGroup authorGroup) throws Exception{
		coreAuthorGroupMapper.insertAuthorGroup(authorGroup);
	}
	
	/**
	 * 화면에 조회된 그룹권한정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param authorGroup AuthorGroup
	 * @exception Exception
	 */
	public void updateAuthorGroup(AuthorGroup authorGroup) throws Exception{
		coreAuthorGroupMapper.updateAuthorGroup(authorGroup);
	}

	/**
	 * 그룹별 할당된 시스템 메뉴 접근권한을 삭제
	 * @param authorGroup AuthorGroup
	 * @exception Exception
	 */
	public void deleteAuthorGroup(AuthorGroup authorGroup) throws Exception {
		coreAuthorGroupMapper.deleteAuthorGroup(authorGroup);
	}
	
    /**
	 * 목록조회 카운트를 반환한다
	 * @param authorGroupVO AuthorGroupVO
	 * @return int
	 * @exception Exception
	 */
	public int selectAuthorGroupListTotCnt(AuthorGroupVO authorGroupVO) throws Exception {
		return coreAuthorGroupMapper.selectAuthorGroupListTotCnt(authorGroupVO);
    }
	
}