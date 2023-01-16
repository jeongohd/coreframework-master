package coreframework.com.sec.gmt.service;

import coreframework.com.sec.gmt.mapper.CoreGroupManageMapper;
import coreframework.com.sec.gmt.vo.GroupManage;
import coreframework.com.sec.gmt.vo.GroupManageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 그룹관리에 관한 ServiceImpl 클래스를 정의한다.
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
@Service("CoreGroupManageService")
public class CoreGroupManageService extends EgovAbstractMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreGroupManageService.class);

	@Resource(name="CoreGroupManageMapper")
	private CoreGroupManageMapper coreGroupManageMapper;

	/**
	 * 시스템사용 목적별 그룹 목록 조회
	 * @param groupManageVO GroupManageVO
	 * @return List<GroupManageVO>
	 * @exception Exception
	 */
	public List<GroupManageVO> selectGroupList(GroupManageVO groupManageVO) throws Exception {
		return coreGroupManageMapper.selectGroupList(groupManageVO);
	}
	
	/**
	 * 검색조건에 따른 그룹정보를 조회
	 * @param groupManageVO GroupManageVO
	 * @return GroupManageVO
	 * @exception Exception
	 */
	public GroupManageVO selectGroup(GroupManageVO groupManageVO) throws Exception {
		return coreGroupManageMapper.selectGroup(groupManageVO);
	}

	/**
	 * 그룹 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param groupManage GroupManage
	 * @param groupManageVO GroupManageVO
	 * @return GroupManageVO
	 * @exception Exception
	 */
	public GroupManageVO insertGroup(GroupManage groupManage, GroupManageVO groupManageVO) throws Exception {
		coreGroupManageMapper.insertGroup(groupManage);
		groupManageVO.setGroupId(groupManage.getGroupId());
		return coreGroupManageMapper.selectGroup(groupManageVO);
	}

	/**
	 * 화면에 조회된 그룹의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param groupManage GroupManage
	 * @exception Exception
	 */
	public void updateGroup(GroupManage groupManage) throws Exception {
		coreGroupManageMapper.updateGroup(groupManage);
	}
	
	/**
	 * 불필요한 그룹정보를 화면에 조회하여 데이터베이스에서 삭제
	 * @param groupManage GroupManage
	 * @exception Exception
	 */
	public void deleteGroup(GroupManage groupManage) throws Exception {
		coreGroupManageMapper.deleteGroup(groupManage);
	}
	
    /**
	 * 목록조회 카운트를 반환한다
	 * @param groupManageVO GroupManageVO
	 * @return int
	 * @exception Exception
	 */
	public int selectGroupListTotCnt(GroupManageVO groupManageVO) throws Exception {
		return coreGroupManageMapper.selectGroupListTotCnt(groupManageVO);
	}
}