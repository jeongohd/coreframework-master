package coreframework.com.sec.rmt.service;


import coreframework.com.sec.rmt.mapper.CoreRoleManageMapper;
import coreframework.com.sec.rmt.vo.RoleManage;
import coreframework.com.sec.rmt.vo.RoleManageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 롤관리에 관한 ServiceImpl 클래스를 정의한다.
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
@Slf4j
@RequiredArgsConstructor
@Service("CoreRoleManageService")
public class CoreRoleManageService extends EgovAbstractMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreRoleManageService.class);

	@Resource(name="CoreRoleManageMapper")
	public CoreRoleManageMapper coreRoleManageMapper;
	/**
	 * 등록된 롤 정보 조회
	 * @param roleManageVO RoleManageVO
	 * @return RoleManageVO
	 * @exception Exception
	 */
	public RoleManageVO selectRole(RoleManageVO roleManageVO) throws Exception {
		return coreRoleManageMapper.selectRole(roleManageVO);
	}

	/**
	 * 등록된 롤 정보 목록 조회
	 * @param roleManageVO RoleManageVO
	 * @return List<RoleManageVO>
	 * @exception Exception
	 */
	public List<RoleManageVO> selectRoleList(RoleManageVO roleManageVO) throws Exception {
		return coreRoleManageMapper.selectRoleList(roleManageVO);
	}

	/**
	 * 불필요한 롤정보를 화면에 조회하여 데이터베이스에서 삭제
	 * @param roleManage RoleManage
	 * @exception Exception
	 */
	public void deleteRole(RoleManage roleManage) throws Exception {
		coreRoleManageMapper.deleteRole(roleManage);
	}
	
	/**
	 * 시스템 메뉴에 따른 접근권한, 데이터 입력, 수정, 삭제의 권한 롤을 수정
	 * @param roleManage RoleManage
	 * @exception Exception
	 */
	public void updateRole(RoleManage roleManage) throws Exception {
		coreRoleManageMapper.updateRole(roleManage);
	}
	
	/**
	 * 시스템 메뉴에 따른 접근권한, 데이터 입력, 수정, 삭제의 권한 롤을 등록
	 * @param roleManage RoleManage
	 * @param roleManageVO RoleManageVO
	 * @return RoleManageVO
	 * @exception Exception
	 */
	public RoleManageVO insertRole(RoleManage roleManage, RoleManageVO roleManageVO) throws Exception {
		coreRoleManageMapper.insertRole(roleManage);
		roleManageVO.setRoleCode(roleManage.getRoleCode());
		return coreRoleManageMapper.selectRole(roleManageVO);
	}
	
    /**
	 * 목록조회 카운트를 반환한다
	 * @param roleManageVO RoleManageVO
	 * @return int
	 * @exception Exception
	 */
	public int selectRoleListTotCnt(RoleManageVO roleManageVO) throws Exception {
		return coreRoleManageMapper.selectRoleListTotCnt(roleManageVO);
	}
	
	/**
	 * 등록된 모든 롤 정보 목록 조회
	 * @param roleManageVO - 등록할 정보가 담긴 RoleManageVO
	 * @return List
	 * @exception Exception
	 */
	public List<RoleManageVO> selectRoleAllList(RoleManageVO roleManageVO) throws Exception {
		return coreRoleManageMapper.selectRoleAllList(roleManageVO);
	} 

}